package com.bdv.game;

import com.bdv.api.BdvScript;
import com.bdv.api.ProjectDimensionNumber;
import com.bdv.components.OpenGLRenderManagerComponent;
import com.bdv.components.RenderManagerComponent;

public class Game {
    private final BdvScript script;

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
        RenderManagerComponent renderManagerComponent = new RenderManagerComponent(script);
    }

    private void openglRender() {
        OpenGLRenderManagerComponent.createRender(this.script.getWidth(), this.script.getHeight(), this.script.getWindowTitle());

        if (this.script.getProjectDimensionNumber() == ProjectDimensionNumber.ThreeDimensions) {

        } else if (this.script.getProjectDimensionNumber() == ProjectDimensionNumber.TwoDimensions) {
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
