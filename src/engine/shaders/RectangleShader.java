package engine.shaders;

import engine.core.interfaces.Shader;
import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.math.MatrixUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.InputStream;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class RectangleShader extends Shader {

    private int _variableLocation1;
    private int _variableLocation2;
    private int _variableLocation3;
    private int _variableLocation4;
    private int _variableLocation5;
    private int _variableLocation6;
    private int _variableLocation7;
    private int _variableLocation8;
    private int _variableLocation9;
    private int _variableLocation10;
    private int _variableLocation11;
    private int _variableLocation12;

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
        _variableLocation6 = super.getUniformVariable("toggleGlow");
        _variableLocation7 = super.getUniformVariable("glowColor");
        _variableLocation8 = super.getUniformVariable("toggleAmbientLight");
        _variableLocation9 = super.getUniformVariable("tick");
        _variableLocation10 = super.getUniformVariable("debugShader");
        _variableLocation11 = super.getUniformVariable("isPlayer");
        _variableLocation12 = super.getUniformVariable("colorTileset");
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

    public void loadAmbientLight(Vector3f color) {
        super.loadVectorInUniformVariable(_variableLocation5, color);
    }

    public void loadToggleGlow(boolean toggle) {
        super.loadBinaryInUniformVariable(_variableLocation6, toggle);
    }

    public void loadGlowColor(Vector3f color) {
        super.loadVectorInUniformVariable(_variableLocation7, color);
    }

    public void loadToggleAmbientLight(boolean toggle) {
        super.loadBinaryInUniformVariable(_variableLocation8, toggle);
    }

    public void loadCurrentTimeFlow() {
        super.loadFloatInUniformVariable(_variableLocation9, (float) glfwGetTime());
    }

    public void loadDebugToggle(boolean toggle) {
        super.loadBinaryInUniformVariable(_variableLocation10, toggle);
    }
    
    public void loadIsPlayer(boolean player) {
        super.loadBinaryInUniformVariable(_variableLocation11, player);
    }

    public void loadTileColors(float[] positionsAndColors) {
        super.loadFloatArrayIntoUniformVariable(_variableLocation12, positionsAndColors);
    }
}
