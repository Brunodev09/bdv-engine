package com.bdv.components;

import com.bdv.ECS.Component;
import com.bdv.exceptions.ComponentException;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class SpriteSheetComponent extends Component<SpriteSheetComponent> {

    private SpriteComponent SPRITESHEET = null;
    private SpriteComponent[][] spriteArray;
    private final int TILE_SIZE = 32;
    public int width;
    public int height;
    private int wSprite;
    private int hSprite;

    public static FontComponent currentFont;

    public SpriteSheetComponent(SpriteComponent sprite, int width, int height) throws ComponentException {
        this.width = width;
        this.height = height;

        SPRITESHEET = sprite;

        wSprite = SPRITESHEET.image.getWidth() / width;
        hSprite = SPRITESHEET.image.getHeight() / height;
        loadSpriteArray();
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(int i) {
        width = i;
        wSprite = SPRITESHEET.image.getWidth() / width;
    }

    public void setHeight(int i) {
        height = i;
        hSprite = SPRITESHEET.image.getHeight() / height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getRows() { return hSprite; }
    public int getCols() { return wSprite; }
    public int getTotalTiles() { return wSprite * hSprite; }

    public void loadSpriteArray() throws ComponentException {
        spriteArray = new SpriteComponent[hSprite][wSprite];

        for (int y = 0; y < hSprite; y++) {
            for (int x = 0; x < wSprite; x++) {
                spriteArray[y][x] = getSprite(getClass().getName() + "_SPRITESHEET_" + x + "_" + y, x, y);
            }
        }
    }

    public void setEffect(SpriteComponent.effect e) {
        SPRITESHEET.setEffect(e);
    }

    public SpriteComponent getSpriteSheet() {
        return SPRITESHEET;
    }

    public SpriteComponent getSprite(String id, int x, int y) throws ComponentException {
        return SPRITESHEET.getSubimage(id, x * width, y * height, width, height);
    }

    public SpriteComponent getNewSprite(String id, int x, int y) throws ComponentException {
        return SPRITESHEET.getNewSubimage(id, x * width, y * height, width, height);
    }

    public SpriteComponent getSprite(String id, int x, int y, int w, int h) throws ComponentException {
        return SPRITESHEET.getSubimage(id, x * w, y * h, w, h);
    }

    public SpriteComponent[] getSpriteArrayAtIndex(int i) {
        return spriteArray[i];
    }

    public SpriteComponent[][] getSpriteArray() {
        return spriteArray;
    }

    public static void drawArray(Graphics2D g, List<SpriteComponent> img, Vector2f pos, int width, int height, int xOffset, int yOffset) {
        float x = pos.x;
        float y = pos.y;

        for (SpriteComponent spriteComponent : img) {
            if (spriteComponent != null) {
                g.drawImage(spriteComponent.image, (int) x, (int) y, width, height, null);
            }

            x += xOffset;
            y += yOffset;
        }
    }

    public static void drawArray(Graphics2D g, String word, Vector2f pos, int size) {
        drawArray(g, currentFont, word, pos, size, size, size, 0);
    }

    public static void drawArray(Graphics2D g, String word, Vector2f pos, int size, int xOffset) {
        drawArray(g, currentFont, word, pos, size, size, xOffset, 0);
    }

    public static void drawArray(Graphics2D g, String word, Vector2f pos, int width, int height, int xOffset) {
        drawArray(g, currentFont, word, pos, width, height, xOffset, 0);
    }

    public static void drawArray(Graphics2D g, FontComponent f, String word, Vector2f pos, int size, int xOffset) {
        drawArray(g, f, word, pos, size, size, xOffset, 0);
    }

    public static void drawArray(Graphics2D g, FontComponent f, String word, Vector2f pos, int width, int height, int xOffset, int yOffset) {
        float x = pos.x;
        float y = pos.y;

        currentFont = f;

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != 32)
                g.drawImage(f.getLetter(word.charAt(i)), (int) x, (int) y, width, height, null);

            x += xOffset;
            y += yOffset;
        }

    }
}
