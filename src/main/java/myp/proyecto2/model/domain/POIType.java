package myp.proyecto2.model.domain;

/** 
 * Representa los distintos tipos en los que pueden ser clasificados los puntos
 * de interes.
 */
public enum POIType {

    /** Facultad. */
    FACULTY,

    /** Escuela. */
    SCHOOL,

    /** Centro.*/
    CENTER,

    /** Lugar recreativo. */
    RECREATION,

    /** Parada de autobus. */
    BUS_STOP, 

    /** Lugar donde venden comida. */
    FOOD,

    /** Estacionamiento. */
    PARKING,

    /** Otro tipo. */
    OTHER;

    /**
     * Regresa el elemento de la enumeracion asociado a una cadena.
     * @param type cadena que se quiere verificar.
     * @returnel elemento de la enumeracion asociado a una cadena.
     * @throws IllegalArgumentException si la cadena no esta asociada a ningun elemento de la enumeracion.
     */
    public static POIType getType(String type)throws IllegalArgumentException{
        return switch (type.toLowerCase()) { //Switch expresion
            case "faculty" -> FACULTY;
            case "school" -> SCHOOL;
            case "center" -> CENTER;
            case "recreation" -> RECREATION;
            case "bus stop" -> BUS_STOP;
            case "food" -> FOOD;
            case "parking" -> PARKING;
            case "other" -> OTHER;
            default -> throw new IllegalArgumentException("Type " + type + " is not a valid type");
        };
    }

    public String getDisplayName() {
        return switch (this) {
            case FACULTY -> "Faculty";
            case SCHOOL -> "School";
            case CENTER -> "Center";
            case RECREATION -> "Recreation";
            case BUS_STOP -> "Bus stop";
            case FOOD -> "Food";
            case PARKING -> "Parking";
            case OTHER -> "Other";
            default -> throw new IllegalArgumentException("The type provided is not valid.");  
        };
    }
}