package myp.proyecto2.model.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.POIType;

/**
 * Esta clase se utiliza para dar una representacion interna al conjunto de todos
 * los puntos de interes.
 */
public class POICatalog implements Catalog<PointOfInterest, POIType>{

    /** Lector y escritor de archivos para reportes. */
    private final CSVPOIReaderWriter readerWriter;

    /** Mapa de puntos de interes por ID. */
    private Map<String, PointOfInterest> poisByID;

    /** Mapa de puntos de interes por tipo. */
    private Map<POIType, List<PointOfInterest>> poisByType;

    /**
     * Constructor principal de la clase {@link POICatalog}. Inicializa los 
     * hashMaps y define la ruta del archivo que almacena los puntos de interes (POI).
     */
    public POICatalog(String filePath) {
        if (filePath == null)
            throw new NullPointerException("The file path cannot be null.");

        this.readerWriter = new CSVPOIReaderWriter(filePath);
        this.poisByID = new HashMap<>();
        this.poisByType = new EnumMap<>(POIType.class);

        initialize();
    }

    private void initialize() {
        try {
            List<PointOfInterest> loaded = this.readerWriter.readAll();

            for (POIType type : POIType.values())
                this.poisByType.put(type, new ArrayList<>());

            for (PointOfInterest poi : loaded) {
                this.poisByID.put(poi.getId(), poi);
                this.poisByType.get(poi.getType()).add(poi);
            }
            System.out.println("Loaded " + this.poisByID.size() + " POIs from CSV");
        } catch (IOException ioe) {
            System.err.println("Failed to load POIs: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * Agrega un punto de interes al catalogo y tambien lo guarda en la base de datos.
     *
     * @param poi el punto de interes que se quiere guardar.
     * @throws NullPointerException si el punto de interes que se quiere guardar es <code>null</code>.
     * @throws RuntimeException si ocurrio un error al guardar el punto de interes.
     */
    @Override
    public void save(PointOfInterest poi) {
        if (poi == null)
            throw new NullPointerException("Cannot save a null point of interest.");

        add(poi);

        try {
            this.readerWriter.add(poi);
        } catch (IOException ioe) {
            delete(poi);
            throw new RuntimeException("Failed to save point of interest.", ioe);
        }
    }

    /**
     * Agrega un punto de interes (POI), instancia de {@link PointOfInterest}, al catalogo.
     *
     * @param poi punto de interes que se desea agregar.
     * @throws NullPointerException si el punto de interes que se quiere agregar es <code>null</code>.
     */
    public void add(PointOfInterest poi) {
        if (poi == null)
            throw new NullPointerException("Cannot add a null point of interest.");
        //Agrega al punto de interes a una hashMap donde su id funge como llave.
        PointOfInterest old = this.poisByID.put(poi.getId(), poi);

        /*Como put regresa <code>null</code> si el objeto agregado no estaba previamente en el hashmap,
         * verificamos el caso contrario.
         */
        if (old != null)
            this.poisByType.get(old.getType()).remove(old);
        //Finalmente agregamos el nuevo punto de interes a la lista del segundo hash map.
        this.poisByType.get(poi.getType()).add(poi);
    }

    /**
     * Elimina un punto de interes del catalogo y de la base de datos.
     *
     * @param poi el punto de interes que se quiere eliminar.
     * @return <code>true</code> si el punto de interes estaba en el catalogo y fue eliminado, <code>false</code>
     *          en otro caso.
     * @throws NullPointerException si el punto de interes que se quiere eliminar es <code>null</code>.
     */
    @Override
    public boolean delete(PointOfInterest poi) {
        if (poi == null)
            throw new NullPointerException("Cannot delete a null point of interest.");

        PointOfInterest removed = this.poisByID.remove(poi.getId());
        //Verificamos si el reporte realmente estaba en el catalogo.
        if (removed != null)
            this.poisByType.get(removed.getType()).remove(removed);//Lo removemos de la lista del segundo hashmap.

        try {
            this.readerWriter.delete(poi);
        } catch (IOException ioe) {
            this.poisByID.put(poi.getId(), poi);
            this.poisByType.get(poi.getType()).add(poi);
            throw new RuntimeException("Failed to delete point of interest", ioe);
        }
        return removed != null;
    }

    /**
     * Regresa una lista de todos los puntos de interes.
     *
     * @return una lista de todos los puntos de interes.
     */
    @Override
    public List<PointOfInterest> findAll() {
        return new ArrayList<>(this.poisByID.values());
    }

    /**
     * Regresa un punto de interes del catalogo, en base a un identificador.
     *
     * @param id del punto de interes que se desea encontrar.
     * @return el punto de interes con el identificador dado.<code>null</code> si no existe un 
     * punto de interes con dicho identificador.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    @Override
    public PointOfInterest findById(String id) throws NullPointerException {
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");
            
        return this.poisByID.get(id);
    }

    /**
     * Regresa una lista de todos los puntos de interes de un mismo tipo.
     *
     * @param type de todos los puntos de interes.
     * @return una lista de todos los puntos de interes de un mismo tipo.
     * <code>null</code> si no existen puntos de interes de ese tipo.
     * @throws NullPointerException si el tipo dado es <code>null</code>.
     */
    @Override
    public List<PointOfInterest> findByType(POIType type) throws NullPointerException {
        return this.poisByType.get(type);
    }

    /**
     * Regresa un punto de interes del catalogo, buscandolo por su nombre.
     * @param name nombre del punto de interes.
     * @return el punto de interes con dicho nombre.<code>null</code> si no lo encuentra.
     * @throws NullPointerException si el nombre dado es <code>null</code>.
     */
    public PointOfInterest findByName(String name) throws NullPointerException {
        if (name == null) 
            throw new NullPointerException("The POI's name cannot be null.");

        for (PointOfInterest poi : this.findAll())
            if(poi.getName().equals(name))return poi; //Podriamos normalizar ambos nombres en las busquedas.

        return null;
    }
}