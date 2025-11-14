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
	 * Define como se mostrara el valor de cada enum cuando se convierta en texto.
	 * @return el valor de cada enum cuando se convierta en texto.
	*/
	@Override
	public String toString() {
		return switch (this) {
            case FACULTY -> "FACULTY";
            case SCHOOL -> "SCHOOL";
            case CENTER  -> "CENTER";
            case RECREATION  -> "RECREATION";
            case BUS_STOP -> "BUS_STOP"; 
            case FOOD -> "FOOD";
            case PARKING -> "PARKING"; 
            case OTHER -> "OTHER";
		};
	}

    /**
     * Regresa el elemento de la enumeracion asociado a una cadena.
     * @param type cadena que se quiere verificar.
     * @returnel elemento de la enumeracion asociado a una cadena.
     * @throws IllegalArgumentException si la cadena no esta asociada a ningun elemento de la enumeracion.
     */
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