package app.Math;

public class RGBA {
    private int R;
    private int G;
    private int B;
    private int A;

    public RGBA(byte R, byte G, byte B, byte A) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;
    }

    public RGBA(int R, int G, int B, int A) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;
    }

    public void set(int R, int G, int B, int A) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;
    }

    public void set(byte R, byte G, byte B, byte A) {
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;
    }

    public int[] getColorCodes() {
        return new int[] { this.R, this.G, this.B, this.A };
    }

}
