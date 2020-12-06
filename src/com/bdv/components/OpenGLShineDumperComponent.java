package com.bdv.components;

public class OpenGLShineDumperComponent {
    public int factor;
    public OpenGLShineDumperComponent() {}
    private OpenGLShineDumperComponent(int factor) {
        this.factor = factor;
    }

    public static OpenGLShineDumperComponent invoke(int factor) {
        return new OpenGLShineDumperComponent(factor);
    }
}
