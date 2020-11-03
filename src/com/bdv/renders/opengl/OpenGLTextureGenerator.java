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
        int[] data = new int[sprite.getWidth() * sprite.getHeight()];

        for (int i = 0; i < sprite.getWidth() * sprite.getHeight(); i++) {
            int a = (sprite.pixels[i] & 0xff000000) >> 24;
            int r = (sprite.pixels[i] & 0xff0000) >> 16;
            int g = (sprite.pixels[i] & 0xff00) >> 8;
            int b = (sprite.pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int texture = glGenTextures();

        bind(texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sprite.getWidth(), sprite.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        unbind();

        return texture;
    }

    public static void bind(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
