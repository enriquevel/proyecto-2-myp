package main.java.myp.proyecto2.model.catalog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import myp.proyecto1.model.catalog.ComponentFactory;
import myp.proyecto2.model.catalog.POICatalog;

/**
 * Clase que se encarga de construir un HashMap con los puntos de interes (POI)
 * que lee de un archivo.
 */
public class POICatalogLoader{

    /** La ruta del archivo. */
    private final String FilePathPOI;


    public POICatalogLoader(String filePath)throws NullPointerException{
        if(filePath == null)throw NullPointerException("File path to read from cannot be null. From POICatalogLoader.");

        this.FilePathPOI = filePath;
    }

    /**
     * Regresa un catalogo con la informacion (puntos de interes) que contenia
     * el archivo especificado en la ruta {@link #FilePathPOI}.
     * @return un catalogo de los puntos de interes almacenados en un archivo.
     */
    public POICatalog getCatalog(){
        return this.load();
    }


    /**
     * Metodo auxiliar que lee un archivo utilizando la ruta especificada
     * en el atributo {@link #FilePathPOI} y regresa un catalogo de POI con la informacion del archivo.
     * @return un catalogo de POI con la informacion del archivo.
     */
    private POICatalog load(){
        POICatalog catalog = new POICatalog(FilePathPOI)
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
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
                    if (values[i].isEmpty())
                        continue;

                    row.put(cols[i], values[i].trim());
                }

                //for (Map.Entry<String, String> entry : row.entrySet())
                //    System.out.println(entry.getKey() + ": " + entry.getValue());

                String id = UUID.randomUUID().toString();
                String category = row.get("category");
                catalog.addComponent(this.factory.createComponent(id, category, row));
            }
        }
        return catalog;
    }

}