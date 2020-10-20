package com.bdv.components;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class FontComponent {
    private BufferedImage FONTSHEET = null;
    private BufferedImage[][] spriteArray;
    private final int TILE_SIZE = 32;
    public int width;
    public int height;
    private int wLetter;
    private int hLetter;

    public FontComponent(String file) {
        width = TILE_SIZE;
        height = TILE_SIZE;

        FONTSHEET = loadFont(file);

        wLetter = FONTSHEET.getWidth() / width;
        hLetter = FONTSHEET.getHeight() / height;
        loadFontArray();
    }

    public FontComponent(String file, int width, int height) {
        this.width = width;
        this.height = height;

        FONTSHEET = loadFont(file);

        wLetter = FONTSHEET.getWidth() / width;
        hLetter = FONTSHEET.getHeight() / height;
        loadFontArray();
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(int i) {
        width = i;
        wLetter = FONTSHEET.getWidth() / width;
    }

    public void setHeight(int i) {
        height = i;
        hLetter = FONTSHEET.getHeight() / height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private BufferedImage loadFont(String file) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(file));
        } catch (Exception e) {
            System.out.println("ERROR: could not load file: " + file);
        }

        return sprite;
    }

    public void loadFontArray() {
        spriteArray = new BufferedImage[wLetter][hLetter];

        for (int x = 0; x < wLetter; x++) {
            for (int y = 0; y < hLetter; y++) {
                spriteArray[x][y] = getLetter(x, y);
            }
        }
    }

    public BufferedImage getFontSheet() {
        return FONTSHEET;
    }

    public BufferedImage getLetter(int x, int y) {
        return FONTSHEET.getSubimage(x * width, y * height, width, height);
    }

    public BufferedImage getLetter(char letter) {
        int value = letter;

        int x = value % wLetter;
        int y = value / wLetter;
        return getLetter(x, y);
    }
}
