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
    STREET_LIGHT_OUT,

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
	 * @return el valor de cada enum cuando se convierta en texto.
	*/
    public String toString(){
        return switch (this){
            case CRIME_INCIDENT -> "CRIME INCIDENT";
            case ACCIDENT -> "ACCIDENT";
            case CONSTRUCTION -> "CONSTRUCTION";
            case TRAFFIC_JAM -> "TRAFFIC JAM";
            case STREET_LIGHT_OUT -> "STREET LIGHT OUT";
            case NATURAL_DEBRIS -> "NATURAL DEBRIS";
            case FLOODING -> "FLOODING";
            case LOST_ITEM -> "LOST ITEM";
            case OTHER -> "OTHER";
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
            case "TRAFFIC JAM" -> TRAFFIC_JAM;
            case "STREET LIGHT OUT" -> STREET_LIGHT_OUT;
            case "NATURAL DEBRIS" -> NATURAL_DEBRIS;
            case "FLOODING" -> FLOODING;
            case "LOST ITEM" -> LOST_ITEM;
            case "OTHER" -> OTHER;
            default -> throw new IllegalArgumentException("Report type " + type + " is not a valid report type");
        };
    } 

    public int getDefaultPenalty() {
        return 0;
    }

    public int getSeverity() {
        return 0;
    }
}