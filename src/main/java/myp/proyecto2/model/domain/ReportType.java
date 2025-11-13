package myp.proyecto2.model.domain;

public enum ReportType {
    CRIME_INCIDENT,
    ACCIDENT,
    CONSTRUCTION,
    TRAFFIC_JAM,
    STREET_LIGHT_OUT,
    NATURAL_DEBRIS,
    FLOODING,
    LOST_ITEM,
    OTHER;

    public static ReportType getType(String type) {
        return switch (type) { 
            case "CRIME INCIDENT" -> CRIME_INCIDENT;
            case "ACCIDENT" -> ACCIDENT;
            case "CONSTRUCTION" -> CONSTRUCTION;
            case "TRAFFIC JAM" -> TRAFFIC_JAM;
            case "STREET LIGHT OUT" -> STREET_LIGHT_OUT;
            case "NATURAL DEBRIS" -> NATURAL_DEBRIS;
            case "FLOODING" -> FLOODING;
            case "LOST ITEM" -> LOST_ITEM;
            case "OTHER" -> OTHER;
            default -> throw new IllegalArgumentException("Report type " + type + " is not a valid report type");
        };
    } 

    public String getDisplayName() {
        return switch(this) {
            case CRIME_INCIDENT -> "Crime Incident";
            case ACCIDENT -> "Accident";
            case CONSTRUCTION -> "COnstruction";
            case TRAFFIC_JAM -> "Traffic jam";
            case STREET_LIGHT_OUT -> "Street light out";
            case NATURAL_DEBRIS -> "Natural debris";
            case FLOODING -> "Flooding";
            case LOST_ITEM -> "Lost item";
            case OTHER -> "Other";
            default -> throw new IllegalArgumentException("The report type provided is not valid.");  
        };
    }

    public int getDefaultPenalty() {
        return 0;
    }

    public int getSeverity() {
        return 0;
    }
}