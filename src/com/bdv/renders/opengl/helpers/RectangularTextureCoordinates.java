package com.bdv.renders.opengl.helpers;

import java.util.HashMap;
import java.util.Map;

public class RectangularTextureCoordinates<T extends Number> {
    public T x;
    public T y;
    public T x2;
    public T y2;
    public T x3;
    public T y3;
    public T x4;
    public T y4;

    public Map<Integer, T> counterClockWiseGetter = new HashMap<>();

    // Textures coordinates will be stored from top-left (x, y, w, h) clock-wise order
    // That derives to p1 = (x, y), p2 = (x + w, y), p3 = (x + w, y + h) and p4 = (x, y + h)
    public RectangularTextureCoordinates(T x, T y, T w, T h) {
        this.x = x;
        this.y = y;

        if (x instanceof Integer && w instanceof Integer && y instanceof Integer && h instanceof Integer) {
            this.x2 = (T) Integer.valueOf(x.intValue() + w.intValue());
            this.y4 = (T) Integer.valueOf(y.intValue() + h.intValue());
            this.x3 = (T) Integer.valueOf(x.intValue() + w.intValue());
            this.y3 = (T) Integer.valueOf(y.intValue() + h.intValue());
        } else if (x instanceof Float && w instanceof Float && y instanceof Float && h instanceof Float) {
            this.x2 = (T) Float.valueOf(x.floatValue() + w.floatValue());
            this.y4 = (T) Float.valueOf(y.floatValue() + h.floatValue());
            this.x3 = (T) Float.valueOf(x.floatValue() + w.floatValue());
            this.y3 = (T) Float.valueOf(y.floatValue() + h.floatValue());
        }
        this.y2 = y;
        this.x4 = x;
    }

    public RectangularTextureCoordinates(T x, T y, T x2, T y2, T x3, T y3, T x4, T y4) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    public T queryOrderedPoint(int order) {
        if (order < 1 || order > 8) return null;
        T value = null;
        switch (order) {
            case 1:
                value = x2;
                break;
            case 2:
                value = y2;
                break;
            case 3:
                value = x;
                break;
            case 4:
                value = y;
                break;
            case 5:
                value = x4;
                break;
            case 6:
                value = y4;
                break;
            case 7:
                value = x3;
                break;
            case 8:
                value = y3;
                break;
        }
        return value;
    }
}
