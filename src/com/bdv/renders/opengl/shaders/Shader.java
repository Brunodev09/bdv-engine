package com.bdv.renders.opengl.shaders;

import com.bdv.renders.opengl.helpers.BufferOperations;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Shader {

    private static final Logger LOG = Logger.getLogger(Shader.class.getName());

    private final int id;
    private final int vertexShaderId;
    private final int fragmentShaderId;

    // 4x4 matrixes
    private static final FloatBuffer floatBuffer = BufferOperations.createFloatBuffer(16);

    public Shader(InputStream vertexShaderStream, InputStream fragmentShaderStream) {
        vertexShaderId = loadShaderData(vertexShaderStream, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShaderData(fragmentShaderStream, GL20.GL_FRAGMENT_SHADER);
        id = GL20.glCreateProgram();
        GL20.glAttachShader(id, vertexShaderId);
        GL20.glAttachShader(id, fragmentShaderId);
        bindAttributes();
        GL20.glLinkProgram(id);
        GL20.glValidateProgram(id);
        getAllUniformsVariables();
    }

    public void init() {
        GL20.glUseProgram(id);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    protected abstract void getAllUniformsVariables();

    protected int getUniformVariable(String variableName) {
        return GL20.glGetUniformLocation(id, variableName);
    }

    protected void loadFloatInUniformVariable(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVectorInUniformVariable(int location, Vector3f vec3) {
        GL20.glUniform3f(location, vec3.x, vec3.y, vec3.z);
    }

    protected void loadVector2fInUniformVariable(int location, Vector2f vec2) {
        GL20.glUniform2f(location, vec2.x, vec2.y);
    }

    protected void loadBinaryInUniformVariable(int location, boolean value) {
        float load = value ? 1 : 0;
        loadFloatInUniformVariable(location, load);
    }

    protected void loadMatrixInUniformVariable(int location, Matrix4f m4x4) {
        m4x4.store(floatBuffer);
        floatBuffer.flip();
        GL20.glUniformMatrix4fv(location, false, floatBuffer);
    }

    protected void loadIntArrayIntoUniformVariable(int location, int[] data) {
        IntBuffer buffer = BufferOperations.convertIntToIntBuffer(data);
        GL20.glUniform1iv(location, buffer);
    }

    protected void loadFloatArrayIntoUniformVariable(int location, float[] data) {
        FloatBuffer buffer = BufferOperations.convertFloatToFloatBuffer(data);
        GL20.glUniform1fv(location, buffer);
    }

    public void runCollector() {
        stop();
        GL20.glDetachShader(id, vertexShaderId);
        GL20.glDetachShader(id, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(id);
    }

    protected abstract void bindAttributes();

    protected void bind(int attr, String varName) {
        GL20.glBindAttribLocation(id, attr, varName);
    }

    private static int loadShaderData(InputStream stream, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderId = GL20.glCreateShader(type);

        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            LOG.log(Level.SEVERE, GL20.glGetShaderInfoLog(shaderId, 500));
            LOG.severe("Could not compile shader program!");
            System.exit(-1);
        }

        return shaderId;
    }

}
