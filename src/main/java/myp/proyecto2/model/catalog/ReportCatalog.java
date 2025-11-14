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
 * Clase utilizada para dar una representacion interna a todos los reportes.
 */
public class ReportCatalog implements Catalog<Report,ReportType>{

    /** Ruta del archivo donde se guardan los reportes. */
    private final String filePath;

    /** Mapa de los reportes por ID. */
    private Map<String, Report> reportsByID;

    /** Mapa de los reportes por tipo. */
    private Map<ReportType, List<Report>> reportsByType;

    /**
     * Constructor principal de la clase {@link ReportCatalog}. Inicializa
     * los diccionariios y define la ruta del archivo.
     * @param filePath ruta del archivo donde se guardan los reportes.
     */
    public ReportCatalog(String filePath) {
        if(filePath == null)    
            throw new NullPointerException("The file path cannot be null.");
        
        this.filePath = filePath;
        this.reportsByID = new HashMap<>();
        this.reportsByType = new EnumMap<>(ReportType.class);
    }
    
    /**
     * Agrega un reporte al catalogo.
     * @param report reporte que se dedesa agregar.
     * @throws NullPointerException si el reporte que se quiere agregar es <code>null</code>.
     */
    @Override
    public void add(Report report) throws NullPointerException {
        if (report == null)
            throw new NullPointerException("The point of interest you want to add cannot be null.");
        //Agrega el reporte a un hashMap donde su id funge como llave.
        Report old = this.reportsByID.put(report.getId(), report);

        /*Como put regresa <code>null</code> si el objeto agregado no estaba previamente en el hashmap,
         * verificamos el caso contrario.
         */
        if (old !=null)
            this.reportsByType.get(old.getType()).remove(old);
        //Finalmente agregamos el nuevo punto de interes a la lista del segundo hash map.//
        this.reportsByType.get(report.getType()).add(old);
    }



    /** 
     * Elimina un reporte del catalogo.
     * @param report reporte que se quiere eliminar.
     * @return <code>true</code> si el reporte estaba en el catalogo y fue eliminado.<code>false</code> 
     * si el reporte no estaba en el catalogo y no pudo ser eliminado.
     * @throws NullPointerException si el reporte que se quiere eliminar es <code>null</code>.
    */
    @Override
    public Boolean delete(Report report) throws NullPointerException {
        if(report == null) 
            throw new NullPointerException("The point of interest you want to delete cannot be null.");

        Report removed = this.reportsByID.remove(report.getId());
        //Verificamos si el reporte realmente estaba en el catalogo.
        if (removed!= null){
            this.reportsByType.get(removed.getType()).remove(removed);//Lo removemos de la lista del segundo hashmap.
            return true; 
        }
        return false;
    }

    /**  
     * Regresa una lista de todos los reportes.
     * @return una lista de todos los reportes.
    */
    @Override
    public List<Report> findAll() {
       return new ArrayList<>(this.reportsByID.values());
    }

    /**
     * Regresa el reporte asociado a un identificador.
     * @param id identificador con el que se quiere buscar el reporte.
     * @return el reporte reporte asociado al identificador.<code>null</code> si 
     * no existe un reporte con dicho identificador.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    @Override
    public Report findById(String id) throws NullPointerException {
        if(id == null) 
            throw new NullPointerException("The ID cannot be null.");
        return this.reportsByID.get(id);
    }

    /**
     * Regresa una lista de todos los reportes de un cierto tipo. Los tipos validos
     * son elementos {@link ReportType}).
     * @param type tipo de reporte. Elemento {@link ReportType}).
     * @return una lista de todos los reportes del tipo dado.
     * @throws NullPointerException si el tipo dado es <code>null</code>.
     */
    @Override
    public List<Report> findByType(ReportType type) throws NullPointerException {
        return this.reportsByType.get(type);
    }

    /**
     * Agrega un reporte al catalogo y tambien lo guarda en la base de datos.
     * @param report reporte que se quiere guardar.
     * @throws NullPointerException si el reporte que se quiereguardar es <code>null</code>.
     * @throws IOException cuando existen problemas al intentar encontrar el archivo.
     */
    @Override
    public void save(Report report) throws NullPointerException, IOException {
        if (report == null)throw new NullPointerException("Cannot save a null report.");
        add(report);
        ReportCatalogReaderWriter readerWriter = new ReportCatalogReaderWriter(this.filePath);
        readerWriter.add(report);
    }

    /**
     * Elimina un reporte del catalogo y de la base de datos.
     * @param report reporte que se desea eliminar.
     * @throws NullPointerException si el reporte dado es <code>null</code>.
     * @throws IOException si no se  puede leer una linea del archivo o bien 
     * existen problemas al intentar encontrar el archivo durante su reescritura.
     */
    @Override
    public void dontSave(Report report) throws NullPointerException, IOException {
        if (report == null)throw new NullPointerException("Cannot delete a null report.");
        if(delete(report)){
            ReportCatalogReaderWriter readerWriter = new ReportCatalogReaderWriter(this.filePath);
            readerWriter.delete(report);
        }
    }

    //-----Otra manera de buscar en un catalogo de reportes.-----//
    /**
     * Regresa una lista de todos los reportes activos.
     * @return una lista de todos los reportes activos.
     */
    public List<Report> findByActive(){
        List <Report> activeReports = new ArrayList<>();

        for(Report report: findAll())
            if (report.isActive()) activeReports.add(report);

        return activeReports;
    }

}