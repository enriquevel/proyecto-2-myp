package myp.proyecto2.model.domain;

public enum POIType {
    
    FACULTY,
    SCHOOL,
    CENTER,
    RECREATION,
    BUS_STOP, 
    FOOD,
    PARKING,
    OTHER;

    public static POIType getType(String type)throws IllegalArgumentException{
    return switch (type) { //Switch expresion
        case "FACULTY" -> FACULTY;
        case "SCHOOL" -> SCHOOL;
        case "CENTER" -> CENTER;
        case "RECREATION" -> RECREATION;
        case "BUS_STOP" -> BUS_STOP;
        case "FOOD" -> FOOD;
        case "PARKING" -> PARKING;
        case "OTHER" -> OTHER;
        default -> throw new IllegalArgumentException("Type " + type + " is not a valid type");
    };
}

    public String getDisplayName() {
        return null;
    }
}