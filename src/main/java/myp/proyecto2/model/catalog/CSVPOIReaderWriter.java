package myp.proyecto2.model.catalog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import myp.proyecto2.model.domain.Location;
import myp.proyecto2.model.domain.PointOfInterest;
import myp.proyecto2.model.domain.POIType;

class CSVPOIReaderWriter implements CSVReaderWriter<PointOfInterest> {

    /** La ruta del archivo. */
    private final String filePath;

    private static final String HEADER = "id,type,name,latitude,longitude,address,description";

    /**
     * Constructor principal de la clase {@link CSVPOIReaderWriter}.
     *
     * @param filePath ruta del archivo.
     * @throws NullPointerException si la ruta proporcionada es <code>null</code>.
     */
    CSVPOIReaderWriter(String filePath) {
        if (filePath == null)
            throw new NullPointerException("File path cannot be null.");

        this.filePath = filePath;
    }

    /**
     * Devuelve una lista con todos los puntos de interes en el archivo.
     *
     * @return una lista con todos los puntos de interes en el archivo.
     * @throws IOException si ocurrio un error al leer el archivo.
     */
    @Override
    public List<PointOfInterest> readAll() throws IOException {
        List<PointOfInterest> pois = new ArrayList<>();

        File file = new File(this.filePath);
        if (!file.exists())
            return pois;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine();
            if (header == null)
                return pois;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                try {
                    PointOfInterest poi = parseLine(line);
                    pois.add(poi);
                } catch (Exception e) {
                    System.err.println("Failed to parse line: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new IOException("Error reading file " + this.filePath, e);
        }

        return pois;
    }

    /**
     * Agrega un punto de interes al archivo.
     *
     * @param poi el punto de interes que se quiere agregar.
     * @throws IOException si ocurrio un error al leer o escribir el archivo.
     */
    @Override
    public void add(PointOfInterest poi) throws IOException {
        if (poi == null)
            throw new NullPointerException("Point of interest cannot be null.");

        File file = new File(this.filePath);
        boolean fileExists = file.exists();
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            if (!fileExists || file.length() == 0) {
                bw.write(HEADER);
                bw.newLine();
            }

            bw.write(poi.toString());
            bw.newLine();

        } catch (IOException ioe) {
            throw new IOException("Error reading or writing file " + this.filePath, ioe);
        } finally {
            if (bw != null)
                bw.close();
        }
    }

    /**
     * Elimina un punto de interes del archivo.
     *
     * @param poi el punto de interes que se quiere eliminar.
     * @throws IOException si no se puede leer una linea del archivo o bien cuando existen
     *          problemas al intentar encontrar el archivo durante su reescritura.
     */
    @Override
    public boolean delete(PointOfInterest poi) throws IOException {
        if (poi == null)
            throw new NullPointerException("Point of interest cannot be null.");

        List<PointOfInterest> allPOIs = readAll();
        if (!allPOIs.remove(poi))
            return false;

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.filePath));
            bw.write(HEADER);
            bw.newLine();

            for (PointOfInterest p : allPOIs) {
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException ioe) {
            throw new IOException("Error reading or writing file " + this.filePath, ioe);
        } finally {
            if (bw != null)
                bw.close();
        }
        return true;
    }

    private PointOfInterest parseLine(String line) {
        String[] parts = line.split(",", -1);

        if (parts.length != 7)
            throw new IllegalArgumentException("Invalid CSV line: expected 7 fields, got " + parts.length);

        try {
            String id = parts[0].trim();
            POIType type = POIType.getType(parts[1].trim());
            String name = parts[2].trim();
            double latitude = Double.parseDouble(parts[3].trim());
            double longitude = Double.parseDouble(parts[4].trim());
            String address = parts[5].trim();
            String description = parts[6].trim();

            Location location = new Location(latitude, longitude, address);

            return new PointOfInterest(id, name, description, location, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse point of interest: " + e.getMessage(), e);
        }
    }
}