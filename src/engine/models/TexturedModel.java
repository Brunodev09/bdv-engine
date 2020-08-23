package engine.models;


import engine.texture.ModelTexture;

public class TexturedModel {
    private Model _mdl;
    private ModelTexture _t_mdl;

    public TexturedModel(Model mdl, ModelTexture t_mdl) {
        this._mdl = mdl;
        this._t_mdl = t_mdl;
    }

    public Model getModel() {
        return _mdl;
    }

    public ModelTexture getModelTexture() {
        return _t_mdl;
    }
}

