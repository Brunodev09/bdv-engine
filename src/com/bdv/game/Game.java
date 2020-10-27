package com.bdv.game;

import com.bdv.api.BdvScript;
import com.bdv.api.ProjectDimensionNumber;
import com.bdv.components.OpenGLRenderManagerComponent;
import com.bdv.renders.swing.RenderManager;
import com.bdv.exceptions.InvalidInstance;
import com.bdv.systems.RenderSystem;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class Game {
    private final BdvScript script;
    private static final Logger log = Logger.getLogger(Game.class.getName());

    public Game(BdvScript script) {
        this.script = script;
        this.loop();
    }

    public void loop() {
        switch (this.script.getRendererAPI()) {
            case OPENGL_RENDERER:
                openglRender();
                break;
            default:
                swingRender();
                break;
        }
    }

    private void swingRender() {
        try {
            this.script.manager.addSystem(RenderSystem.class);
            RenderManager renderManagerComponent = new RenderManager(script);
            renderManagerComponent.frame.setResizable(false);
            renderManagerComponent.frame.setTitle(this.script.getWindowTitle());
            renderManagerComponent.frame.add(renderManagerComponent);
            renderManagerComponent.frame.pack();
            renderManagerComponent.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            renderManagerComponent.frame.setLocationRelativeTo(null);
            renderManagerComponent.frame.setVisible(true);
            renderManagerComponent.start();
        } catch (InvalidInstance | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

    }

    private void openglRender() {
        OpenGLRenderManagerComponent.createRender(this.script.getWidth(), this.script.getHeight(), this.script.getWindowTitle());

        if (this.script.getProjectDimensionNumber() == ProjectDimensionNumber.threeDimensions) {

        } else if (this.script.getProjectDimensionNumber() == ProjectDimensionNumber.twoDimensions) {
            while (!OpenGLRenderManagerComponent.shouldExit()) {
                // @TODO - 3d rendering will remain procedural (1 entity -> 1 draw call)
                // @TODO - 2d rendering will only happen by constructing a mesh composed of all the images in the set positions and dimensions (n entities -> 1 draw call)
                // @TODO - It's important to remember that these dimensions can and will be dynamic and not restricted to a tileSize or whatever
                // @TODO - No need for 2 chunks between GUI and textures, I can use the z-axis to detect images that are not going to be drawn
                // @TODO - Sprite and Sprisheet components are going to be used universally (OpenGL and Swing will read the same images)

            }
        }

    }
}
