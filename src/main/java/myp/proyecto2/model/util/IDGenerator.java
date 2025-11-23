package myp.proyecto2.model.util;

public class IDGenerator {

    private static int reportCounter = 0;

    private static int poiCounter = 0;

    private static String formatCounter(int counter) {
        return String.format("%04d", counter);
    }

    public static String generateSequentialID(String prefix) {
        return switch (prefix) {
            case "REP" -> prefix + "-" + formatCounter(++reportCounter);
            case "POI" -> prefix + "-" + formatCounter(++poiCounter);
            default -> throw new IllegalArgumentException("Unknown prefix: " + prefix);
        };
    }
}
