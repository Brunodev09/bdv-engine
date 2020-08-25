package engine.shaders;

import engine.core.interfaces.Shader;
import engine.entities.Camera;
import engine.entities.Lightsource;
import engine.math.MatrixUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.io.InputStream;

public class DefaultShader extends Shader {
    private int _variableLocation1;
    private int _variableLocation2;
    private int _variableLocation3;
    private int _variableLocation4;
    private int _variableLocation5;
    private int _variableLocation6;
    private int _variableLocation7;

    public DefaultShader() {
        super(readVertexData(), readFragmentData());
    }

    private static InputStream readVertexData() {
        return DefaultShader.class.getResourceAsStream("/src/engine/shaders/vertexShader.txt");
    }

    private static InputStream readFragmentData() {
        return DefaultShader.class.getResourceAsStream("/src/engine/shaders/fragmentShader.txt");
    }

    @Override
    protected void getAllUniformsVariables() {
        _variableLocation1 = super.getUniformVariable("transformation");
        _variableLocation2 = super.getUniformVariable("projection");
        _variableLocation3 = super.getUniformVariable("view");
        _variableLocation4 = super.getUniformVariable("lightPosition");
        _variableLocation5 = super.getUniformVariable("lightColor");
        _variableLocation6 = super.getUniformVariable("shineDamper");
        _variableLocation7 = super.getUniformVariable("reflectivity");
    }

    @Override
    protected void _bindAttr() {
        super._bind(0, "position");
        super._bind(1, "textureCoordinates");
        super._bind(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f m4x4) {
        super.loadMatrixInUniformVariable(_variableLocation1, m4x4);
    }

    public void loadProjectionMatrix(Matrix4f m4x4) {
        super.loadMatrixInUniformVariable(_variableLocation2, m4x4);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f view = MatrixUtils.createViewMatrix(camera);
        super.loadMatrixInUniformVariable(_variableLocation3, view);
    }

    public void loadLight(Lightsource light) {
        super.loadVectorInUniformVariable(_variableLocation4, light.getPosition());
        super.loadVectorInUniformVariable(_variableLocation5, light.getColor());
    }

    public void loadSpecularLights(float damperFactor, float reflectivity) {
        super.loadFloatInUniformVariable(_variableLocation6, damperFactor);
        super.loadFloatInUniformVariable(_variableLocation7, reflectivity);
    }
}

