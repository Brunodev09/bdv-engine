package com.bdv.components;

import com.bdv.ECS.Component;

public class OpenGLReflectivityComponent extends Component<OpenGLReflectivityComponent> {
    public int factor;
    public OpenGLReflectivityComponent() {}
    private OpenGLReflectivityComponent(int factor) {
        this.factor = factor;
    }

    public static OpenGLReflectivityComponent invoke(int factor) {
        return new OpenGLReflectivityComponent(factor);
    }
}
