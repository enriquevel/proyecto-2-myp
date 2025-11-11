package myp.proyecto2.model.catalog;

import java.util.List;
import java.util.Map;

public class ReportCatalog {

    private String filePath;
    private Map<String, Report> reports;

    public ReportCatalog(String filePath) {
        if(filePath == null)    
            throw new NullPointerException("The file path cannot be null.");
        
        this.filePath = filePath;
    }

    public void load() {}

    public List<Report> findAll() { return null; }

    public String findById(String id) { 
        return null;
    }

    public List<Report> findActive() { 
        return null;
    }

    public Report save(Report report) {
        return null;
    }

    public void delete(String id) {
    }

    private String readFile() {
        return null;
    }

    private void writeFile(String line) {
        if(line == null) 
            throw new NullPointerException("The line to write in the file cannot be null");
    }

}