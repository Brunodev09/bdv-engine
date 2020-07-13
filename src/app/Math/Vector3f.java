package app.Math;

public class Vector3f {
    public float x;
    public float y;
    public float z = -1;

    public Vector3f() {
        x = 0; y = 0; z = 0;
    }

    public Vector3f(float x, float y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
