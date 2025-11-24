package myp.proyecto2.model.provider;

import java.util.List;

public class RouteProviderFactory {

    public RouteProviderFactory() {}

    public RouteProvider createRouteProvider(String provider, String apiKey) {
        if (provider == null)
            throw new NullPointerException("Provider cannot be null");

        if (apiKey == null)
            throw new NullPointerException("API key cannot be null");

        return switch (provider.toLowerCase()) {
            case "tomtom" -> new TomTomRouteProvider(apiKey);
            case "google" -> new GoogleMapsRouteProvider(apiKey);
            default -> throw new IllegalArgumentException("Unknown route provider.");
        };
    }

    public static List<String> getAvailableProviders() {
        return List.of("Google Maps Directions API", "TomTom Routing API");
    }
}
