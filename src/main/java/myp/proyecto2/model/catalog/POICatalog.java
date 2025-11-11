package myp.proyecto2.model.catalog;

import java.util.List;
import java.util.Map;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.POIType;

public class POICatalog {

    private String filePath;
    private Map<String, PointOfInterest> pois;


    public POICatalog(String filePath) {
        if(filePath == null)    
            throw new NullPointerException("The file path cannot be null.");
        
        this.filePath = filePath;
    }

    public void load() {

    }

    public List<PointOfInterest> findAll() {

    }

    public PointOfInterest findById(String id) {
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");
    
    }

    public List<PointOfInterest> findByName(String name) {
        if(name == null) 
            throw new NullPointerException("The POI's name cannot be null.");
    }

    public List<PointOfInterest> findByType(POIType type) {

    }

    public List<PointOfInterest> search(String query) {

    }

    public PointOfInterest save(PointOfInterest poi) {

    }

    public void delete(String id) {
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");
    }

    private String readFile() {

    }

    private void writeFile(String line) {}
        if(line == null) 
            throw new NullPointerException("The line to be written cannot be null.");
    }
}