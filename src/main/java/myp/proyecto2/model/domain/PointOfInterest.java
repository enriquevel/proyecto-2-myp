package myp.proyecto2.model.domain;

/**
 * Clase que representa las caracteristicas y comportamiento
 * de los puntos de interes.
 */
public class PointOfInterest {

    /** Identificador unico. */
    private String id;

    /** Nombre del punto de interes. */
    private String name;

    /** Descripcion del punto de interes. */
    private String description;

    /** Localizacion del punto de interes. */
    private Location location;

    /** El tipo de; punto de interes. */
    private POIType type;

    /**
     * Constructor principal de la clase {@link PointOfInteres}. 
     * @param id identificador unico del punto.
     * @param name nombre del punto de interes.
     * @param description descripcion del punto de interes.
     * @param location localizacion del punto de interes.
     * @param type el tipo del punto de interes.
     * @throws NullPointerException si algun parametro es null.
     */
    public PointOfInterest(String id, String name, String description, Location location, POIType type) {
        if(id == null)
            throw new NullPointerException("ID cannot be null.");
        if(description == null)
            throw new NullPointerException("Point of interest's description cannot be null.");
        if(location == null)
            throw new NullPointerException("Location cannot be null.");
        if(type == null)
            throw new NullPointerException("POI's type cannot be null");
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
    }

    /**
     * Regresa el identificador del punto.
     * @return el identificador del punto.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Regresa el nombre del punto.
     * @return el nombre del punto.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Regresa la descripcion del punto de interes.
     * @return la descripcion del punto de interes.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Regresa la localizacion del punto de interes.
     * @return la localizacion del punto de interes.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Regresa el tipo del punto de interes.
     * @return el tipo del punto de interes.
     */
    public POIType getType() {
        return this.type;
    }

    /**
     * Regresa una representacion en cadena del punto, lista para ser
     * escrita en un archivo.
     * @return una representacion en cadena del punto, lista para ser
     * escrita en un archivo.
     */
    public String getFileFormat(){
        return this.type.toString()+","
        + this.name + ","
        + this.location.getLatitude() + ","
        + this.location.getLongitude() + ","
        + this.location.getAddress() + ","
        + this.description;
    }
}