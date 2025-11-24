package myp.proyecto2.model.provider;

public class RouteProviderFactory {

    private RouteProviderFactory() {}

    public static RouteProvider createRouteProvider(String provider, String apiKey) {
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
}
