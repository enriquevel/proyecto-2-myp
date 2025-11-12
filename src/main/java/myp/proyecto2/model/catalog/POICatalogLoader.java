package main.java.myp.proyecto2.model.catalog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import myp.proyecto1.model.catalog.ComponentFactory;
import myp.proyecto2.model.catalog.POICatalog;
import myp.proyecto2.model.domain.PointOfInterest;

/**
 * Clase que se encarga de construir una instancia de {@link POICatalog} a partir 
 * de un archivo.
 */
public class POICatalogLoader{

    /** La ruta del archivo. */
    private final String filePathPOI;

    /**
     * Constructor principal de la clase {@link POICatalogLoader}.
     * @param filePath ruta del archivo.
     * @throws NullPointerException si la ruta proporcionada es <code>null</code>.
     */
    public POICatalogLoader(String filePath)throws NullPointerException{
        if(filePath == null)throw NullPointerException("File path to read from cannot be null. From POICatalogLoader.");

        this.filePathPOI = filePath;
    }

    /**
     * Regresa un catalogo con la informacion (puntos de interes) que contenia
     * el archivo especificado en la ruta {@link #FilePathPOI}.
     * @return un catalogo de los puntos de interes almacenados en un archivo.
     */
    public POICatalog getCatalog(){
        POICatalog catalog = new POICatalog(this.filePathPOI);
        return this.load(catalog);
    }


    /**
     * Metodo auxiliar que lee un archivo utilizando la ruta especificada.
     * en el atributo {@link #FilePathPOI} y regresa un catalogo de POI con la informacion del archivo.
     * @return un catalogo de POI con la informacion del archivo.
     */
    private POICatalog load(POICatalog catalog){
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePathPOI))) {
            String header = br.readLine();
            if (header == null)
                return catalog;
            String[] cols = header.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] values = line.split(",", 0);
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < cols.length && i < values.length; i++) {
                    /**if (values[i].isEmpty())
                        continue;
                    */
                    row.put(cols[i], values[i].trim());
                }

                String id = UUID.randomUUID().toString();
                String type = row.get("type");
                catalog.addPOI(new PointOfInterest(id, row.get(name),row.get("description"), row.get("location"), type ));
            }
        }
        return catalog;
    }

}