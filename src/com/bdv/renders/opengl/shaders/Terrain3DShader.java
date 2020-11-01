package com.bdv.renders.opengl.shaders;

import com.bdv.components.CameraComponent;
import com.bdv.components.OpenGLCameraComponent;
import com.bdv.components.OpenGLightsourceComponent;
import com.bdv.renders.opengl.helpers.MatrixUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.io.InputStream;

public class Terrain3DShader extends Shader {
    private int variableLocation1;
    private int variableLocation2;
    private int variableLocation3;
    private int variableLocation4;
    private int variableLocation5;
    private int variableLocation6;
    private int variableLocation7;

    public Terrain3DShader() {
        super(readVertexData(), readFragmentData());
    }

    private static InputStream readVertexData() {
        return MeshShader.class.getResourceAsStream("/shaders/terrain3DVertexShader.txt");
    }

    private static InputStream readFragmentData() {
        return MeshShader.class.getResourceAsStream("/shaders/terrain3DFragmentShader.txt");
    }

    @Override
    protected void getAllUniformsVariables() {
        variableLocation1 = super.getUniformVariable("transformation");
        variableLocation2 = super.getUniformVariable("projection");
        variableLocation3 = super.getUniformVariable("view");
        variableLocation4 = super.getUniformVariable("lightPosition");
        variableLocation5 = super.getUniformVariable("lightColor");
        variableLocation6 = super.getUniformVariable("shineDamper");
        variableLocation7 = super.getUniformVariable("reflectivity");
    }

    @Override
    protected void bindAttributes() {
        super.bind(0, "position");
        super.bind(1, "textureCoordinates");
        super.bind(2, "normal");
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

    public void loadLight(OpenGLightsourceComponent light) {
        super.loadVectorInUniformVariable(variableLocation4, light.getPosition());
        super.loadVectorInUniformVariable(variableLocation5, light.getColor());
    }

    public void loadSpecularLights(float damperFactor, float reflectivity) {
        super.loadFloatInUniformVariable(variableLocation6, damperFactor);
        super.loadFloatInUniformVariable(variableLocation7, reflectivity);
    }
}
