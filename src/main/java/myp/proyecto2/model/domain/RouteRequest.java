package myp.proyecto2.model.domain;

public record RouteRequest(Location from, Location to, TransportMode mode, RoutePreference routePreference) {}
