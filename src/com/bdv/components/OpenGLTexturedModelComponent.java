package com.bdv.components;

import com.bdv.ECS.Component;
import com.bdv.renders.opengl.OpenGLModelComponent;

public class OpenGLTexturedModelComponent extends Component<OpenGLTexturedModelComponent> {
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
