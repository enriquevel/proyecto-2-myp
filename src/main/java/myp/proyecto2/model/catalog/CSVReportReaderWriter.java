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
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

class CSVReportReaderWriter implements CSVReaderWriter<Report> {

    /** La ruta del archivo. */
    private final String filePath;

    private final static String HEADER = "id,type,latitude,longitude,address,description,upvotes,downvotes";

    /**
     * Constructor principal de la clase {@link CSVReportReaderWriter}.
     * Inicializa la ruta del archivo.
     *
     * @param filePath ruta del archivo.
     * @throws NullPointerException si la ruta del archivo es <code>null</code>
     */
    CSVReportReaderWriter(String filePath) {
        if (filePath == null)
            throw new NullPointerException("File path cannot be null.");

        this.filePath = filePath;
    }

    /**
     * Devuelve una lista con todos los reportes en el archivo.
     *
     * @return una lista con todos los reportes en el archivo.
     * @throws IOException si ocurrio un error al leer el archivo.
     */
    @Override
    public List<Report> readAll() throws IOException {
        List<Report> reports = new ArrayList<>();

        File file = new File(this.filePath);
        if (!file.exists())
            return reports;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine();
            if (header == null)
                return reports;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                try {
                    Report report = parseLine(line);
                    reports.add(report);
                } catch (Exception e) {
                    System.err.println("Failed to parse line: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new IOException("Error reading file " + this.filePath, e);
        }

        return reports;
    }

    /**
     * Agrega un reporte al archivo.
     *
     * @param report reporte que se quiere agregar.
     * @throws IOException si ocurrio un error al leer o escribir el archivo.
     */
    @Override
    public void add(Report report) throws IOException {
        if (report == null)
            throw new NullPointerException("Report cannot be null.");

        File file = new File(this.filePath);
        boolean fileExists = file.exists();
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            if (!fileExists || file.length() == 0) {
                bw.write(HEADER);
                bw.newLine();
            }

            bw.write(report.toString());
            bw.newLine();

        } catch (IOException ioe) {
            throw new IOException("Error reading or writing file " + this.filePath, ioe);
        } finally {
            if (bw != null)
                bw.close();
        }
    }

    /**
     * Elimina un reporte del archivo.
     *
     * @param report el reporte que se quiere eliminar.
     * @throws IOException si no se puede leer una linea del archivo o bien cuando existen
     *          problemas al intentar encontrar el archivo durante su reescritura.
     */
    @Override
    public boolean delete(Report report) throws IOException {
        if (report == null)
            throw new NullPointerException("Report cannot be null.");

        List<Report> allReports = readAll();
        if (!allReports.remove(report))
            return false;

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.filePath));
            bw.write(HEADER);
            bw.newLine();

            for (Report r : allReports) {
                bw.write(r.toString());
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

    private Report parseLine(String line) {
        String[] parts = line.split(",", -1);

        if (parts.length != 8)
            throw new IllegalArgumentException("Invalid CSV line: expected 8 fields, got " + parts.length);

        try {
            String id = parts[0].trim();
            ReportType type = ReportType.getType(parts[1].trim());
            double latitude = Double.parseDouble(parts[2].trim());
            double longitude = Double.parseDouble(parts[3].trim());
            String address = parts[4].trim();
            String description = parts[5].trim();
            int upvotes = Integer.parseInt(parts[6].trim());
            int downvotes = Integer.parseInt(parts[7].trim());

            Location location = new Location(latitude, longitude, address);

            return new Report(id, type, location, description, upvotes, downvotes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse report: " + e.getMessage(), e);
        }
    }
}
