package myp.proyecto2.model.provider;

public class RouteProviderFactory {

    private RouteProviderFactory() {}

    public static RouteProvider createRouteProvider(boolean openSource, String apiKey) {
        return openSource ? new TomTomRouteProvider(apiKey) : new GoogleMapsRouteProvider(apiKey);
    }
}
