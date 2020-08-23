package engine.math;

public class RGBAf {
    private float R;
    private float G;
    private float B;
    private float A;

    public RGBAf(byte R, byte G, byte B, byte A) {
        this.R = R / 255.0f;
        this.G = G / 255.0f;
        this.B = B / 255.0f;
        this.A = A / 255.0f;
    }

    public RGBAf(float R, float G, float B, float A) {
        this.R = R / 255.0f;
        this.G = G / 255.0f;
        this.B = B / 255.0f;
        this.A = A / 255.0f;
    }

    public void set(float R, float G, float B, float A) {
        this.R = R / 255.0f;
        this.G = G / 255.0f;
        this.B = B / 255.0f;
        this.A = A / 255.0f;
    }

    public void set(byte R, byte G, byte B, byte A) {
        this.R = R / 255.0f;
        this.G = G / 255.0f;
        this.B = B / 255.0f;
        this.A = A / 255.0f;
    }

    public float[] getColorCodes() {
        return new float[] { this.R, this.G, this.B, this.A };
    }

    @Override
    public String toString() {
        return this.R + " " + this.G + " " + this.B;
    }

}
