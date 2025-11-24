package myp.proyecto2.view.javafx;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import myp.proyecto2.model.domain.ReportType;

/**
 * Clase utilitaria para renderizar iconos en el mapa para reportes.
 */
public class IconRenderer {

    /**
     * Dibuja un pin de localizacion.
     *
     * @param gc el contexto donde dibujar
     * @param point el punto donde se quiere dibujar
     * @param color el color del pin
     * @param size el tamano del pin
     */
    public static void drawLocationPin(GraphicsContext gc, Point2D point, Color color, double size) {
        double x = point.getX();
        double y = point.getY();

        gc.setFill(color);
        gc.fillOval(x - size/2, y - size, size, size);

        double[] xPoints = {x, x - size/3, x + size/3};
        double[] yPoints = {y, y - size/2, y - size/2};
        gc.fillPolygon(xPoints, yPoints, 3);

        gc.setFill(Color.WHITE);
        gc.fillOval(x - size/4, y - size + size/4, size/2, size/2);
    }

    /**
     * Dibuja un marcador circular.
     *
     * @param gc el contexto donde dibujar
     * @param point el punto donde se quiere dibujar
     * @param color el color del marcador
     * @param radius el radio del marcador
     */
    public static void drawCircleMarker(GraphicsContext gc, Point2D point, Color color, double radius) {
        double x = point.getX();
        double y = point.getY();

        gc.setFill(Color.rgb((int)(color.getRed() * 255), (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255), 0.3));
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    /**
     * Dibuja un icono para reportes.
     *
     * @param gc el contexto donde dibujar
     * @param point el punto donde se quiere dibujar
     * @param type el tipo de reporte a dibujar
     * @param size el tamano del icono
     */
    public static void drawReportIcon(GraphicsContext gc, Point2D point, ReportType type, double size) {
        Color color = getReportColor(type);

        switch (type) {
            case CRIME_INCIDENT, ACCIDENT -> drawWarningTriangle(gc, point, color, size);
            case TRAFFIC_JAM, CONSTRUCTION -> drawCircleMarker(gc, point, color, size);
            default -> drawSquareMarker(gc, point, color, size);
        }
    }

    /**
     * Dibuja un triangulo de advertencia.
     *
     * @param gc el contexto donde dibujar
     * @param point el punto donde se quiere dibujar
     * @param color el color del triangulo
     * @param size el tamano del triangulo
     */
    private static void drawWarningTriangle(GraphicsContext gc, Point2D point, Color color, double size) {
        double x = point.getX();
        double y = point.getY();

        double[] xPoints = {x, x - size, x + size};
        double[] yPoints = {y - size, y + size, y + size};

        gc.setFill(Color.rgb((int)(color.getRed() * 255), (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255), 0.3));
        gc.fillPolygon(xPoints, yPoints, 3);

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    /**
     * Dibuja un marcador cuadrado.
     *
     * @param gc el contexto donde dibujar
     * @param point el punto donde se quiere dibujar
     * @param color el color del marcador
     * @param size el tamano del marcador
     */
    private static void drawSquareMarker(GraphicsContext gc, Point2D point, Color color, double size) {
        double x = point.getX();
        double y = point.getY();

        gc.setFill(Color.rgb((int)(color.getRed() * 255), (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255), 0.3));
        gc.fillRect(x - size, y - size, size * 2, size * 2);

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeRect(x - size, y - size, size * 2, size * 2);
    }

    /**
     * Devuelve un color correspondiente a un tipo de reporte.
     *
     * @param type el tipo de reporte
     * @return un color correspondiente a un tipo de reporte
     */
    private static Color getReportColor(ReportType type) {
        return switch (type) {
            case CRIME_INCIDENT -> Color.rgb(183, 28, 28);   // Dark Red
            case ACCIDENT -> Color.rgb(211, 47, 47);          // Red
            case TRAFFIC_JAM -> Color.rgb(255, 152, 0);       // Orange
            case CONSTRUCTION -> Color.rgb(255, 193, 7);          // Amber
            case FLOODING -> Color.rgb(3, 169, 244);          // Light Blue
            case NATURAL_DEBRIS -> Color.rgb(139, 195, 74);      // Light Green
            case OTHER -> Color.rgb(121, 85, 72);           // Brown
            case STREETLIGHT_OUT -> Color.rgb(96, 125, 139);  // Blue Gray
            case LOST_ITEM -> Color.rgb(158, 158, 158);       // Gray
        };
    }
}
