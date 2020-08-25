package engine.shaders;

import engine.core.interfaces.Shader;
import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.math.MatrixUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.InputStream;

public class GeometryShader extends Shader {

    private int _variableLocation1;
    private int _variableLocation2;
    private int _variableLocation3;
    private int _variableLocation4;

    public GeometryShader() {
        super(readVertexData(), readFragmentData());
    }

    private static InputStream readVertexData() {
        return DefaultShader.class.getResourceAsStream("/src/engine/shaders/vertexShaderPrimitive.txt");
    }

    private static InputStream readFragmentData() {
        return DefaultShader.class.getResourceAsStream("/src/engine/shaders/fragmentShaderPrimitive.txt");
    }

    @Override
    protected void getAllUniformsVariables() {
        _variableLocation1 = super.getUniformVariable("transformationPrimitive");
        _variableLocation2 = super.getUniformVariable("projectionPrimitive");
        _variableLocation3 = super.getUniformVariable("viewPrimitive");
        _variableLocation4 = super.getUniformVariable("colorOffset");
    }

    @Override
    protected void _bindAttr() {
        super._bind(0, "positionPrimitive");
        super._bind(1, "textureCoordinatesPrimitive");
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

    public void loadViewMatrix(Camera2D camera) {
        Matrix4f view = MatrixUtils.createViewMatrix(camera);
        super.loadMatrixInUniformVariable(_variableLocation3, view);
    }

    public void loadColorOffset(Vector3f color) {
        super.loadVectorInUniformVariable(_variableLocation4, color);
    }

}

