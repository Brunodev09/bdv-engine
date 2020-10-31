package com.bdv.renders.opengl;

import com.bdv.components.SpriteComponent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLTextureGenerator {
    private static final Logger LOG = Logger.getLogger(OpenGLTextureGenerator.class.getName());
    private OpenGLTextureGenerator() {}

    public static int generateTexture(SpriteComponent sprite) {
        int[] data = sprite.pixels;
        int result = glGenTextures();
        int texture;

        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sprite.getWidth(), sprite.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);

        texture = result;

        return texture;
    }

    public static void bind(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
