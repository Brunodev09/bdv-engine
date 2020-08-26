package engine.shaders;

import engine.core.interfaces.Shader;
import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.math.MatrixUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.InputStream;

public class RectangleShader extends Shader {

    private int _variableLocation1;
    private int _variableLocation2;
    private int _variableLocation3;
    private int _variableLocation4;
    private int _variableLocation5;

    public RectangleShader() {
        super(readVertexData(), readFragmentData());
    }

    private static InputStream readVertexData() {
        return DefaultShader.class.getResourceAsStream("/src/engine/shaders/vertex2d.txt");
    }

    private static InputStream readFragmentData() {
        return DefaultShader.class.getResourceAsStream("/src/engine/shaders/fragment2d.txt");
    }

    @Override
    protected void getAllUniformsVariables() {
        _variableLocation1 = super.getUniformVariable("transformation");
        _variableLocation2 = super.getUniformVariable("projection");
        _variableLocation3 = super.getUniformVariable("view");
        _variableLocation4 = super.getUniformVariable("colorOffset");
        _variableLocation5 = super.getUniformVariable("ambientLight");
    }

    @Override
    protected void _bindAttr() {
        super._bind(0, "position");
        super._bind(1, "textureCoordinates");
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
