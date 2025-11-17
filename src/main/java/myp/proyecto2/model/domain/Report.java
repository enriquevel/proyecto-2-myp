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

    /** Indica si el reporte esta activo. */
    private boolean active;
    
    /**
     * Constructor principal de la clase {@link Report}.
     *
     * @param id identificador unico del reporte.
     * @param type tipo del reporte.
     * @param location localizacion del reporte.
     * @param description descripcion del reporte.
     */
    public Report(String id, ReportType type, Location location, String description) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.description = description;
        this.active = true;
    }

    /**
     * Regresa el identificador del reporte.
     *
     * @return el identificador del reporte.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Regresa el tipo del reporte.
     *
     * @return el tipo del reporte.
     */
    public ReportType getType() {
        return this.type;
    }

    /**
     * Regresa la localizacion del reporte.
     *
     * @return la localizacion del reporte.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Regresa la descripcion del reporte.
     *
     * @return la descripcion del reporte.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Nos dice si el reporte esta activo.
     *
     * @return <code>true</code> si el reporte esta activo, <code>false</code> en otro caso.
     */
    public boolean isActive() {
        return this.active;
    }

    public void resolve() {
        this.active = false;
    }

    /**
     * Regresa una representacion en cadena del reporte, lista para ser
     * escrita en un archivo.
     *
     * @return una representacion en cadena del reporte.
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

    }
}