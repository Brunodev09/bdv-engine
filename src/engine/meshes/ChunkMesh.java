package engine.meshes;

import engine.texture.SpriteSheet;

import java.awt.*;

public class ChunkMesh {
    private final float tileSizeX;
    private final float tileSizeY;
    private final int numberOfTiles;
    private final int numberOfCoordinatesPerPoint = 3;
    private final int numberOfPointsPerSquare = 4;
    private final int tilesPerRow;
    private SpriteSheet[] spriteSheets;

    private float[] mesh;
    private int[] indexes;
    private float[] textureCoordinates;

    private float xPos;
    private float yPos;

    private boolean shouldRender = false;

    public ChunkMesh(float tileSizeX, float tileSizeY, int tilesPerRow, int numberOfTiles, float xPos, float yPos, SpriteSheet[] sprites, boolean shouldRender) {
        this.tileSizeX = tileSizeX;
        this.tileSizeY = tileSizeY;
        this.tilesPerRow = tilesPerRow;
        this.xPos = xPos;
        this.yPos = yPos;
        this.numberOfTiles = numberOfTiles;
        this.spriteSheets = sprites;
        this.shouldRender = shouldRender;
        this.generate();
    }

    private void generate() {
        mesh = new float[numberOfTiles * numberOfCoordinatesPerPoint * numberOfPointsPerSquare];
        indexes = new int[numberOfTiles * 6];
        textureCoordinates = new float[numberOfTiles * 8];

        float startX = 0;
        float startY = 0;
        int tilesInRow = 0;

        for (int i = 0; i < mesh.length - 11; i += 12) {
            mesh[i] = startX;
            mesh[i + 1] = startY;
            mesh[i + 2] = 0;

            mesh[i + 3] = startX + tileSizeX;
            mesh[i + 4] = startY;
            mesh[i + 5] = 0;

            mesh[i + 6] = startX + tileSizeX;
            mesh[i + 7] = startY + tileSizeY;
            mesh[i + 8] = 0;

            mesh[i + 9] = startX;
            mesh[i + 10] = startY + tileSizeY;
            mesh[i + 11] = 0;

            if (tilesInRow == tilesPerRow) {
                startY += tileSizeY;
                startX = 0;
                tilesInRow = 0;
            } else {
                startX += tileSizeX;
                tilesInRow++;
            }

        }
        for (int i = 0; i < indexes.length - 5; i += 6) {
            indexes[i] = i;
            indexes[i + 1] = i + 1;
            indexes[i + 2] = i + 3;
            indexes[i + 3] = i + 3;
            indexes[i + 4] = i + 1;
            indexes[i + 5] = i + 2;
        }

        for (int i = 0; i < spriteSheets.length - 7; i+=8) {
            Rectangle subImageSize = spriteSheets[i].getTile();
            Rectangle fullImageSize = spriteSheets[i].getFullImageSize();
            float uOffset = spriteSheets[i].getTileX();
            float vOffset = spriteSheets[i].getTileY();
            float u = (float) subImageSize.width / fullImageSize.width;
            float v = (float) subImageSize.height / fullImageSize.height;

            textureCoordinates[i] = (u * uOffset);
            textureCoordinates[i + 1] = (vOffset * v);
            textureCoordinates[i + 2] = (u + (u * uOffset));
            textureCoordinates[i + 3] = (vOffset * v);
            textureCoordinates[i + 4] = (u + (u * uOffset));
            textureCoordinates[i + 5] = (v + (vOffset * v));
            textureCoordinates[i + 6] = (u * uOffset);
            textureCoordinates[i + 7] = (v + (vOffset * v));
        }
    }

    public float[] getMesh() {
        return mesh;
    }

    public int[] getIndexes() {
        return indexes;
    }

    public float[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public float getTileSizeX() {
        return tileSizeX;
    }

    public float getTileSizeY() {
        return tileSizeY;
    }

    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }
}
