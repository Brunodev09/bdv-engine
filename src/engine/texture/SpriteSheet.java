package engine.texture;

import java.awt.*;

public class SpriteSheet {
    private final String file;
    private final Rectangle tile;
    private final int tileX;
    private final int tileY;
    private Rectangle fullImageSize;

    public SpriteSheet(String file, Rectangle tile, int tileX, int tileY) {
        this.file = file;
        this.tile = tile;
        this.tileX = tileX;
        this.tileY= tileY;
    }

    public SpriteSheet(String file, Rectangle tile, int tileX, int tileY, Rectangle fullImageSize) {
        this.file = file;
        this.tile = tile;
        this.tileX = tileX;
        this.tileY= tileY;
        this.fullImageSize = fullImageSize;
    }

    public String getFile() {
        return file;
    }

    public Rectangle getTile() {
        return tile;
    }

    public int getTileY() {
        return tileY;
    }

    public int getTileX() {
        return tileX;
    }

    public Rectangle getFullImageSize() {
        return fullImageSize;
    }

    public void setFullImageSize(Rectangle fullImageSize) {
        this.fullImageSize = fullImageSize;
    }
}
