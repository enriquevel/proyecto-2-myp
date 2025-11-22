package myp.proyecto2.model.domain;

/**
 * Clase que define los atributos y comportamiento de los reportes.
 */
public class Report {

    /** Identificador unico del reporte. */
    private final String id;

    /** Tipo del reporte. */
    private final ReportType type;

    /** Localizacion del reporte. */
    private final Location location;

    /** Descripcion del reporte. */
    private final String description;

    /** Indica si el reporte esta activo. */
    private boolean active;

    /** Numero de votos a favor. */
    private int upvotes;

    /** Numero de votos en contra. */
    private int downvotes;
    
    /**
     * Constructor principal de la clase {@link Report}.
     *
     * @param id identificador unico del reporte.
     * @param type tipo del reporte.
     * @param location localizacion del reporte.
     * @param description descripcion del reporte.
     * @param upvotes el numero de votos a favor del reporte.
     * @param downvotes el numero de votos en contra del reporte.
     * @throws NullPointerException si alguno de los parametros es null.
     * @throws IllegalArgumentException si alguno de los votos es negativo.
     */
    public Report(String id, ReportType type, Location location, String description, int upvotes, int downvotes) {
        if (id == null)
            throw new NullPointerException("Report's ID cannot be null.");
        
        if (type == null)
            throw new NullPointerException("Report's type cannot be null.");
        
        if (location == null)
            throw new NullPointerException("Report's location cannot be null.");
        
        if (description == null)
            throw new NullPointerException("Report's description cannot be null.");

        if (upvotes < 0 || downvotes < 0)
            throw new IllegalArgumentException("Report's upvotes or downvotes cannot be negative.");

        this.id = id;
        this.type = type;
        this.location = location;
        this.description = description;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.active = true;
    }

    /**
     * Construye un reporte con cero votos.
     *
     * @param id identificador unico del reporte.
     * @param type tipo del reporte.
     * @param location localizacion del reporte.
     * @param description descripcion del reporte.
     * @throws NullPointerException si alguno de los parametros es null.
     */
    public Report(String id, ReportType type, Location location, String description) {
        this(id, type, location, description, 0, 0);
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
     * Devuelve el numero de votos a favor del reporte.
     *
     * @return el numero de votos a favor del reporte.
     */
    public int getUpvotes() {
        return this.upvotes;
    }

    /**
     * Devuelve el numero de votos en contra del reporte.
     *
     * @return el numero de votos en contra del reporte.
     */
    public int getDownvotes() {
        return this.downvotes;
    }

    /**
     * Nos dice si el reporte esta activo.
     *
     * @return <code>true</code> si el reporte esta activo, <code>false</code> en otro caso.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Marca un reporte como inactivo.
     */
    public void resolve() {
        this.active = false;
    }

    /**
     * Aumenta en uno el numero de votos a favor.
     */
    public void upvote() {
        this.upvotes++;
    }

    /**
     * Aumenta en uno el numero de votos en contra.
     */
    public void downvote() {
        this.downvotes++;
    }

    /**
     * Devuelve el valor neto de votos de este reporte.
     *
     * @return el valor neto de votos de este reporte.
     */
    public int getNetVotes() {
        return this.upvotes + this.downvotes;
    }

    /**
     * Regresa una representacion en cadena del reporte, lista para ser
     * escrita en un archivo.
     *
     * @return una representacion en cadena del reporte.
     */
    public String getFileFormat() {
        return this.id + ","
                + this.type.toString() + ","
                + this.location.getLatitude() + ","
                + this.location.getLongitude() + ","
                + this.location.getAddress() + ","
                + this.description + ","
                + this.upvotes + ","
                + this.downvotes;
    }
}