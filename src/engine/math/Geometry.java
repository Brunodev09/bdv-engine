package engine.math;

public class Geometry {
    private Geometry() {
    }
    public static double distanceBetweenPoints(float x, float y, float x2, float y2) {
        return Math.sqrt(((x - x2) * (x - x2)) + ((y - y2) * (y - y2)));
    }
}
