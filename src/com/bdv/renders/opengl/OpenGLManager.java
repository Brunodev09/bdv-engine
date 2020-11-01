package com.bdv.renders.opengl;

import com.bdv.ECS.Entity;
import com.bdv.api.BdvScript;
import com.bdv.api.ProjectDimensionNumber;
import com.bdv.components.*;
import com.bdv.exceptions.OpenGLException;
import com.bdv.renders.opengl.helpers.ModelParser;
import com.bdv.renders.opengl.shaders.MeshShader;
import com.bdv.renders.opengl.shaders.RectangleShader;
import com.bdv.renders.opengl.shaders.Terrain3DShader;
import com.bdv.systems.MeshRendererSystem;
import com.bdv.systems.MeshTerrainRendererSystem;
import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class OpenGLManager {

    private static final Logger logger = Logger.getLogger(OpenGLManager.class.getName());

    private final BdvScript script;
    private final MeshRendererSystem meshRendererSystem;
    private final MeshTerrainRendererSystem meshTerrainRendererSystem;
    private final OpenGLGraphicsPipeline pipeline = new OpenGLGraphicsPipeline();
    private final CameraComponent camera;

    private double lastUpdate;
    private double lastTime;
    private int frames = 0;

    public final List<Class<?>> shadersToUse = new ArrayList<>();

    public OpenGLManager(BdvScript script) throws OpenGLException {
        this.script = script;

        if (this.script.projectDimensionNumber == ProjectDimensionNumber.threeDimensions) {
            shadersToUse.add(Terrain3DShader.class);
            shadersToUse.add(MeshShader.class);
            camera = new OpenGLCameraComponent();
        } else {
            shadersToUse.add(RectangleShader.class);
            camera = new OpenGLCamera2DComponent();
        }

        try {
            OpenGLRenderManager.createRender(
                    this.script.width,
                    this.script.height,
                    this.script.windowTitle,
                    shadersToUse,
                    this.script.manager);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new OpenGLException(e.getMessage());
        }

        meshRendererSystem = (MeshRendererSystem) this.script.manager.getSystem(MeshRendererSystem.class);
        meshTerrainRendererSystem = (MeshTerrainRendererSystem) this.script.manager.getSystem(MeshTerrainRendererSystem.class);
    }

    public void loop() {
        OpenGLRenderManager.toggleDebugShaderMode(this.script.debugShader);

        if (script.projectDimensionNumber == ProjectDimensionNumber.threeDimensions) {
            insertToVAO_3d();
        } else {
            insertToVAO_2d();
        }

        OpenGLightsourceComponent light = new OpenGLightsourceComponent(new Vector3f(300, 300, -30), new Vector3f(1, 1, 1));

        while (!OpenGLRenderManager.shouldExit()) {
            // @TODO - 2d rendering will only happen by constructing a mesh composed of all the images in the set positions and dimensions (n entities -> 1 draw call)
            // @TODO - It's important to remember that these dimensions can and will be dynamic and not restricted to a tileSize or whatever
            // @TODO - No need for 2 chunks between GUI and textures, I can use the z-axis to detect images that are not going to be drawn
            // @TODO - Sprite and Sprisheet components are going to be used universally (OpenGL and Swing will read the same images)

            if (lastUpdate == 0.0) {
                lastUpdate = currentTimeMillis();
            }

            if (currentTimeMillis() - lastUpdate >= 1000) {
                if (script.logFps) {
                    logger.info("FPS: " + frames);
                }
                frames = 0;
                lastUpdate = currentTimeMillis();
            }

            frames++;

            double thisTime = currentTimeMillis();
            double delta = thisTime - lastTime;
            lastTime = thisTime;

            script.update(delta);
            camera.move();

            for (Entity entity : meshRendererSystem.getEntities()) {
                OpenGLRenderManager.processEntity(entity);
            }

            OpenGLRenderManager.renderBatch(light, (OpenGLCameraComponent) camera);
            OpenGLRenderManager.updateRender(script.fps);
        }
        // Freeing memory
        if (script.inputAPI != null) {
            script.inputAPI.destroy();
        }

        OpenGLRenderManager.runCollector();
        pipeline.runCollector();
        OpenGLRenderManager.closeRender();
    }

    private void insertToVAO_2d() {
    }

    public void insertToVAO_3d() {
        for (Entity entity : meshRendererSystem.getEntities()) {
            ObjComponent objComponent = script.manager.getComponent(entity, ObjComponent.class);
            SpriteComponent spriteComponent = script.manager.getComponent(entity, SpriteComponent.class);

            OpenGLModel mdl = pipeline.loadDataToVAO(objComponent.data.getVertices(), objComponent.data.getTextures(),
                    objComponent.data.getNormals(), objComponent.data.getIndexes());
            OpenGLTextureCustom texture = new OpenGLTextureCustom(pipeline.loadTexture(spriteComponent));

            texture.setShineDamper(10);
            texture.setReflectivity(1);

            entity.addComponent(OpenGLTexturedModelComponent.class, mdl, texture);
        }
    }

    public static float currentTimeMillis() {
        return (float) glfwGetTime() * 1000;
    }
}
