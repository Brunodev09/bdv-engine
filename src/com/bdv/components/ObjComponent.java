package com.bdv.components;

import com.bdv.ECS.Component;
import com.bdv.renders.opengl.OpenGLBufferedModel;
import com.bdv.renders.opengl.helpers.ModelParser;

public class ObjComponent extends Component<ObjComponent> {
    public String filePath;
    public OpenGLBufferedModel data;

    public ObjComponent() {
    }

    private ObjComponent(String filePath) {
        this.filePath = filePath;
        this.data = ModelParser.parseOBJ(this.filePath);
    }

    public static ObjComponent invoke(String filePath) {
        return new ObjComponent(filePath);
    }
}
