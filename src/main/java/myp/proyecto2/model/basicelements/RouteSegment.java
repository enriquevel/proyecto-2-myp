package myp.proyecto2.model.basicelements;

public class RouteSegment {

    private String instruction;
    private double distanceInMeters;
    private Location startPoint;
    private Location endPoint;

    public RouteSegment(String instruction, double distanceInMeters, Location startPoint, Location endPoint) {

        this.instruction = instruction;
        this.distanceInMeters = distanceInMeters;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public double getDistanceInMeters() {
        return this.distanceInMeters;
    }

    public Location getStartPoint() {
        return this.startPoint;
    }

    public Location getEndPoint() {
        return this.endPoint;
    }

    @Override
    public String toString() { 
        return null;
    }
}