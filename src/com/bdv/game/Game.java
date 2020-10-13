package com.bdv.game;

import com.bdv.api.BdvScript;
import com.bdv.components.OpenGLRenderManagerComponent;

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

    }

    private void openglRender() {
        OpenGLRenderManagerComponent.createRender(this.script.getWidth(), this.script.getHeight(), this.script.getWindowTitle());

    }
}
