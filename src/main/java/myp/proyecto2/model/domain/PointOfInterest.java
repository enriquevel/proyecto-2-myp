package myp.proyecto2.model.domain;

public class PointOfInterest {

    private String id;
    private String name;
    private String description;
    private Location location;
    private POIType type;


    public PointOfInterest(String id, String name, String description, Location location, POIType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Location getLocation() {
        return this.location;
    }

    public POIType getType() {
        return this.type;
    }
}