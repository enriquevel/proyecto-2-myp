package myp.proyecto2.view.javafx;

import myp.proyecto2.model.domain.Location;

public class UniversityBounds {

    private final Location northwest;
    private final Location southeast;

    public UniversityBounds(Location northwest, Location southeast) {
        this.northwest = northwest;
        this.southeast = southeast;
    }

    public Location getNorthwest() {
        return this.northwest;
    }

    public Location getSoutheast() {
        return this.southeast;
    }

    public double getMinLatitude() {
        return this.southeast.getLatitude();
    }

    public double getMaxLatitude() {
        return this.northwest.getLatitude();
    }

    public double getMinLongitude() {
        return this.northwest.getLongitude();
    }

    public double getMaxLongitude() {
        return this.southeast.getLongitude();
    }

    public boolean contains(Location location) {
        return location.getLatitude() >= getMinLatitude() &&
                location.getLatitude() <= getMaxLatitude() &&
                location.getLongitude() >= getMinLongitude() &&
                location.getLongitude() <= getMaxLongitude();
    }

    public static UniversityBounds ciudadUniversitaria() {
        Location nw = new Location(19.3350, -99.1950);
        Location se = new Location(19.3100, -99.1700);
        return new UniversityBounds(nw, se);
    }
}
