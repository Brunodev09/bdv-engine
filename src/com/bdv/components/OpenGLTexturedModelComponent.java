package com.bdv.components;

public class OpenGLTexturedModelComponent {
    private final OpenGLModelComponent mdl;
    private final OpenGLTextureCustomComponent tmdl;

    public OpenGLTexturedModelComponent(OpenGLModelComponent mdl, OpenGLTextureCustomComponent tmdl) {
        this.mdl = mdl;
        this.tmdl = tmdl;
    }

    public OpenGLModelComponent getModel() {
        return mdl;
    }

    public OpenGLTextureCustomComponent getModelTexture() {
        return tmdl;
    }
}
