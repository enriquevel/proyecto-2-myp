package myp.proyecto2.model.basicelements;

public enum ReportType {
    CRIME_INCIDENT,
    ACCIDENT,
    CONSTRUCTION,
    TRAFFIC_JAM,
    STREETLIGHT_OUT,
    NATURAL_DEBRIS,
    FLOODING,
    LOST_ITEM;

    public int getDefaultPenalty() {
        return 0;
    }

    public String getDisplayName() {

    }

    public int getSeverity() {
        return 0;
    }
}