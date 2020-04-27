package app.Math;

public class Point<T extends Number> {
    public T x;
    public T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }
}
