package myp.proyecto2.model.provider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.RouteSegment;
import myp.proyecto2.model.domain.TransportMode;
import myp.proyecto2.model.domain.builder.*;
import myp.proyecto2.model.util.IDGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleMapsRouteProvider implements RouteProvider {

    private final String apiKey;
    private final Map<RouteQuery, List<Route>> cachedRoutes;
    private static final String API_URL = "https://maps.googleapis.com/maps/api/directions/json";
    private static final int DEFAULT_TIMEOUT = 10000;

    public GoogleMapsRouteProvider(String apiKey) {
        this.apiKey = apiKey;
        this.cachedRoutes = new HashMap<>();
    }

    /**
     * @param from
     * @param to
     * @param mode
     * @return
     */
    @Override
    public List<Route> getRoutes(Location from, Location to, TransportMode mode) {
        RouteQuery query = new RouteQuery(from, to, mode);
        List<Route> cached = this.cachedRoutes.get(query);
        if (cached != null)
            return cached;

        List<Route> routes = new ArrayList<>();

        try {
            String url = buildRequestUrl(from, to, mode);
            String jsonResponse = executeRequest(url);
            routes.addAll(parseGoogleResponse(jsonResponse, query));
        } catch (Exception e) {
            System.err.printf("Failed to get routes for %s: %s", mode, e.getMessage());
        }

        if (routes.isEmpty())
            throw new IllegalArgumentException("No routes found");

        this.cachedRoutes.put(query, routes);

        return routes;
    }

    /**
     * @return
     */
    @Override
    public String getProviderName() {
        return "Google Maps Directions API";
    }

    private String buildRequestUrl(Location from, Location to, TransportMode mode) {
        return API_URL + "?origin=" + from.getLatitude() + "," + from.getLongitude() +
                "&destination=" + to.getLatitude() + "," + to.getLongitude() +
                "&mode=" + mode.getGoogleMapsMode() +
                "&alternatives=true" +
                "&key=" + apiKey;
    }

    private String executeRequest(String url) throws APIException {
        HttpURLConnection connection = null;

        try {
            URI uri = new URI(url);
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_TIMEOUT);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200)
                throw new APIException("API returned response code " + responseCode);

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null)
                    response.append(line);
            }

            return response.toString();

        } catch (Exception e) {
            throw new APIException("Failed to execute Google API request: " + e.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private List<Route> parseGoogleResponse(String jsonResponse, RouteQuery query) throws APIException {
        try {
            JSONObject json = new JSONObject(jsonResponse);

            String status = json.getString("status");
            if (!status.equals("OK")) {
                String errorMessage = json.optString("error_message", "Unknown error");
                throw new APIException(errorMessage);
            }

            JSONArray routesArray = json.getJSONArray("routes");
            List<Route> routes = new ArrayList<>();

            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject routeJson = routesArray.getJSONObject(i);
                Route route = convertToRoute(routeJson, query.from(), query.to());
                routes.add(route);
            }

            return routes;

        } catch (Exception e) {
            throw new APIException("Failed to parse Google response: " + e.getMessage());
        }
    }

    private Route convertToRoute(JSONObject routeJson, Location origin, Location destination) throws Exception {
        JSONArray legs = routeJson.getJSONArray("legs");
        JSONObject leg = legs.getJSONObject(0);

        double totalDistance = leg.getJSONObject("distance").getInt("value");
        int totalDuration = leg.getJSONObject("duration").getInt("value");
        List<RouteSegment> segments = extractSegments(leg.getJSONArray("steps"));
        List<Location> pathPoints = extractPathPoints(routeJson);
        Set<TransportMode> modes = extractTransportModes(segments);

        RouteBuilder builder = new DefaultRouteBuilder();
        return builder.setId(IDGenerator.generateSequentialID("ROU"))
                .setOrigin(origin)
                .setDestination(destination)
                .setDistance(totalDistance)
                .setDuration(totalDuration)
                .setSegments(segments)
                .setPathPoints(pathPoints)
                .setTransportModes(modes)
                .build();
    }

    private List<RouteSegment> extractSegments(JSONArray stepsArray) {
        List<RouteSegment> segments = new ArrayList<>();

        for (int i = 0; i < stepsArray.length(); i++) {
            JSONObject step = stepsArray.getJSONObject(i);

            String travelMode = step.getString("travel_mode");
            TransportMode mode = TransportMode.fromGoogleString(travelMode.toLowerCase());

            String instruction = step.getString("html_instructions")
                    .replaceAll("<[^>]*>", "");

            int duration = step.getJSONObject("duration").getInt("value");
            double distance = step.getJSONObject("distance").getInt("value");

            JSONObject startLoc = step.getJSONObject("start_location");
            Location startLocation = new Location(startLoc.getDouble("lat"),
                    startLoc.getDouble("lng"));

            JSONObject endLoc = step.getJSONObject("end_location");
            Location endLocation = new Location(endLoc.getDouble("lat"),
                    endLoc.getDouble("lng"));

            RouteSegment segment = new RouteSegment(instruction, distance, duration, startLocation, endLocation, mode);
            segments.add(segment);
        }

        return segments;
    }

    private List<Location> extractPathPoints(JSONObject routeJson) {
        try {
            String encodedPolyline = routeJson.getJSONObject("overview_polyline").getString("points");
            return decodePolyline(encodedPolyline);
        } catch (Exception e) {
            System.err.println("Failed to decode polyline: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Location> decodePolyline(String encoded) {
        // Algoritmo de la documentacion de Google Maps
        List<Location> polyline = new ArrayList<>();
        int index = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            double latitude = lat / 1e5;
            double longitude = lng / 1e5;

            polyline.add(new Location(latitude, longitude));
        }

        return polyline;
    }

    private Set<TransportMode> extractTransportModes(List<RouteSegment> segments) {
        Set<TransportMode> modes = new HashSet<>();
        for (RouteSegment segment : segments)
            modes.add(segment.getMode());
        return modes;
    }

}