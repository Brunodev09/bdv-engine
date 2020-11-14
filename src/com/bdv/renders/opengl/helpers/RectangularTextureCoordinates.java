package com.bdv.renders.opengl.helpers;

public class RectangularTextureCoordinates {
    public final int x;
    public final int y;
    public final int x2;
    public final int y2;
    public final int x3;
    public final int y3;
    public final int x4;
    public final int y4;

    // Textures coordinates will be stored from top-left (x, y, w, h)
    // That derives to p1 = (x, y), p2 = (x + w, y), p3 = (x, y + h) and p4 = (x + w, y + h)
    public RectangularTextureCoordinates(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;

        this.x2 = x + w;
        this.y2 = y;

        this.x3 = x;
        this.y3 = y + h;

        this.x4 = x + w;
        this.y4 = y + h;

    }
}
