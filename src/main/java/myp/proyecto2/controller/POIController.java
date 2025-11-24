package myp.proyecto2.controller;

import java.util.List;
import myp.proyecto2.model.catalog.POICatalog;
import myp.proyecto2.model.domain.PointOfInterest;

/**
 * Controlador para gestionar puntos de interes (POI) que los usuarios deseen registrar
 * y consultar en el sistema de navegacion.
 */
public class POIController {

    /** El catalogo con los puntos de interes. */
    private final POICatalog poiCatalog;

    /**
     * Construye un nuevo controlador de POIs con el catalogo especificado.
     *
     * @param poiCatalog el catalogo de puntos de interes
     */
    public POIController(POICatalog poiCatalog) {
        this.poiCatalog = poiCatalog;
    }

    /**
     * Agrega un nuevo punto de interes al sistema.
     *
     * @param poi el punto de interes a agregar al sistema
     */
    public void addPOI(PointOfInterest poi) {
        this.poiCatalog.save(poi);
    }

    /**
     * Actualiza la informacion de un punto de interes existente.
     *
     * @param poi el punto de interes con la informacion actualizada
     */
    public void updatePOI(PointOfInterest poi) {
        this.poiCatalog.update(poi);
    }

    /**
     * Elimina un punto de interes del sistema usando su identificador.
     *
     * @param poiId el identificador unico del punto de interes a eliminar
     */
    public void deletePOI(String poiId) {
        this.poiCatalog.delete(this.poiCatalog.findById(poiId));
    }

    /**
     * Obtiene todos los puntos de interes almacenados en el sistema.
     *
     * @return una lista con todos los puntos de interes registrados
     */
    public List<PointOfInterest> getAllPOIs() {
        return this.poiCatalog.findAll();
    }

    /**
     * Obtiene un punto de interes especifico mediante su identificador unico.
     *
     * @param id el identificador unico del punto de interes a buscar
     * @return el punto de interes correspondiente al ID proporcionado.
     */
    public PointOfInterest getPOIById(String id) {
        return this.poiCatalog.findById(id);
    }

    /**
     * Busca un punto de interes por su nombre.
     *
     * @param name el nombre del punto de interes a buscar
     * @return el punto de interes con el nombre especificado.
     */
    public PointOfInterest findByName(String name) {
        return this.poiCatalog.findByName(name);
    }
}
