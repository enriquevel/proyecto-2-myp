package myp.proyecto2.model.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import myp.proyecto2.model.domain.*;
import myp.proyecto2.model.domain.builder.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class TomTomRouteProvider implements RouteProvider {

    private final String apiKey;
    private final Map<RouteQuery, List<Route>> cachedRoutes;
    private static final String API_URL = "https://api.tomtom.com/routing/1/calculateRoute";
    private static final int DEFAULT_TIMEOUT = 10000;

    public TomTomRouteProvider(String apiKey) {
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
            routes.addAll(parseTomTomResponse(jsonResponse, query));
        } catch (Exception e) {
            System.err.printf("Failed to get routes for %s: %s", mode, e.getMessage());
        }

        if (routes.isEmpty())
            throw new IllegalArgumentException("No routes found");

        this.cachedRoutes.put(query, routes);

        return routes;
    }

    private String buildRequestUrl(Location from, Location to, TransportMode mode) {
        return API_URL + "/" +
                from.getLatitude() + "," + to.getLongitude() + ":" +
                from.getLatitude() + "," + to.getLongitude() + "/json?" +
                "key=" + apiKey +
                "&travelMode=" + mode.getTomTomMode() +
                "&traffic=true" +
                "&computeTravelTimeFor=all" +
                "&instructionsType=text" +
                "&maxAlternatives=3" +
                "&alternativeType=anyRoute";
    }

    private String executeRequest(String urlString) throws APIException {
        HttpURLConnection connection = null;

        try {
            URI uri = new URI(urlString);
            connection = (HttpURLConnection) uri.toURL().openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "CampusRouter/1.0");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_TIMEOUT);

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                String errorBody = readStream(connection.getErrorStream());
                if (errorBody == null || errorBody.isBlank())
                    errorBody = "No error body returned.";
                throw new APIException(String.format("TomTom API HTTP %d: %s", responseCode, errorBody));
            }
            return readStream(connection.getInputStream());

        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            throw new APIException("Failed to execute TomTom API request: " + e.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return "";

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);
        }
        return response.toString();
    }

    private List<Route> parseTomTomResponse(String jsonResponse, RouteQuery query) {
        try {
            JSONObject json = new JSONObject(jsonResponse);

            if (json.has("detailedError"))
                handleApiError(json.getJSONObject("detailedError"));

            if (!json.has("routes"))
                throw new RuntimeException("Response missing 'routes' field");

            JSONArray routesArray = json.getJSONArray("routes");

            if (routesArray.length() == 0)
                throw new RuntimeException("No TomTom routes found");

            List<Route> routes = new ArrayList<>();

            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject routeJson = routesArray.getJSONObject(i);
                Route route = convertJsonToRoute(routeJson, query.from(), query.to());
                routes.add(route);
            }

            return routes;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse TomTom response: " + e.getMessage(), e);
        }
    }

    private void handleApiError(JSONObject err) {
        String code = err.optString("code", "UNKNOWN");
        String msg = err.optString("message", "No message");
        throw new RuntimeException("TomTom API Error [" + code + "]: " + msg);
    }

    private Route convertJsonToRoute(JSONObject routeJson, Location origin, Location destination) throws Exception {
        JSONObject summary = routeJson.getJSONObject("summary");

        double totalDistance = summary.getDouble("lengthInMeters");
        int totalDuration = summary.getInt("travelTimeInSeconds");

        List<RouteSegment> segments = extractSegments(routeJson);
        List<Location> polyline = extractPolyline(routeJson);
        Set<TransportMode> modes = extractTransportModes(routeJson);

        RouteBuilder builder = new DefaultRouteBuilder();

        return builder
                .setOrigin(origin)
                .setDestination(destination)
                .setDistance(totalDistance)
                .setDuration(totalDuration)
                .setSegments(segments)
                .setPathPoints(polyline)
                .setTransportModes(modes)
                .build();
    }

    private List<RouteSegment> extractSegments(JSONObject routeJson) {
        List<RouteSegment> list = new ArrayList<>();

        if (!routeJson.has("guidance"))
            return createFallbackSegment(routeJson);

        JSONObject guidance = routeJson.getJSONObject("guidance");

        if (!guidance.has("instructions"))
            return createFallbackSegment(routeJson);

        JSONArray instructions = guidance.getJSONArray("instructions");

        List<Location> allPoints = extractAllPoints(routeJson);
        JSONObject summary = routeJson.getJSONObject("summary");
        double totalDistance = summary.getDouble("lengthInMeters");

        for (int i = 0; i < instructions.length(); i++) {
            JSONObject inst = instructions.getJSONObject(i);

            String message = inst.optString("message", "Continue");
            String tomtomMode = inst.optString("travelMode", "car");
            TransportMode mode = TransportMode.fromTomTomString(tomtomMode);

            int offset = inst.optInt("routeOffsetInMeters", 0);
            int duration = inst.optInt("travelTimeInSeconds", 0);
            int pointIndex = inst.optInt("pointIndex", i);

            double segmentDistance;
            if (i < instructions.length() - 1) {
                int next = instructions.getJSONObject(i + 1)
                        .optInt("routeOffsetInMeters", offset);
                segmentDistance = next - offset;
            } else {
                segmentDistance = totalDistance - offset;
            }

            Location start = (pointIndex < allPoints.size())
                    ? allPoints.get(pointIndex)
                    : allPoints.get(0);

            Location end = (i + 1 < allPoints.size())
                    ? allPoints.get(i + 1)
                    : allPoints.get(allPoints.size() - 1);

            list.add(new RouteSegment(
                    message,
                    segmentDistance,
                    duration,
                    start,
                    end,
                    mode
            ));
        }

        return list.isEmpty() ? createFallbackSegment(routeJson) : list;
    }

    private List<RouteSegment> createFallbackSegment(JSONObject routeJson) {
        try {
            JSONObject summary = routeJson.getJSONObject("summary");
            JSONArray legs = routeJson.getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            JSONArray points = leg.getJSONArray("points");

            JSONObject first = points.getJSONObject(0);
            JSONObject last = points.getJSONObject(points.length() - 1);

            Location start = new Location(first.getDouble("latitude"), first.getDouble("longitude"), null);
            Location end = new Location(last.getDouble("latitude"), last.getDouble("longitude"), null);

            RouteSegment seg = new RouteSegment(
                    "Proceed to destination",
                    summary.getInt("travelTimeInSeconds"),
                    summary.getDouble("lengthInMeters"),
                    start,
                    end,
                    TransportMode.WALKING

                    );

            return List.of(seg);

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<Location> extractAllPoints(JSONObject routeJson) {
        List<Location> list = new ArrayList<>();

        try {
            JSONArray legs = routeJson.getJSONArray("legs");

            for (int i = 0; i < legs.length(); i++) {
                JSONArray pts = legs.getJSONObject(i).getJSONArray("points");

                for (int j = 0; j < pts.length(); j++) {
                    JSONObject p = pts.getJSONObject(j);

                    list.add(new Location(
                            p.getDouble("latitude"),
                            p.getDouble("longitude"),
                            null
                    ));
                }
            }
        } catch (Exception ignored) { }

        return list;
    }

    private List<Location> extractPolyline(JSONObject routeJson) {
        return extractAllPoints(routeJson);
    }

    private Set<TransportMode> extractTransportModes(JSONObject routeJson) {
        Set<TransportMode> set = new HashSet<>();

        try {
            if (routeJson.has("sections")) {
                JSONArray sections = routeJson.getJSONArray("sections");

                for (int i = 0; i < sections.length(); i++) {
                    String m = sections.getJSONObject(i).optString("travelMode", "car");
                    set.add(TransportMode.fromTomTomString(m));
                }
            }
        } catch (Exception ignored) { }

        if (set.isEmpty())
            set.add(TransportMode.WALKING);

        return set;
    }


    /**
     * @return
     */
    @Override
    public String getProviderName() {
        return "TomTom API";
    }
}
