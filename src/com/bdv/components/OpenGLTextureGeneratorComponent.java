package com.bdv.components;

import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLTextureGeneratorComponent {
    private static Logger LOG = Logger.getLogger(OpenGLTextureGeneratorComponent.class.getName());

    private int width, height;
    private SpriteComponent spriteSheet = null;
    private SpriteComponent[][] sprites = null;

    private int eachSpriteWidth;
    private int eachSpriteHeight;

    public OpenGLTextureGeneratorComponent() {
    }

    public OpenGLTextureGeneratorComponent(String path) {
    }

    public OpenGLTextureGeneratorComponent(String path, OpenGLTextureGeneratorComponent sprite, int width, int height) {
    }

}
