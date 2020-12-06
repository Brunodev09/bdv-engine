package engine.video;

import engine.api.BdvScriptGL;
import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.entities.Entity;
import engine.entities.Lightsource;
import engine.math.MatrixUtils;
import engine.math.RGBAf;
import engine.models.Terrain;
import engine.models.TexturedModel;
import engine.shaders.DefaultShader;
import engine.shaders.GeometryShader;
import engine.shaders.RectangleShader;
import engine.shaders.Terrain3DShader;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.opengl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;


public class RenderManager {
    // private static GeometryShader _geoShader;
    private static RectangleShader _geoShader;
    private static DefaultShader _shader;
    private static Terrain3DShader _shaderTerrain;
    private static Matrix4f _projection;
    private static final Map<TexturedModel, List<Entity>> _entities = new HashMap<>();
    private static final List<Terrain> _terrains = new ArrayList<>();

    private static final float _FOV = 70;
    private static final float _NEAR_PLANE = 0.1f;
    private static final float _FAR_PLANE = 1000;

    private static Renderer mainRenderer;
    private static TerrainRenderer terrainRenderer;

    private static RGBAf _cleanColor = new RGBAf(255, 255, 255, 255);

    private static long window;
    private static int windowWidth;
    private static int windowHeight;
    private static GLFWErrorCallback errorCallback;

    private static boolean debugShader;

    private static Logger LOG = Logger.getLogger(RenderManager.class.getName());

    private RenderManager() {

    }

    public static long getWindow() {
        return window;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static void createRender(int WIDTH, int HEIGHT, String TITLE, RGBAf cleanColor) {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        windowWidth = WIDTH;
        windowHeight = HEIGHT;

        if(!glfwInit()) {
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

        _shader = new DefaultShader();
        _shaderTerrain = new Terrain3DShader();
        _geoShader = new RectangleShader();

        GL11.glViewport(0, 0, WIDTH, HEIGHT);

    }

    public static void initAllRenders() {
        float[] codes = _cleanColor.getColorCodes();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(codes[0], codes[1], codes[2], codes[3]);

        if (_projection == null) _projection = MatrixUtils.createProjectionMatrix(_FOV, _NEAR_PLANE, _FAR_PLANE);
        if (mainRenderer == null) mainRenderer = new Renderer(_shader, _projection);
        if (terrainRenderer == null) terrainRenderer = new TerrainRenderer(_shaderTerrain, _projection);
    }

    public static void init2DRender() {
        float[] codes = _cleanColor.getColorCodes();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(codes[0], codes[1], codes[2], codes[3]);
        if (_projection == null) _projection = MatrixUtils.createOrthographicMatrix();
        if (mainRenderer == null) mainRenderer = new Renderer(_geoShader, _projection);
    }

    public static void renderBatch(Lightsource light, Camera camera) {
        initAllRenders();

        _shader.init();
        _shader.loadLight(light);
        _shader.loadViewMatrix(camera);
        mainRenderer.renderEntities(_entities);
        _shader.stop();

        _shaderTerrain.init();
        _shaderTerrain.loadLight(light);
        _shaderTerrain.loadViewMatrix(camera);
        terrainRenderer.render(_terrains);
        _shaderTerrain.stop();

        _terrains.clear();
        _entities.clear();
    }

    public static void renderBatch(Camera2D camera) {
        init2DRender();
        mainRenderer.setDebugShaderMode(debugShader);
        _geoShader.init();
        _geoShader.loadViewMatrix(camera);
        mainRenderer.renderEntities2D(_entities);
        _geoShader.stop();

        _entities.clear();
    }

    public static void processEntity(Entity entity) {
        TexturedModel model = entity.getModel();
        List<Entity> listOfEntities = _entities.get(model);
        if (listOfEntities != null) {
            listOfEntities.add(entity);
        }
        else {
            List<Entity> newList = new ArrayList<>();
            newList.add(entity);
            _entities.put(model, newList);
        }
    }

    public static void runCollector() {
        _shader.runCollector();
        _shaderTerrain.runCollector();
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
