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
    LOST_ITEM;
    
    /** 
	 * Define como se mostrara el valor de cada enum cuando se convierta en texto.
	 * @return el valor de cada enum cuando se convierta en texto.
	*/
    public String toString(){
        return switch (this){
            case CRIME_INCIDENT -> "CRIME_INCIDENT";
            case ACCIDENT -> "ACCIDENT";
            case CONSTRUCTION -> "CONSTRUCTION";
            case TRAFFIC_JAM -> "TRAFFIC_JAM";
            case STREETLIGHT_OUT -> "STREETLIGHT_OUT";
            case NATURAL_DEBRIS -> "NATURAL_DEBRIS";
            case FLOODING -> "FLOODING";
            case LOST_ITEM -> "LOST_ITEM";
        };
    }

    /**
     * Regrese el elemento de la enumeracion asociado a una cadena.
     * @param type cadena que se quiere verificar.
     * @return el elemento de la enumeracion asociado a una cadena.
     * @throws IllegalArgumentException si la cadena no esta asociada a ningun elemento de la enumeracion.
     */
    public static ReportType getType(String type)throws IllegalArgumentException{
        return switch (type) {
            case "CRIME_INCIDENT" -> CRIME_INCIDENT;
            case "ACCIDENT" -> ACCIDENT;
            case "CONSTRUCTION" -> CONSTRUCTION;
            case "TRAFFIC_JAM" -> TRAFFIC_JAM;
            case "STREETLIGHT_OUT" -> STREETLIGHT_OUT;
            case "NATURAL_DEBRIS" -> NATURAL_DEBRIS;
            case "FLOODING" -> FLOODING;
            case "LOST_ITEM" -> LOST_ITEM;
            default -> throw new IllegalArgumentException("Type " + type + " is not a valid type");
        };
    }

    public int getDefaultPenalty() {
        return 0;
    }

    public String getDisplayName() {
        return null;
    }

    public int getSeverity() {
        return 0;
    }
}