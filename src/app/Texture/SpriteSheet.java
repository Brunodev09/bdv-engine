package app.Texture;

import java.awt.*;

public class SpriteSheet {
    private final String file;
    private final Rectangle tile;
    private final int tileX;
    private final int tileY;

    public SpriteSheet(String file, Rectangle tile, int tileX, int tileY) {
        this.file = file;
        this.tile = tile;
        this.tileX = tileX;
        this.tileY= tileY;
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
}
