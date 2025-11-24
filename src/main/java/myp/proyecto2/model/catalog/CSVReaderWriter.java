package myp.proyecto2.model.catalog;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz que define el comportamiento de un lector y escritor de archivos.
 *
 * @param <T> el tipo de entidades almacenadas en el archivo.
 */
interface CSVReaderWriter<T> {

    /**
     * Devuelve una lista con todas las entidades en el archivo.
     *
     * @return una lista con todas las entidades en el archivo.
     * @throws IOException si ocurrio un error al leer el archivo.
     */
    List<T> readAll() throws IOException;

    /**
     * Agrega una entidad al archivo.
     *
     * @param t la entidad que se quiere agregar.
     * @throws IOException si ocurrio un error al leer o escribir el archivo.
     */
    void add(T t) throws IOException;

    /**
     * Elimina una entidad del archivo.
     * @param t la entidad que se quiere eliminar.
     * @throws IOException si no se puede leer una linea del archivo o bien cuando existen
     *          problemas al intentar encontrar el archivo durante su reescritura.
     */
    boolean delete(T t) throws IOException;

    /**
     * Escribe todas las entidades de una lista al archivo, sobreescribiendolo.
     *
     * @throws IOException si ocurrio un error al escribir el archivo.
     */
    void writeAll(List<T> all) throws IOException;

}
