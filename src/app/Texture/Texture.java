package app.Texture;

import app.Math.Vector3f;
import app.Math.Vector3i;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private int width, height;
    private int texture;
    private Vector3f colorOffset;

    public Texture(String path) {
        texture = load(path);
    }

    public Texture(String path, Vector3f colorOffset) {
        this.colorOffset = colorOffset;
        texture = load(path);
    }

    public Texture(String path, SpriteSheet spriteSheet) {
        texture = loadSpritesheet(path, spriteSheet);
    }

    public Texture(String path, SpriteSheet spriteSheet, Vector3f colorOffset) {
        this.colorOffset = colorOffset;
        texture = loadSpritesheet(path, spriteSheet);
    }

    private int load(String path) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            offsetPixels(image);
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }

    private int loadSpritesheet(String path, SpriteSheet spriteSheet) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = spriteSheet.getTile().width;
            height = spriteSheet.getTile().height;
            int startX = spriteSheet.getTileX() * spriteSheet.getTile().width;
            int startY = spriteSheet.getTileY() * spriteSheet.getTile().height;
            pixels = new int[width * height];
            offsetPixels(image, spriteSheet);
            image.getRGB(startX, startY, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glBindTexture(GL_TEXTURE_2D, 0);

        return result;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void offsetPixels(BufferedImage image) {
        if (colorOffset != null) {
            if (colorOffset.x > 1.0f || colorOffset.x < 0.0f) colorOffset.x = 1.0f;
            if (colorOffset.y > 1.0f || colorOffset.y < 0.0f) colorOffset.y = 1.0f;
            if (colorOffset.z > 1.0f || colorOffset.z < 0.0f) colorOffset.z = 1.0f;
            Color[] rgb = new Color[width * height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    rgb[i] = new Color(image.getRGB(i, j));
                    Color newColor = new Color(colorOffset.x, colorOffset.y, colorOffset.z);
                    image.setRGB(i, j, newColor.getRGB());
                }
            }
        }
    }

    public void offsetPixels(BufferedImage image, SpriteSheet spriteSheet) {
        if (colorOffset != null) {
            if (colorOffset.x > 1.0f || colorOffset.x < 0.0f) colorOffset.x = 1.0f;
            if (colorOffset.y > 1.0f || colorOffset.y < 0.0f) colorOffset.y = 1.0f;
            if (colorOffset.z > 1.0f || colorOffset.z < 0.0f) colorOffset.z = 1.0f;
            Color[] rgb = new Color[width * height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int xTarget = i + spriteSheet.getTileX() * spriteSheet.getTile().width;
                    int yTarget = j + spriteSheet.getTileY() * spriteSheet.getTile().height;
                    rgb[i] = new Color(image.getRGB(xTarget, yTarget));
                    Color newColor = new Color(colorOffset.x, colorOffset.y, colorOffset.z);
                    image.setRGB(xTarget, yTarget, newColor.getRGB());
                }
            }
        }
    }
    public int getTextureID() {
        return texture;
    }

}

