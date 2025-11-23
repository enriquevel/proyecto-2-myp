package myp.proyecto2.model.catalog;

import java.util.List;

/** 
 * Interfaz que define el comportamiento de un catalogo.
 *
 * @param <O> representa los objetos que se almacenaran en el catalogo.
 * @param <T> representa la clasificacion por "tipos" compatible con dichos objetos.
 */
public interface Catalog <O,T> {

    /**
     * Agrega un objeto al catalogo o lo actualiza, y lo guarda en una base de datos.
     *
     * @param object objeto que se desea guardar o actualizar.
     * @throws NullPointerException si el objeto dado es <code>null</code>.
     */
    void save(O object);

    /**
     * Elimina un objeto del catalogo y de la base de datos.
     *
     * @param object objeto que se desea eliminar.
     * @return <code>true</code> si se elimino el objeto deseado, <code>false</code> en
     *          otro caso.
     * @throws NullPointerException si el objeto que se quiere eliminar es <code>null</code>.
     */
    boolean delete(O object);

    /**
     * Devuelve una lista de todos los objetos del catalogo.
     *
     * @return una lista de todos los objetos del catalogo.
     */
    List<O> findAll();

    /**
     * Devuelve un objeto del catalogo con un identificador especifico.
     *
     * @param id del objeto deseado.
     * @return el objeto con el identificador dado.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    O findById(String id);

    /**
     * Devuelve una lista de todos los objetos que estan clasificados con el tipo T en el catalogo.
     *
     * @param type tipo de los objetos.
     * @return una lista de todos los objetos que estan clasificados con el tipo T en el catalogo.
     * @throws NullPointerException si el tipo dado es <code>null</code>.
     */
    List<O> findByType(T type);

}