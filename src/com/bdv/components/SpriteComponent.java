package com.bdv.components;

import com.bdv.helpers.MatrixUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteComponent {

    public BufferedImage image;

    //  The TYPE_INT_ARGB represents Color as an int (4 bytes, 32bits) with alpha channel in bits 24-31,
    //  red channels in 16-23, green in 8-15 and blue in 0-7.
    public int[] pixels;
    private int[] ogpixels;

    private int width;
    private int height;

    // Used as mask to eliminate the leftover 24 bits from a 32bits integer as 0-255 is within the range of 8bits.
    private final int EIGHT_BIT_MASK = 0xff;

    public enum effect {NORMAL, SEPIA, REDISH, GRAYSCALE, NEGATIVE, DECAY}

    private final float[][] id =
                    {{1.0f, 0.0f, 0.0f},
                    {0.0f, 1.0f, 0.0f},
                    {0.0f, 0.0f, 1.0f},
                    {0.0f, 0.0f, 0.0f}};

    private final float[][] negative =
                    {{1.0f, 0.0f, 0.0f},
                    {0.0f, 1.0f, 0.0f},
                    {0.0f, 0.0f, 1.0f},
                    {0.0f, 0.0f, 0.0f}};

    private final float[][] decay =
                    {{0.000f, 0.333f, 0.333f},
                    {0.333f, 0.000f, 0.333f},
                    {0.333f, 0.333f, 0.000f},
                    {0.000f, 0.000f, 0.000f}};

    private final float[][] sepia =
                    {{0.393f, 0.349f, 0.272f},
                    {0.769f, 0.686f, 0.534f},
                    {0.189f, 0.168f, 0.131f},
                    {0.000f, 0.000f, 0.000f}};

    private final float[][] redish =
                    {{1.0f, 0.0f, 0.0f},
                    {0.0f, 0.3f, 0.0f},
                    {0.0f, 0.0f, 0.3f},
                    {0.0f, 0.0f, 0.0f}};

    private final float[][] grayscale =
                    {{0.333f, 0.333f, 0.333f},
                    {0.333f, 0.333f, 0.333f},
                    {0.333f, 0.333f, 0.333f},
                    {0.000f, 0.000f, 0.000f}};

    private float[][] currentEffect = id;

    public SpriteComponent(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        ogpixels = image.getRGB(0, 0, width, height, ogpixels, 0, width);
        pixels = ogpixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void saveColors() {
        pixels = image.getRGB(0, 0, width, height, pixels, 0, width);
        currentEffect = id;
    }

    public void restoreColors() {
        image.setRGB(0, 0, width, height, pixels, 0, width);
    }

    public void restoreDefault() {
        image.setRGB(0, 0, width, height, ogpixels, 0, width);
    }

    public Color hexToColor(String color) {
        return new Color(
                Integer.valueOf(color.substring(1, 3), 16),
                Integer.valueOf(color.substring(3, 5), 16),
                Integer.valueOf(color.substring(5, 7), 16));
    }

    public void setContrast(float value) {
        float[][] effect = id;
        float contrast = (259 * (value + 255)) / (255 * (259 - value));
        for (int i = 0; i < 3; i++) {
            if (i < 3)
                effect[i][i] = contrast;
            effect[3][i] = 128 * (1 - contrast);
        }

        addEffect(effect);
    }

    public void setBrightness(float value) {
        float[][] effect = id;
        for (int i = 0; i < 3; i++)
            effect[3][i] = value;

        addEffect(effect);
    }

    public void setEffect(effect e) {
        float[][] effect;
        switch (e) {
            case SEPIA:
                effect = sepia;
                break;
            case REDISH:
                effect = redish;
                break;
            case GRAYSCALE:
                effect = grayscale;
                break;
            case NEGATIVE:
                effect = negative;
                break;
            case DECAY:
                effect = decay;
                break;
            default:
                effect = id;
        }

        if (effect != currentEffect) {
            addEffect(effect);
        }
    }

    private void addEffect(float[][] effect) {
        float[][] rgb = new float[1][4];
        float[][] xrgb;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = pixels[x + y * width];

                // Bit-packing the effect for each pixel with bit-shifting and masks
                // alpha
                int a = (p >> 24) & EIGHT_BIT_MASK;

                // red channel
                rgb[0][0] = (p >> 16) & EIGHT_BIT_MASK;
                // green channel
                rgb[0][1] = (p >> 8) & EIGHT_BIT_MASK;
                // blue channel
                rgb[0][2] = (p) & EIGHT_BIT_MASK;

                // for matrix multiplication purposes (homogenous coordinate)
                rgb[0][3] = 1f;

                xrgb = MatrixUtils.Multiplication(rgb, effect);

                for (int i = 0; i < 3; i++) {
                    if (xrgb[0][i] > 255) rgb[0][i] = 255;
                    else if (xrgb[0][i] < 0) rgb[0][i] = 0;
                    else rgb[0][i] = xrgb[0][i];
                }

                // setting each new transformed color channel back into the pixel bit
                p = (a << 24) | ((int) rgb[0][0] << 16) | ((int) rgb[0][1] << 8) | (int) rgb[0][2];
                image.setRGB(x, y, p);
            }
        }
        currentEffect = effect;
    }

    public SpriteComponent getSubimage(int x, int y, int w, int h) {
        return new SpriteComponent(image.getSubimage(x, y, w, h));
    }

    public SpriteComponent getNewSubimage(int x, int y, int w, int h) {
        BufferedImage temp = image.getSubimage(x, y, w, h);
        BufferedImage newImage = new BufferedImage(image.getColorModel(), image.getRaster().createCompatibleWritableRaster(w, h), image.isAlphaPremultiplied(), null);
        temp.copyData(newImage.getRaster());
        return new SpriteComponent(newImage);
    }

    public SpriteComponent getNewSubimage() {
        return getNewSubimage(0, 0, this.width, this.height);
    }

}
