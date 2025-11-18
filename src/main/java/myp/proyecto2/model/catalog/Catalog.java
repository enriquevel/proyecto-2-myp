package myp.proyecto2.model.catalog;

import java.io.IOException;
import java.util.List;

/** 
 * Interfaz que define el comportamiento de un catalogo. 
 * @param <O> representa los objetos que se almacenaran en el catalogo.
 * @param <T> representa la clasificacion por "tipos" compatible con dichos objetos.
 */
public interface Catalog <O,T> {
    
    /**
     * Agrega un obteto al catalogo. 
     * @param object objeto que se desea agregar.
     * @throws NullPointerException si el objeto que se quiere agregar es <code>null</code>.
     */
    void add(O object) throws NullPointerException;

    /**
     * Elimina un objeto del catalogo.
     * @param  object objeto que se desea eliminar.
     * @throws NullPointerException si el objeto que se quiere eliminar es <code>null</code>.
     */
    Boolean delete(O object) throws NullPointerException;

    /**
     * Regresa una lista de todos los objetos del catalogo.
     * @return una lista de todos los objetos del catalogo.
     */
    List<O> findAll();

    /**
     * Regresa un objeto del catalogo con un identificador especifico.
     * @param id del objeto deseado.
     * @return el objeto con el identificador dado.
     * @throws NullPointerException si el identificador es <code>null</code>.
     */
    O findById(String id) throws NullPointerException;

    /**
     * Regresa una lista de todos los objetos que estan clasificados con el tipo T en el catalogo.
     * @param type tipo de los objetos.
     * @return una lista de todos los objetos que estan clasificados con el tipo T en el catalogo.
     * @throws NullPointerException si el tipo dado es <code>null</code>.
     */
    List<O> findByType(T type) throws NullPointerException;

    /**
     * Agrega un objeto al catalogo y lo guarda en una base de datos.
     * @param object objeto que se desea guardar.
     * @throws NullPointerException si el objeto dado es <code>null</code>.
     */
    void save(O object) throws NullPointerException,IOException;

    /**
     * Elimina un objeto del catalogo y de la base de datos.
     * @param object que se desea eliminar.
     * @throws NullPointerException si el objeto dado es <code>null</code>.
     */
    void dontSave(O object) throws NullPointerException,IOException;
}