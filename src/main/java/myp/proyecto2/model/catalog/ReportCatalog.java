package myp.proyecto2.model.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import myp.proyecto2.model.domain.Report;
import myp.proyecto2.model.domain.ReportType;

/**
 * Clase utilizada para dar una representacion interna al conjunto de todos los reportes.
 */
public class ReportCatalog implements Catalog<Report, ReportType> {

    /** Lector y escritor de archivos para reportes. */
    private final CSVReportReaderWriter readerWriter;

    /** Mapa de los reportes por ID. */
    private Map<String, Report> reportsByID;

    /** Mapa de los reportes por tipo. */
    private Map<ReportType, List<Report>> reportsByType;

    /**
     * Constructor principal de la clase {@link ReportCatalog}.
     * Inicializa los diccionarios y define la ruta del archivo.
     *
     * @param filePath ruta del archivo donde se guardan los reportes.
     */
    public ReportCatalog(String filePath) {
        if (filePath == null)
            throw new NullPointerException("The file path cannot be null.");

        this.readerWriter = new CSVReportReaderWriter(filePath);
        this.reportsByID = new HashMap<>();
        this.reportsByType = new EnumMap<>(ReportType.class);

        initialize();
    }

    private void initialize() {
        try {
            List<Report> loaded = this.readerWriter.readAll();

            for (ReportType type : ReportType.values())
                this.reportsByType.put(type, new ArrayList<>());

            for (Report report : loaded) {
                this.reportsByID.put(report.getId(), report);
                this.reportsByType.get(report.getType()).add(report);
            }

            System.out.println("Loaded " + this.reportsByID.size() + " reports from CSV");
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to load reports: " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Agrega un reporte al catalogo y tambien lo guarda en la base de datos.
     *
     * @param report el reporte que se quiere guardar.
     * @throws NullPointerException si el reporte que se quiere guardar es <code>null</code>.
     * @throws RuntimeException si ocurrio un error al guardar el reporte.
     */
    @Override
    public void save(Report report) {
        if (report == null)
            throw new NullPointerException("Cannot save a null report.");

        add(report);

        try {
            this.readerWriter.add(report);
        } catch (IOException ioe) {
            delete(report);
            throw new RuntimeException("Failed to save report.", ioe);
        }
    }

    /**
     * Agrega un reporte al catalogo.
     *
     * @param report reporte que se desea agregar.
     * @throws NullPointerException si el reporte que se quiere agregar es <code>null</code>.
     */
    private void add(Report report) {
        if (report == null)
            throw new NullPointerException("Cannot add a null report.");
        //Agrega el reporte a un hashMap donde su id funge como llave.
        Report old = this.reportsByID.put(report.getId(), report);

        /*Como put regresa <code>null</code> si el objeto agregado no estaba previamente en el hashmap,
         * verificamos el caso contrario.
         */
        if (old != null)
            this.reportsByType.get(old.getType()).remove(old);
        //Finalmente agregamos el nuevo punto de interes a la lista del segundo hash map.//
        this.reportsByType.get(report.getType()).add(report);
    }

    /** 
     * Elimina un reporte del catalogo y de la base de datos.
     *
     * @param report el reporte que se quiere eliminar.
     * @return <code>true</code> si el reporte estaba en el catalogo y fue eliminado, <code>false</code>
     *          en otro caso.
     * @throws NullPointerException si el reporte que se quiere eliminar es <code>null</code>.
     * @throws RuntimeException si ocurrio un error al eliminar el reporte.
    */
    @Override
    public boolean delete(Report report) {
        if (report == null)
            throw new NullPointerException("Cannot delete a null report.");

        try {
            boolean deletedFromFile = this.readerWriter.delete(report);

            if (deletedFromFile) {
                Report removed = this.reportsByID.remove(report.getId());
                if (removed != null)
                    this.reportsByType.get(removed.getType()).remove(removed);

                return true;
            }

            return false;
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to delete report: " + ioe.getMessage(), ioe);
        }
    }

    /**  
     * Regresa una lista de todos los reportes.
     *
     * @return una lista de todos los reportes.
    */
    @Override
    public List<Report> findAll() {
       return new ArrayList<>(this.reportsByID.values());
    }

    /**
     * Regresa el reporte asociado a un identificador.
     *
     * @param id identificador con el que se quiere buscar el reporte.
     * @return el reporte reporte asociado al identificador.<code>null</code> si 
     *          no existe un reporte con dicho identificador.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    @Override
    public Report findById(String id) {
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");
        return this.reportsByID.get(id);
    }

    /**
     * Regresa una lista de todos los reportes de un cierto tipo.
     * Los tipos validos son elementos {@link ReportType}).
     *
     * @param type tipo de reporte. Elemento {@link ReportType}).
     * @return una lista de todos los reportes del tipo dado.
     * @throws NullPointerException si el tipo dado es <code>null</code>.
     */
    @Override
    public List<Report> findByType(ReportType type) {
        return this.reportsByType.get(type);
    }

    /**
     * Regresa una lista de todos los reportes activos.
     *
     * @return una lista de todos los reportes activos.
     */
    public List<Report> findActive() {
        List <Report> activeReports = new ArrayList<>();

        for (Report report: findAll()) {
            if (report.isActive()) 
                activeReports.add(report);
        }
        return activeReports;
    }
}