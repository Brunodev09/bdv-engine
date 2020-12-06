package com.bdv.renders.opengl.shaders;

import com.bdv.components.CameraComponent;
import com.bdv.renders.opengl.helpers.MatrixUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.io.InputStream;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class RectangleShader extends Shader {

    private int variableLocation1;
    private int variableLocation2;
    private int variableLocation3;
    private int variableLocation4;
    private int variableLocation5;
    private int variableLocation6;

    public RectangleShader() {
        super(readVertexData(), readFragmentData());
    }

    private static InputStream readVertexData() {
        return RectangleShader.class.getResourceAsStream("/shaders/vertex2d.txt");
    }

    private static InputStream readFragmentData() {
        return RectangleShader.class.getResourceAsStream("/shaders/fragment2d.txt");
    }

    @Override
    protected void getAllUniformsVariables() {
        variableLocation1 = super.getUniformVariable("transformation");
        variableLocation2 = super.getUniformVariable("projection");
        variableLocation3 = super.getUniformVariable("view");
        variableLocation4 = super.getUniformVariable("tick");
        variableLocation5 = super.getUniformVariable("debugShader");
        variableLocation6 = super.getUniformVariable("isPlayer");
    }

    @Override
    protected void bindAttributes() {
        super.bind(0, "position");
        super.bind(1, "textureCoordinates");
        super.bind(2, "vertexColor");
    }

    public void loadTransformationMatrix(Matrix4f m4x4) {
        super.loadMatrixInUniformVariable(variableLocation1, m4x4);
    }

    public void loadProjectionMatrix(Matrix4f m4x4) {
        super.loadMatrixInUniformVariable(variableLocation2, m4x4);
    }

    public void loadViewMatrix(CameraComponent camera) {
        Matrix4f view = MatrixUtils.createViewMatrix(camera);
        super.loadMatrixInUniformVariable(variableLocation3, view);
    }

    public void loadCurrentTimeFlow() {
        super.loadFloatInUniformVariable(variableLocation4, (float) glfwGetTime());
    }

    public void loadDebugToggle(boolean toggle) {
        super.loadBinaryInUniformVariable(variableLocation5, toggle);
    }

    public void loadIsPlayer(boolean player) {
        super.loadBinaryInUniformVariable(variableLocation6, player);
    }

}
