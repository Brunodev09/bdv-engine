package com.bdv.renders.opengl;

import com.bdv.ECS.Component;
import com.bdv.ECS.Entity;
import com.bdv.ECS.SystemManager;
import com.bdv.components.*;
import com.bdv.exceptions.OpenGLException;
import com.bdv.renders.opengl.helpers.MatrixUtils;
import com.bdv.renders.opengl.helpers.Sync;
import com.bdv.renders.opengl.shaders.MeshShader;
import com.bdv.renders.opengl.shaders.RectangleShader;
import com.bdv.renders.opengl.shaders.Shader;
import com.bdv.renders.opengl.shaders.Terrain3DShader;
import com.bdv.systems.MeshRendererSystem;
import com.bdv.systems.MeshTerrainRendererSystem;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;

public class OpenGLRenderManager extends Component<OpenGLRenderManager> {
    public static Matrix4f projection;

    private static RectangleShader rectangleShader;
    private static MeshShader meshShader;
    private static Terrain3DShader meshShaderTerrain;

    private static List<Shader> shaders = new ArrayList<>();

    private static final Map<OpenGLTexturedModelComponent, List<Entity>> entities = new HashMap<>();
    private static final List<OpenGLTerrainComponent> terrains = new ArrayList<>();

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private static MeshRendererSystem mainRenderer;
    private static MeshTerrainRendererSystem terrainRenderer;

    private static long window;
    private static int windowWidth;
    private static int windowHeight;
    private static GLFWErrorCallback errorCallback;

    private static boolean debugShader;

    private static SystemManager sysManager;

    private static Logger LOG = Logger.getLogger(OpenGLRenderManager.class.getName());

    public static long getWindow() {
        return window;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static void createRender(int WIDTH, int HEIGHT, String TITLE, List<Class<?>> shadersList, SystemManager manager)
            throws OpenGLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        sysManager = manager;

        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        windowWidth = WIDTH;
        windowHeight = HEIGHT;

        if (!glfwInit()) {
            throw new OpenGLException("Cannot initialize OpenGL");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);

        if (window == 0) {
            throw new OpenGLException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwShowWindow(window);

        for (Class<?> shaderType : shadersList) {
            shaders.add((Shader) shaderType.getDeclaredConstructor().newInstance());
        }
        for (Shader shader : shaders) {
            if (shader instanceof MeshShader) {
                meshShader = (MeshShader) shader;
            } else if (shader instanceof Terrain3DShader) {
                meshShaderTerrain = (Terrain3DShader) shader;
            } else if (shader instanceof RectangleShader) {
                rectangleShader = (RectangleShader) shader;
            }
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void initAllRenders() {
        float[] codes = new float[]{0, 0, 0, 0};
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(codes[0], codes[1], codes[2], codes[3]);

        if (projection == null) {
            projection = MatrixUtils.createProjectionMatrix(windowWidth, windowHeight, FOV, NEAR_PLANE, FAR_PLANE);
        }
        if (mainRenderer == null) {
            mainRenderer = (MeshRendererSystem) sysManager.getSystem(MeshRendererSystem.class);
            mainRenderer.setShader(meshShader, projection);
        }
        if (terrainRenderer == null) {
            terrainRenderer = (MeshTerrainRendererSystem) sysManager.getSystem(MeshTerrainRendererSystem.class);
            if (terrainRenderer != null)
                terrainRenderer.setShader(meshShaderTerrain, projection);
        }
    }

    public static void init2DRender() {
        float[] codes = new float[]{0,1,1,1};
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(codes[0], codes[1], codes[2], codes[3]);

        if (projection == null) {
            projection = MatrixUtils.createOrthographicMatrix(windowWidth, windowHeight);
        }
        if (mainRenderer == null) {
            mainRenderer = (MeshRendererSystem) sysManager.getSystem(MeshRendererSystem.class);
            mainRenderer.setShader(rectangleShader, projection);
        }
    }

    public static void renderBatch(OpenGLightsourceComponent light, CameraComponent camera) {
        initAllRenders();

        meshShader.init();
        meshShader.loadLight(light);
        meshShader.loadViewMatrix(camera);
        mainRenderer.renderEntities(entities);
        meshShader.stop();

        if (!terrains.isEmpty()) {
            meshShaderTerrain.init();
            meshShaderTerrain.loadLight(light);
            meshShaderTerrain.loadViewMatrix(camera);
            terrainRenderer.render(terrains);
            meshShaderTerrain.stop();
            terrains.clear();
        }

        entities.clear();
    }

    public static void renderBatch(CameraComponent camera) {
        init2DRender();
        mainRenderer.setDebugShaderMode(debugShader);
        rectangleShader.init();
        rectangleShader.loadViewMatrix(camera);
        mainRenderer.renderEntities2D(entities);
        rectangleShader.stop();

        entities.clear();
    }

    public static void processEntity(Entity entity) {
        OpenGLTexturedModelComponent model = entity.getComponent(OpenGLTexturedModelComponent.class);
        List<Entity> listOfEntities = entities.get(model);
        if (listOfEntities != null) listOfEntities.add(entity);
        else {
            List<Entity> newList = new ArrayList<>();
            newList.add(entity);
            entities.put(model, newList);
        }
    }

    public static void runCollector() {
        if (meshShader != null)
            meshShader.runCollector();
        if (rectangleShader != null)
            rectangleShader.runCollector();
        if (meshShaderTerrain != null)
            meshShaderTerrain.runCollector();
    }

    public static void updateRender(int fps) {
        Sync.sync(fps);
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public static void closeRender() {
        glfwDestroyWindow(window);
    }

    public static boolean shouldExit() {
        return glfwWindowShouldClose(window);
    }

    public static void toggleDebugShaderMode(boolean toggle) {
        debugShader = toggle;
    }
}
