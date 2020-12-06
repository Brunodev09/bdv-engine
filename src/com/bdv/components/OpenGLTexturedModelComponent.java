package com.bdv.components;

import com.bdv.ECS.Component;
import com.bdv.renders.opengl.OpenGLModel;
import com.bdv.renders.opengl.OpenGLTextureCustom;

public class OpenGLTexturedModelComponent extends Component<OpenGLTexturedModelComponent> {
    private OpenGLModel mdl;
    private OpenGLTextureCustom tmdl;

    public OpenGLTexturedModelComponent() {}

    public OpenGLTexturedModelComponent(OpenGLModel mdl, OpenGLTextureCustom tmdl) {
        this.mdl = mdl;
        this.tmdl = tmdl;
    }

    public static OpenGLTexturedModelComponent invoke(OpenGLModel mdl, OpenGLTextureCustom tmdl) {
        return new OpenGLTexturedModelComponent(mdl, tmdl);
    }

    public OpenGLModel getModel() {
        return mdl;
    }

    public OpenGLTextureCustom getModelTexture() {
        return tmdl;
    }
}
