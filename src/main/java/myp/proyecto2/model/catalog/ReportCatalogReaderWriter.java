package myp.proyecto2.model.catalog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

public class ReportCatalogReaderWriter {

    /** La ruta del archivo. */
    private final String filePathPOI;

    /**
     * Constructor principal de la clase {@link ReportCatalogReaderWriter}.
     * Inicializa la ruta del archivo.
     * @param filePath ruta del archivo.
     * @throws NullPointerException si la ruta del archivo es <code>null</code>
     */
    public ReportCatalogReaderWriter(String filePath)throws NullPointerException{
        if(filePath == null)throw new NullPointerException("File path to read from cannot be null. From ReportCatalogLoader.");

        this.filePathPOI = filePath;
    }

    /**
     * Regresa un catalogo de reportes obtenido del archivo 
     * almacenado en {@link #filePathPOI}.
     * @return un catalogo de reportes basado en el contenido de un archivo.
     * @throws IOException si no se puede  leer una linea del archivo.
     * @throws FileNotFoundException si no se pudo encontrar el archivo.
     */
    public ReportCatalog getCatalog()throws IOException,FileNotFoundException {
        ReportCatalog catalog = new ReportCatalog(this.filePathPOI);
        return this.load(catalog);
    }

    /**
     * Agrega un reporte al archivo.
     * @param report reporte que se quiere agregar.
     * @throws IOException cuando existen problemas al intentar encontrar el archivo.
     */
    public void add(Report report) throws IOException{
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.filePathPOI,true));
            bw.newLine();
            bw.write(report.getFileFormat());
        } catch (IOException ioe) {
            throw new IOException("ERROR: An error occurred while trying to find the file.");
        }finally{
            if (bw!= null) bw.close();
        }
    }

    /**
     * ELimina un  reporte del archivo.
     * @param report reporte que se quiere eliminar.
     * @throws IOException si no se  puede leer una linea del archivo o bien
     * cuando existen problemas al intentar encontrar el archivo durante su reescritura.
     * @throws FileNotFoundException si no se puede encontrar el archivo durante su lectura.
     */
    public void delete(Report report) throws IOException, FileNotFoundException{
        //Se almacenan todas las lineas del archivo omitiendo la linea correspondiente al punto de interes.
        BufferedReader br = null;
        List<String> lines = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(this.filePathPOI));
            String line;
            while ((line = br.readLine()) != null){
                if (line.trim().isEmpty())
                    continue;

                if (!line.equals(report.getFileFormat()))
                    lines.add(line);
            }
        }catch(FileNotFoundException fnfe){
            throw new FileNotFoundException("ERROR: File could not be found in: "+ this.filePathPOI);
        }catch(IOException ioe){
            throw new IOException("ERROR: Could not read the line.");
        }finally{
            if (br!= null) br.close();
        }
        
        //Se reescribe el archivo, ahora sin el punto de interes que se queria eliminar.
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.filePathPOI));
            for (String l : lines){
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException ioe) {
            throw new IOException("ERROR: An error occurred while trying to find the file.");
        }finally{
            if (bw!= null) bw.close();
        }
    }

    /**
     * Metodo auxiliar que lee un archivo utilizando la ruta especificada.
     * en el atributo {@link #filePathPOI} y llena un catalogo de reportes con la informacion
     * contenida en dicho archivo.
     * @param catalog que se va a llenar.
     * @return un catalogo de reportes con la informacion contenida en el archivo.
     * @throws IOException si no se puede leer una linea del archivo.
     * @throws FileNotFoundException si no puede encontrar el archivo. 
     */
    private ReportCatalog load(ReportCatalog catalog)throws IOException,FileNotFoundException{ 
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
                    row.put(cols[i], values[i]); //Se suben las cadenas con espacios.
                }

                catalog.add(buildReport(row));
            }
        }catch(FileNotFoundException fnfe){
            throw new FileNotFoundException("ERROR: File could not be found in: "+ this.filePathPOI);
        }catch(IOException ioe){
            throw new IOException("ERROR: Could not read the line.");
        }finally{
            if (br!= null) br.close();
        }
        return catalog;
    }

    /**
     * Metodo auxiliar que construye una instancia de {@link Report} utilizando
     * un HashMap que representa una fila del archivo.
     * @param row fila con la que se va a construir el reporte.
     * @return un reporte.
     */
    private Report buildReport(Map<String, String> row){
        double latitude = Double.parseDouble(row.get("latitude"));
        double longitude = Double.parseDouble(row.get("longitude"));
        Location location = new Location(latitude,longitude, row.get("address"));
        
        ReportType type = ReportType.getType(row.get("type"));
        String id = UUID.randomUUID().toString();
        return new Report(id, type, location, row.get("description"));
    }
}
