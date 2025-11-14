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
    public String toString() {
        return switch (this){
            case CRIME_INCIDENT -> "CRIME INCIDENT";
            case ACCIDENT -> "ACCIDENT";
            case CONSTRUCTION -> "CONSTRUCTION";
            case TRAFFIC_JAM -> "TRAFFIC JAM";
            case STREETLIGHT_OUT -> "STREETLIGHT OUT";
            case NATURAL_DEBRIS -> "NATURAL DEBRIS";
            case FLOODING -> "FLOODING";
            case LOST_ITEM -> "LOST ITEM";
            case OTHER -> "OTHER";
        };
    }

    /**
     * Regrese el elemento de la enumeracion asociado a una cadena.
     *
     * @param type cadena que se quiere verificar.
     * @return el elemento de la enumeracion asociado a una cadena.
     * @throws IllegalArgumentException si la cadena no esta asociada a ningun elemento de la enumeracion.
     */
    public static ReportType getType(String type) throws IllegalArgumentException {
        return switch (type) {
            case "CRIME_INCIDENT" -> CRIME_INCIDENT;
            case "ACCIDENT" -> ACCIDENT;
            case "CONSTRUCTION" -> CONSTRUCTION;
            case "TRAFFIC JAM" -> TRAFFIC_JAM;
            case "STREETLIGHT OUT" -> STREETLIGHT_OUT;
            case "NATURAL DEBRIS" -> NATURAL_DEBRIS;
            case "FLOODING" -> FLOODING;
            case "LOST ITEM" -> LOST_ITEM;
            case "OTHER" -> OTHER;
            default -> throw new IllegalArgumentException("Report type " + type + " is not a valid report type");
        };
    }

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

    public int getSeverity() {
        return switch (this) {
            case CRIME_INCIDENT -> 5;
            case ACCIDENT -> 5;
            case CONSTRUCTION -> 3;
            case TRAFFIC_JAM -> 4;
            case STREETLIGHT_OUT -> 2;
            case NATURAL_DEBRIS -> 3;
            case FLOODING -> 4;
            case LOST_ITEM -> 1;
            case OTHER -> 1;
        };
    }
}