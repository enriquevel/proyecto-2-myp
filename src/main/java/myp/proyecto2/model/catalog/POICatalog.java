package myp.proyecto2.model.catalog;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.POIType;

/**
 * Esta clase se utiliza para dar una representacion interna a varios puntos de interes.
 */
public class POICatalog {

    /** Mapa de puntos de interes por ID. */
    private Map<String, PointOfInterest> poisByID;

    /** Mapa de puntos de interes por tipo. */
    private Map<POIType, List<PointOfInterest>> poisByType;

    /**
     * Constructor principal de la clase {@link POICatalog}. Solamente inicializa los 
     * atributos.
     */
    public POICatalog(){
        this.poisByID = new HashMap<>();
        this.poisByType = new EnumMap<>(POIType.class);
    }

    /**
     * Agrega un punto de interes (POI), instancia de {@link PointOfInterest}, al catalogo.
     * @param poi punto de interes que se desea agregar.
     * @throws NullPointerException si el punto de interes que se quiere agregar es <code>null</code>.
     */
    public void addPOI(PointOfInterest poi)throws NullPointerException{
        if (poi == null)
            throw new NullPointerException("The point of interest you want to add cannot be null.");
        //Agrega al punto de interes a una hashMap donde su id funge como llave.
        POICatalog old = this.poisByID.put(poi.getId(), poi);

        /*Como put regresa <code>null</code> si el objeto agregado no estaba previamente en el hashmap,
         * verificamos el caso contrario.
         */
        if (old !=null)
            this.poisByType.get(old.getType).remove(old);
        //Finalmente agregamos el nuevo punto de interes a la lista del segundo hash map.
        this.poisByType.get(poi.getType).add(old);
    }   

    /**
     * Permite eliminar a un punto de interes del catalogo utilizando
     * su identificador.
     * @param id del punto de interes que se desea eliminar.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    public void deletePOI(String id)throws NullPointerException{
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");

        PointOfInterest removed = this.poisByID.remove(id);
        //Verificamos si el punto de interes realmente estaba en el catalogo.
        if (removed!= null)
            this.poisByType.get(removed.getType()).remove(removed); //Lo removemos de la lista del segundo hashmap.
    }

    /**
     * Regresa una lista de todos los puntos de interes.
     * @return una lista de todos los puntos de interes.
     */
    public List<PointOfInterest> findAll() {
        return new ArrayList<>(this.poisByID.values());
    }

    /**
     * Regresa un punto de interes del catalogo, en base a un identificador.
     * @param id del punto de interes que se desea encontrar.
     * @return el punto de interes con el identificador dado.<code>null</code> si no existe un 
     * punto de interes con dicho identificador.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    public PointOfInterest findById(String id)throws NullPointerException {
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");
        return this.poisByID.get(id);
    }

    /**
     * Regresa un punto de interes del catalogo, buscandolo por su nombre.
     * @param name del punto de interes.
     * @return el punto de interes con dicho nombre.<code>null</code>si no lo encuentra.
     * @throws NullPointerException si el nombre dado es <code>null</code>.
     */
    public List<PointOfInterest> findByName(String name)throws NullPointerException{
        if(name == null) 
            throw new NullPointerException("The POI's name cannot be null.");
        for(PointOfInterest poi : this.findAll())
            if(poi.getName().equals(name))return poi;

        return null;
    }

    /**
     * Regresa una lista de todos los puntos de interes de un mismo tipo.
     * @param type de todos los puntos de interes.
     * @return una lista de todos los puntos de interes de un mismo tipo.
     * <code>null</code> si no existen puntos de interes de ese tipo.
     * @throws NullPointerException si el tipo dado es <code>null</code>.
     */
    public List<PointOfInterest> findByType(POIType type)throws NullPointerException{
        return this.poisByType.get(type);
    }

    
    public List<PointOfInterest> search(String query)throws NullPointerException{

    }

    public PointOfInterest save(PointOfInterest poi)throws NullPointerException{

    }


}