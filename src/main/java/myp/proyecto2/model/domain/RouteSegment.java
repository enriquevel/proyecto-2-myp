package myp.proyecto2.model.domain;

public class RouteSegment {

    private String instruction;
    private double distanceInMeters;
    private double durationInSeconds;
    private Location startPoint;
    private Location endPoint;
    private TransportMode mode;

    public RouteSegment(String instruction, double distanceInMeters, double durationInSeconds,
                        Location startPoint, Location endPoint, TransportMode mode) {
        if (instruction == null)
            throw new NullPointerException("Instruction cannot be null");

        if (distanceInMeters < 0)
            throw new IllegalArgumentException("Distance cannot be negative");

        if (durationInSeconds < 0)
            throw new IllegalArgumentException("Duration cannot be negative");

        if (startPoint == null)
            throw new NullPointerException("Start point cannot be null");

        if (endPoint == null)
            throw new NullPointerException("End point cannot be null");
            
        if (mode == null)
            throw new NullPointerException("Transport mode cannot be null");

        this.instruction = instruction;
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.mode = mode;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public double getDistanceInMeters() {
        return this.distanceInMeters;
    }

    public double getDurationInSeconds() {
        return this.durationInSeconds;
    }

    public Location getStartPoint() {
        return this.startPoint;
    }

    public Location getEndPoint() {
        return this.endPoint;
    }

    public TransportMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return null;
    }
}