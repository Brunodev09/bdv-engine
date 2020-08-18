package app.Math;

public class Vector3i {
    private int x;
    private int y;
    private int z;

    public Vector3i() {
        x = 0; y = 0; z = 0;
    }

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void setZ(int z) {
        this.z = z;
    }

    public int getZ() {
        return z;
    }
}

