package myp.proyecto2.model.domain;

/**
 * Clase que define los atributos y comportamiento de los reportes.
 */
public class Report {

    /** Identificador unico del reporte. */
    private String id;

    /** Tipo del reporte. */
    private ReportType type;

    /** Localizacion del reporte. */
    private Location location;

    /** Descripcion del reporte. */
    private String description;
    
    /**
     * Constructor principal de la clase {@link Report}.
     * @param id identificador unico del reporte.
     * @param type tipo del reporte.
     * @param location localizacion del reporte.
     * @param description descripcion del reporte.
     * @throws NullPointerExcepetion si algun parametro es null.
     */
    public Report(String id, ReportType type, Location location, String description) {
        if(id == null)
            throw new NullPointerException("ID cannot be null.");
        if(type == null)
            throw new NullPointerException("Report type cannot be null.");
        if(location == null)
            throw new NullPointerException("Report's location cannot be null.");
        if(description == null)
            throw new NullPointerException("Report's description cannot be null.");
        
        this.id = id;
        this.type = type;
        this.location = location;
        this.description = description;
    }

    /**
     * Regresa el identificador del reporte.
     * @return el identificador del reporte.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Regresa el tipo del reporte.
     * @return el tipo del reporte.
     */
    public ReportType getType() {
        return this.type;
    }

    /**
     * Regresa la localizacion del reporte.
     * @return la localizacion del reporte.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Regresa la descripcion del reporte.
     * @return la descripcion del reporte.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Nos dice si el reporte esta activo.
     * @return 
     */
    public boolean isActive() {
        return false;
    }

    public void resolve() {}

    /**
     * Regresa una representacion en cadena del reporte, lista para ser
     * escrita en un archivo.
     * @return una representacion en cadena del reporte, lista para ser
     * escrita en un archivo.
     */
    public String getFileFormat(){
        return this.type.toString()+","
        + this.location.getLatitude() + ","
        + this.location.getLongitude() + ","
        + this.location.getAddress() + ","
        + this.description;
    }

    @Override
    public String toString() {
        return null;
    }
}