package engine.texture;

import engine.math.RGBAf;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageProcessor {
    private ImageProcessor() {
    }

    public static void filterImage(BufferedImage image, RGBAf rgb) {
        float[] rgbChannels = rgb.getColorCodes();
        float r = rgbChannels[0] * 255;
        float g = rgbChannels[1] * 255;
        float b = rgbChannels[2] * 255;
        float a = rgbChannels[3] * 255;

        if (r > 255.0f || r < 0.0f) r = 255.0f;
        if (g > 255.0f || g < 0.0f) g = 255.0f;
        if (b > 255.0f || b < 0.0f) b = 255.0f;
        if (a > 255.0f || a < 0.0f) a = 255.0f;

        Color[] colors = new Color[image.getWidth() * image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                colors[i] = new Color(image.getRGB(i, j));
                Color newColor = new Color((int) r, (int) g, (int) b);
                image.setRGB(i, j, newColor.getRGB());
            }
        }
    }

    public static void filterImage(BufferedImage image, RGBAf rgb, SpriteSheet spriteSheet) {
        float[] rgbChannels = rgb.getColorCodes();
        float r = rgbChannels[0] * 255;
        float g = rgbChannels[1] * 255;
        float b = rgbChannels[2] * 255;
        float a = rgbChannels[3] * 255;

        if (r > 255.0f || r < 0.0f) r = 255.0f;
        if (g > 255.0f || g < 0.0f) g = 255.0f;
        if (b > 255.0f || b < 0.0f) b = 255.0f;
        if (a > 255.0f || a < 0.0f) a = 255.0f;

        Color[] colors = new Color[spriteSheet.getTile().width * spriteSheet.getTile().height];

        for (int i = 0; i < spriteSheet.getTile().width; i++) {
            for (int j = 0; j < spriteSheet.getTile().height; j++) {

                int xTarget = i + spriteSheet.getTileX() * spriteSheet.getTile().width;
                int yTarget = j + spriteSheet.getTileY() * spriteSheet.getTile().height;

                colors[i] = new Color(image.getRGB(xTarget, yTarget));
                Color newColor = new Color((int) r, (int) g, (int) b);
                image.setRGB(xTarget, yTarget, newColor.getRGB());
            }
        }
    }
}
