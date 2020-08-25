package engine.core.interfaces;

import engine.math.BufferOperations;
import examples.GL_ASTAR;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Shader {

    private static final Logger LOG = Logger.getLogger(Shader.class.getName());

    private final int _id;
    private final int _vertexShaderId;
    private final int _fragmentShaderId;

    // 4x4 matrixes
    private static final FloatBuffer _floatBuffer = BufferOperations.createFloatBuffer(16);

    public Shader(File vertexShaderFile, File fragmentShaderFile) {
        _vertexShaderId = _load(vertexShaderFile, GL20.GL_VERTEX_SHADER);
        _fragmentShaderId = _load(fragmentShaderFile, GL20.GL_FRAGMENT_SHADER);
        _id = GL20.glCreateProgram();
        GL20.glAttachShader(_id, _vertexShaderId);
        GL20.glAttachShader(_id, _fragmentShaderId);
        _bindAttr();
        GL20.glLinkProgram(_id);
        GL20.glValidateProgram(_id);
        getAllUniformsVariables();
    }

    public void init() {
        GL20.glUseProgram(_id);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    protected abstract void getAllUniformsVariables();

    protected int getUniformVariable(String variableName) {
        return GL20.glGetUniformLocation(_id, variableName);
    }

    protected void loadFloatInUniformVariable(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVectorInUniformVariable(int location, Vector3f vec3) {
        GL20.glUniform3f(location, vec3.x, vec3.y, vec3.z);
    }

    protected void loadBinaryInUniformVariable(int location, boolean value) {
        float load = value ? 1 : 0;
        loadFloatInUniformVariable(location, load);
    }

    protected void loadMatrixInUniformVariable(int location, Matrix4f m4x4) {
        m4x4.store(_floatBuffer);
        _floatBuffer.flip();
        GL20.glUniformMatrix4fv(location, false, _floatBuffer);
    }

    public void runCollector() {
        stop();
        GL20.glDetachShader(_id, _vertexShaderId);
        GL20.glDetachShader(_id, _fragmentShaderId);
        GL20.glDeleteShader(_vertexShaderId);
        GL20.glDeleteShader(_fragmentShaderId);
        GL20.glDeleteProgram(_id);
    }

    protected abstract void _bindAttr();

    protected void _bind(int attr, String varName) {
        GL20.glBindAttribLocation(_id, attr, varName);
    }


    private static int _load(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);

        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            LOG.log(Level.SEVERE, GL20.glGetShaderInfoLog(shaderID, 500));
            LOG.severe("Could not compile shader program!");
            System.exit(-1);
        }

        return shaderID;
    }

    private static int _load(File file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);

        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            LOG.log(Level.SEVERE, GL20.glGetShaderInfoLog(shaderID, 500));
            LOG.severe("Could not compile shader program!");
            System.exit(-1);
        }

        return shaderID;
    }

}

