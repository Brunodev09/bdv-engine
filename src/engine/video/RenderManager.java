package engine.video;

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
import engine.shaders.Terrain3DShader;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.opengl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RenderManager {
    private static GeometryShader _geoShader;
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

    private static RGBAf _cleanColor;

    private RenderManager() {

    }

    public static void createRender(int WIDTH, int HEIGHT, String TITLE, RGBAf cleanColor) {

        ContextAttribs contextAttribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), contextAttribs);
            Display.setTitle(TITLE);

            _shader = new DefaultShader();
            _shaderTerrain = new Terrain3DShader();
            _geoShader = new GeometryShader();
            _cleanColor = cleanColor;

        } catch (LWJGLException e) {
            e.printStackTrace();
        }

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

        _geoShader.init();
        _geoShader.loadViewMatrix(camera);
        mainRenderer.renderEntities2D(_entities);
        _geoShader.stop();

        _entities.clear();
    }

    public static void processEntity(Entity entity) {
        TexturedModel model = entity.getModel();
        List<Entity> listOfEntities = _entities.get(model);
        if (listOfEntities != null) listOfEntities.add(entity);
        else {
            List<Entity> newList = new ArrayList<>();
            newList.add(entity);
            _entities.put(model, newList);
        }
    }

    public static void processTerrain(Terrain tile) {
        _terrains.add(tile);
    }

    public static void runCollector() {
        _shader.runCollector();
        _shaderTerrain.runCollector();
    }

    public static void updateRender(int fps) {
        Display.sync(fps);
        Display.update();
    }

    public static void closeRender() {
        Display.destroy();
    }

    public static boolean shouldExit() {
        return Display.isCloseRequested();
    }
}
