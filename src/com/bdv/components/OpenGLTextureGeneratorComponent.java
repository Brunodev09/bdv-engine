package com.bdv.components;

import com.bdv.ECS.Component;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

public class OpenGLTextureGeneratorComponent extends Component<OpenGLTextureGeneratorComponent> {
    private static Logger LOG = Logger.getLogger(OpenGLTextureGeneratorComponent.class.getName());

    private final int width, height;
    private SpriteComponent sprite = null;
    private int texture;

    public OpenGLTextureGeneratorComponent(SpriteComponent sprite) {
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.sprite = sprite;
        this.createTexture();
    }

    public int createTexture() {
        int[] data = this.sprite.pixels;
        int result = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glBindTexture(GL_TEXTURE_2D, 0);

        this.texture = result;

        return this.texture;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTextureId() {
        return texture;
    }

}
