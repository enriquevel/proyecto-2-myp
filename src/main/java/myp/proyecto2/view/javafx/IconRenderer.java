package myp.proyecto2.view.javafx;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import myp.proyecto2.model.domain.ReportType;

public class IconRenderer {

    /**
     * Draw location pin marker.
     */
    public static void drawLocationPin(GraphicsContext gc, Point2D point, Color color, double size) {
        double x = point.getX();
        double y = point.getY();

        // Pin circle
        gc.setFill(color);
        gc.fillOval(x - size/2, y - size, size, size);

        // Pin point
        double[] xPoints = {x, x - size/3, x + size/3};
        double[] yPoints = {y, y - size/2, y - size/2};
        gc.fillPolygon(xPoints, yPoints, 3);

        // White center
        gc.setFill(Color.WHITE);
        gc.fillOval(x - size/4, y - size + size/4, size/2, size/2);
    }

    /**
     * Draw circle marker.
     */
    public static void drawCircleMarker(GraphicsContext gc, Point2D point, Color color, double radius) {
        double x = point.getX();
        double y = point.getY();

        // Fill with transparency
        gc.setFill(Color.rgb(
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                0.3
        ));
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // Stroke
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    /**
     * Draw report icon based on type.
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
     * Draw warning triangle.
     */
    private static void drawWarningTriangle(GraphicsContext gc, Point2D point, Color color, double size) {
        double x = point.getX();
        double y = point.getY();

        double[] xPoints = {x, x - size, x + size};
        double[] yPoints = {y - size, y + size, y + size};

        // Fill
        gc.setFill(Color.rgb(
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                0.3
        ));
        gc.fillPolygon(xPoints, yPoints, 3);

        // Stroke
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    /**
     * Draw square marker.
     */
    private static void drawSquareMarker(GraphicsContext gc, Point2D point, Color color, double size) {
        double x = point.getX();
        double y = point.getY();

        gc.setFill(Color.rgb(
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                0.3
        ));
        gc.fillRect(x - size, y - size, size * 2, size * 2);

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeRect(x - size, y - size, size * 2, size * 2);
    }

    /**
     * Get color for report type (inlined).
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
