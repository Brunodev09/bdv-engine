package engine.math;

public class Vector2i {
    private int x;
    private int y;

    public Vector2i() {
        x = 0; y= 0;
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
