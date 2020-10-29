package com.bdv.renders.opengl;

import com.bdv.ECS.Component;
import com.bdv.ECS.Entity;
import com.bdv.components.*;
import com.bdv.helpers.MatrixUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;

public class OpenGLRenderManager extends Component<OpenGLRenderManager> {
    private static RectangleShader rectangleShader;
    private static MeshShader meshShader;
    private static Terrain3DShader meshShaderTerrain;
    private static Matrix4f projection;
    private static final Map<OpenGLTexturedModelComponent, List<Entity>> entities = new HashMap<>();
    private static final List<OpenGLTerrainComponent> terrains = new ArrayList<>();

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private static OpenGLRendererComponent mainRenderer;
    private static OpenGLTerrainRendererComponent terrainRenderer;

    private static long window;
    private static int windowWidth;
    private static int windowHeight;
    private static GLFWErrorCallback errorCallback;

    private static boolean debugShader;

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

    public static void createRender(int WIDTH, int HEIGHT, String TITLE) {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        windowWidth = WIDTH;
        windowHeight = HEIGHT;

        if (!glfwInit()) {
            throw new RuntimeException("Cannot initialize OpenGL");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);

        if (window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwShowWindow(window);

        meshShader = new MeshShader();
        meshShaderTerrain = new Terrain3DShader();
        rectangleShader = new RectangleShader();

        GL11.glViewport(0, 0, WIDTH, HEIGHT);

    }

    public static void initAllRenders() {
        float[] codes = new float[]{0, 0, 0, 0};
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(codes[0], codes[1], codes[2], codes[3]);

        if (projection == null) projection = MatrixUtils.createProjectionMatrix(windowWidth, windowHeight, FOV, NEAR_PLANE, FAR_PLANE);
        if (mainRenderer == null) mainRenderer = new OpenGLRendererComponent(meshShader, projection);
        if (terrainRenderer == null) terrainRenderer = new OpenGLTerrainRendererComponent(meshShaderTerrain, projection);
    }

    public static void init2DRender() {
        float[] codes = new float[]{0, 0, 0, 0};
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(codes[0], codes[1], codes[2], codes[3]);
        if (projection == null) projection = MatrixUtils.createOrthographicMatrix(windowWidth, windowHeight);
        if (mainRenderer == null) mainRenderer = new OpenGLRendererComponent(rectangleShader, projection);
    }

    public static void renderBatch(OpenGLightsourceComponent light, OpenGLCameraComponent camera) {
        initAllRenders();

        meshShader.init();
        meshShader.loadLight(light);
        meshShader.loadViewMatrix(camera);
        mainRenderer.renderEntities(entities);
        meshShader.stop();

        meshShaderTerrain.init();
        meshShaderTerrain.loadLight(light);
        meshShaderTerrain.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        meshShaderTerrain.stop();

        terrains.clear();
        entities.clear();
    }

    public static void renderBatch(OpenGLCamera2DComponent camera) {
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
        meshShader.runCollector();
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
