package myp.proyecto2.model.domain;

/**
 * Representa los distintos tipos en los que pueden ser clasificados los reportes.
 */
public enum ReportType {

    /** Crimen. */
    CRIME_INCIDENT,

    /** Accidente. */
    ACCIDENT,

    /** Aviso de construccion. */
    CONSTRUCTION,

    /** Atasco en el trafico. */
    TRAFFIC_JAM,

    /** Falla en el alumbrado publico. */
    STREETLIGHT_OUT,

    /** Escombros naturales. */
    NATURAL_DEBRIS,

    /** Inundacion. */
    FLOODING,

    /** Articulo perdido. */
    LOST_ITEM,

    /** Otro */
    OTHER;

    /**
	 * Define como se mostrara el valor de cada enum cuando se convierta en texto.
     *
	 * @return el valor de cada enum cuando se convierta en texto.
	*/
    public String getDisplayName() {
        return switch (this) {
            case CRIME_INCIDENT -> "Crime incident";
            case ACCIDENT -> "Accident";
            case CONSTRUCTION -> "Construction";
            case TRAFFIC_JAM -> "Traffic jam";
            case STREETLIGHT_OUT -> "Streetlight out";
            case NATURAL_DEBRIS -> "Natural debris";
            case FLOODING -> "Flooding";
            case LOST_ITEM -> "Lost item";
            case OTHER -> "Other";
        };
    }

    /**
     * Regrese el elemento de la enumeracion asociado a una cadena.
     *
     * @param type cadena que se quiere verificar.
     * @return el elemento de la enumeracion asociado a una cadena.
     * @throws NullPointerException si el tipo a buscar es null.
     * @throws IllegalArgumentException si la cadena no esta asociada a ningun elemento de la enumeracion.
     */
    public static ReportType getType(String type) throws IllegalArgumentException {
        if (type == null)
            throw new NullPointerException("Type cannot be null");

        return switch (type.toLowerCase()) {
            case "crime incident" -> CRIME_INCIDENT;
            case "accident" -> ACCIDENT;
            case "construction" -> CONSTRUCTION;
            case "traffic jam" -> TRAFFIC_JAM;
            case "streetlight out" -> STREETLIGHT_OUT;
            case "natural debris" -> NATURAL_DEBRIS;
            case "flooding" -> FLOODING;
            case "lost item" -> LOST_ITEM;
            case "other" -> OTHER;
            default -> throw new IllegalArgumentException("Report type " + type + " is not a valid report type");
        };
    }

    /**
     * Regresa la penalizacion (un entero) asociada a un elemento de la enumeracion.
     * @return la penalizacion (un entero) asociado a un elemento de la enumeracion.
     */
    public int getDefaultPenalty() {
        return switch (this) {
            case CRIME_INCIDENT -> 300;
            case ACCIDENT -> 250;
            case CONSTRUCTION -> 200;
            case TRAFFIC_JAM -> 300;
            case STREETLIGHT_OUT -> 100;
            case NATURAL_DEBRIS -> 150;
            case FLOODING -> 200;
            case LOST_ITEM -> 1;
            case OTHER -> 10;
        };
    }
}