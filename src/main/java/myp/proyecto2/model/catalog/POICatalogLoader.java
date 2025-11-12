package myp.proyecto2.model.catalog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.POIType;
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
        if(filePath == null)throw new NullPointerException("File path to read from cannot be null. From POICatalogLoader.");

        this.filePathPOI = filePath;
    }

    /**
     * Regresa un catalogo con la informacion (puntos de interes) que contenia
     * el archivo especificado en la ruta {@link #FilePathPOI}.
     * @return un catalogo de los puntos de interes almacenados en un archivo.
     * @throws IOException si no se puede leer una linea del archivo.
     * @throws FileNotFoundException si no puede encontrar el archivo. 
     */
    public POICatalog getCatalog()throws IOException,FileNotFoundException {
        POICatalog catalog = new POICatalog(this.filePathPOI);
        return this.load(catalog);
    }


    /**
     * Metodo auxiliar que lee un archivo utilizando la ruta especificada.
     * en el atributo {@link #FilePathPOI} y regresa un catalogo de POI con la informacion del archivo.
     * @return un catalogo de POI con la informacion del archivo.
     * @throws IOException si no se puede leer una linea del archivo.
     * @throws FileNotFoundException si no puede encontrar el archivo. 
     */
    private POICatalog load(POICatalog catalog)throws IOException,FileNotFoundException{ 
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.filePathPOI));
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

                catalog.addPOI(buildPOI(row));
            }
        }catch(FileNotFoundException fnfe){
            throw new FileNotFoundException("ERROR: File could not be found in: "+ this.filePathPOI);
        }catch(IOException ioe){
            throw new IOException("ERROR: Could not read the line.");
        }finally{
            br.close();
        }
        return catalog;
    }

    /**
     * Metodo privado que construye una instancia de {@link PointOfInteres}
     * a partir de un HashMap que representa a una fila.
     * @param row fila con la que se va a construir el POI.
     * @return un punto de interes. 
     */
    private PointOfInterest buildPOI(Map<String, String> row){
        double latitude = Double.parseDouble(row.get("latitude"));
        double longitude = Double.parseDouble(row.get("longitude"));
        Location location = new Location(latitude,longitude, row.get("address"));
        
        POIType type = POIType.getType(row.get("type"));
        String id = UUID.randomUUID().toString();
        return new PointOfInterest(id,row.get("name"),row.get("description"), location, type);
    }
}