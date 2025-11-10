package myp.proyecto2.model.domain;

public class Report {

    private String id;
    private ReportType type;
    private Location location;
    private String description;

    public Report(String id, ReportType type, Location location, String description) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public ReportType getType() {
        return this.type;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isActive() {
        return false;
    }

    public void resolve() {}

    @Override
    public String toString() {
        return null;
    }
}