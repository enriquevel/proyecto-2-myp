package myp.proyecto2.controller;

import java.util.List;
import myp.proyecto2.model.catalog.POICatalog;
import myp.proyecto2.model.domain.PointOfInterest;

public class POIController {

    private final POICatalog poiCatalog;

    public POIController(POICatalog poiCatalog) {
        this.poiCatalog = poiCatalog;
    }

    public void addPOI(PointOfInterest poi) {
        this.poiCatalog.save(poi);
    }

    public void updatePOI(PointOfInterest poi) {
        this.poiCatalog.update(poi);
    }

    public void deletePOI(String poiId) {
        this.poiCatalog.delete(this.poiCatalog.findById(poiId));
    }

    public List<PointOfInterest> getAllPOIs() {
        return this.poiCatalog.findAll();
    }

    public PointOfInterest getPOIById(String id) {
        return this.poiCatalog.findById(id);
    }

    public PointOfInterest findByName(String name) {
        return this.poiCatalog.findByName(name);
    }
}
