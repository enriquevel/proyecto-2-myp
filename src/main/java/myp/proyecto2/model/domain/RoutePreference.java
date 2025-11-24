package myp.proyecto2.model.domain;

/** 
 * Esta enumeracion describe los distintos tipos de preferencias que se pueden elegir
 * al momento de clasificar las rutas.
 */
public enum RoutePreference {

    /** La ruta mas segura. */
    SAFEST,

    /** La ruta mas rapida. */
    FASTEST,

    /** Balanceado. */
    BALANCED;

    /**
     * Regresa el "nombre" asociado a cada elemento de la enumeracion.
     *
     * @return el "nombre" asociado a cada elemento de la enumeracion.
     */
    public String getDisplayName() {
        return switch(this) {
            case SAFEST -> "Ruta mas segura";
            case FASTEST -> "Ruta mas rapida";
            case BALANCED -> "Ruta balanceada";
        };
    }
}