package com.bdv.game;

import com.bdv.api.BdvScript;
import com.bdv.exceptions.OpenGLException;
import com.bdv.renders.opengl.OpenGLManager;
import com.bdv.renders.swing.RenderManager;
import com.bdv.exceptions.InvalidInstance;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class Game {
    // @TODO - Treat all the components that are being silently created insided nested components, because the ECS system is not aware of them (e.g getSubImage)
    private final BdvScript script;
    private static final Logger log = Logger.getLogger(Game.class.getName());

    public Game(BdvScript script) {
        this.script = script;
        try {
            this.loop();
        } catch (NoSuchMethodException |
                IllegalAccessException |
                InstantiationException |
                InvalidInstance |
                InvocationTargetException |
                OpenGLException e) {
            e.printStackTrace();
        }
    }

    public void loop() throws InvocationTargetException, NoSuchMethodException, InstantiationException, InvalidInstance, IllegalAccessException, OpenGLException {
        this.script.init();
        switch (this.script.rendererAPI) {
            case OPENGL_RENDERER:
                openglRender();
                break;
            case SWING_RENDERER:
            default:
                swingRender();
                break;
        }
    }

    private void swingRender() {
        RenderManager renderManagerComponent = new RenderManager(script);
        renderManagerComponent.frame.setResizable(false);
        renderManagerComponent.frame.setTitle(this.script.windowTitle);
        renderManagerComponent.frame.add(renderManagerComponent);
        renderManagerComponent.frame.pack();
        renderManagerComponent.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderManagerComponent.frame.setLocationRelativeTo(null);
        renderManagerComponent.frame.setVisible(true);
        renderManagerComponent.start();
    }

    private void openglRender() throws OpenGLException {
        // @TODO - Make OpenGL multi-threaded as well (once I get it working in the first place)
        OpenGLManager manager = new OpenGLManager(this.script);
        manager.loop();
    }
}
