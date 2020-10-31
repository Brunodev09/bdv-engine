package com.bdv.renders.opengl;

import com.bdv.ECS.Entity;
import com.bdv.ECS.SystemManager;
import com.bdv.api.BdvScript;
import com.bdv.api.ProjectDimensionNumber;
import com.bdv.components.CameraComponent;
import com.bdv.components.OpenGLCamera2DComponent;
import com.bdv.components.OpenGLCameraComponent;
import com.bdv.exceptions.OpenGLException;
import com.bdv.renders.opengl.shaders.MeshShader;
import com.bdv.renders.opengl.shaders.RectangleShader;
import com.bdv.renders.opengl.shaders.Shader;
import com.bdv.renders.opengl.shaders.Terrain3DShader;
import com.bdv.systems.MeshRendererSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class OpenGLManager {

    private static final Logger logger = Logger.getLogger(OpenGLManager.class.getName());

    private final BdvScript script;
    private final MeshRendererSystem meshRendererSystem;
    private final List<Shader> shadersToUse = new ArrayList<>();
    private final OpenGLGraphicsPipeline pipeline = new OpenGLGraphicsPipeline();
    private final CameraComponent camera;

    private double lastTime;
    private double lastUpdate;
    private int frames = 0;

    public OpenGLManager(BdvScript script) throws OpenGLException {
        this.script = script;

        meshRendererSystem = (MeshRendererSystem) this.script.manager.getSystem(MeshRendererSystem.class);

        if (this.script.projectDimensionNumber == ProjectDimensionNumber.threeDimensions) {
            shadersToUse.add(new Terrain3DShader());
            shadersToUse.add(new MeshShader());
            camera = new OpenGLCameraComponent();
        } else {
            shadersToUse.add(new RectangleShader());
            camera = new OpenGLCamera2DComponent();
        }

        OpenGLRenderManager.createRender(
                this.script.width,
                this.script.height,
                this.script.windowTitle,
                shadersToUse);
        this.loop();
    }

    public void loop() {
        OpenGLRenderManager.toggleDebugShaderMode(this.script.debugShader);

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
            script.update(frames);

            for (Entity entity : meshRendererSystem.getEntities()) {

            }

            OpenGLRenderManager.renderBatch(camera);
            OpenGLRenderManager.updateRender(script.fps);

            // Freeing memory
            if (script.inputAPI != null) {
                script.inputAPI.destroy();
            }

            OpenGLRenderManager.runCollector();
            pipeline.runCollector();
            OpenGLRenderManager.closeRender();
        }
    }

    public static float currentTimeMillis() {
        return (float) glfwGetTime() * 1000;
    }
}
