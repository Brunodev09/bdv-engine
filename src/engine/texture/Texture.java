package engine.texture;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private int width, height;
    private int texture;

    public Texture() {

    }

    public Texture(String path) {
        texture = load(path);
    }

    public Texture(String path, SpriteSheet spriteSheet) {
        texture = loadSpritesheet(path, spriteSheet);
    }

    private static Logger LOG = Logger.getLogger(Texture.class.getName());

    private int load(String path) {
        int[] pixels = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            return createTexture(image, pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO - Returning zero here is dangerous
        return 0;
    }


    private int loadSpritesheet(String path, SpriteSheet spriteSheet) {
        int[] pixels = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(path));
            width = spriteSheet.getTile().width;
            height = spriteSheet.getTile().height;
            int startX = spriteSheet.getTileX() * spriteSheet.getTile().width;
            int startY = spriteSheet.getTileY() * spriteSheet.getTile().height;
            pixels = new int[width * height];
            image.getRGB(startX, startY, width, height, pixels, 0, width);

            return createTexture(image, pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO - Returning zero here is dangerous
        return 0;
    }

    private int createTexture(BufferedImage image, int[] pixels) {
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

        TextureCache.addImage(result, image);

        return result;
    }

    public int loadTextureWithImage(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        return createTexture(image, pixels);
    }

    public int loadTextureWithImage(BufferedImage image, SpriteSheet spriteSheet) {
        width = spriteSheet.getTile().width;
        height = spriteSheet.getTile().height;
        int startX = spriteSheet.getTileX() * spriteSheet.getTile().width;
        int startY = spriteSheet.getTileY() * spriteSheet.getTile().height;
        int[] pixels = new int[width * height];
        image.getRGB(startX, startY, width, height, pixels, 0, width);

        return createTexture(image, pixels);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTextureID() {
        return texture;
    }

}

