package com.bdv.renders.opengl;

import com.bdv.ECS.Entity;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TransformComponent;
import com.bdv.renders.opengl.helpers.RectangularTextureCoordinates;

import java.util.*;
import java.util.List;

public class OpenGLPolygonMeshGenerator {
    public final float[] mesh;
    public final int[] indexes;
    public final float[] textureCoordinates;
    public float[] colorPointer;

    private static final int numberOfCoordinatesPerPoint = 3;
    private static final int numberOfPointsPerSquare = 4;
    private final int tilesPerRow;

    private final int width;
    private final int height;
    private final int tx;
    private final int ty;

    public OpenGLPolygonMeshGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.tx = width;
        this.ty = height;
        this.tilesPerRow = 1;

        float startX = 0;
        float startY = 0;

        mesh = new float[]{
                startX, startY, 0,
                startX + tx, startY, 0,
                startX + tx, startY + ty, 0,
                startX, startY + ty, 0
        };

        indexes = new int[]{
                0, 1, 3,
                3, 1, 2,
        };

        textureCoordinates = new float[]{
                1, 0,
                0, 0,
                0, 1,
                1, 1,
        };

        colorPointer = new float[numberOfCoordinatesPerPoint * numberOfPointsPerSquare];
    }

    public OpenGLPolygonMeshGenerator(List<Entity> entityList,
                                      float[][][] effects,
                                      int width,
                                      int height,
                                      float tileSizeX,
                                      float tileSizeY) {
        // Self explanatory
        int numberOfTiles = (width / (int) tileSizeX) * (height / (int) tileSizeY);

        this.width = width;
        this.height = height;
        this.tx = (int) tileSizeX;
        this.ty = (int) tileSizeY;

        // Number of squares to be built before jumping to the next row
        tilesPerRow = width / (int) tileSizeX;
        mesh = new float[numberOfTiles * numberOfCoordinatesPerPoint * numberOfPointsPerSquare];

        // To index 2 triangles we need 6 points (5 actually, since one is reused)
        indexes = new int[numberOfTiles * 6];

        textureCoordinates = new float[numberOfTiles * 8];
        colorPointer = new float[numberOfTiles * 3 * numberOfPointsPerSquare];

        float startX = 0;
        float startY = 0;
        int tilesInRow = 0;

        // 12 coordinates for 4 points per square tile
        // Building each square with 2 right triangles
        for (int i = 0; i < mesh.length - 11; i += 12) {
            float[] points = new float[]{
                    startX, startY, 0,
                    startX + tileSizeX, startY, 0,
                    startX + tileSizeX, startY - tileSizeY, 0,
                    startX, startY - tileSizeY, 0
            };
            for (int j = 0; j < points.length; j++) {
                mesh[i + j] = points[j];
            }
            if (tilesInRow == tilesPerRow) {
                startX += tileSizeX;
                startY = 0;
                tilesInRow = 0;
            } else {
                startY -= tileSizeY;
                tilesInRow++;
            }
        }

        // Mapping the bitboard of rgb effects into a single-dimensional array
        int counter = 0;
        if (effects.length > 0) {
            int cc = 0;
            while (cc < colorPointer.length) {
                colorPointer[cc] = 1.0f;
                cc++;
            }
        }
        int itt = 0;
        for (int x = 0; x < effects.length; x++) {
            for (int y = 0; y < effects[x].length; y++) {
                float r = effects[x][y][0];
                float g = effects[x][y][1];
                float b = effects[x][y][2];

                while (counter < (numberOfPointsPerSquare)) {
                    if (itt >= colorPointer.length) break;
                    colorPointer[itt] = r;
                    colorPointer[itt + 1] = g;
                    colorPointer[itt + 2] = b;
                    counter++;
                    itt += 3;
                }
                counter = 0;
            }
        }

        // Indexing each square (2 triangles) into the mesh
        int it = 0;
        for (int i = 0; i < indexes.length - 5; i += 6) {
            indexes[i] = it;
            indexes[i + 1] = it + 1;
            indexes[i + 2] = it + 3;
            indexes[i + 3] = it + 3;
            indexes[i + 4] = it + 1;
            indexes[i + 5] = it + 2;
            it += 4;
        }

        // Assembling the textures into the mesh
        // Coordinates (x,y) from the texture for each point of the rectangle, that makes 8 coordinates (4 points) of texture coordinates for each square tile
        SpriteComponent[] spriteComponents = extractSprites(entityList);

        int textureRunner = 0;
        float wFactor = 0;
        float hFactor = 0;
        int iterationsDoneForSprite = 0;

        RectangularTextureCoordinates<Integer> subImagePosition = null;
        List<RectangularTextureCoordinates<Float>> subRectanglesInSubImage = new ArrayList<>();

        for (int i = 0; i < textureCoordinates.length - 7; i += 8) {
            if (iterationsDoneForSprite >= Math.max(wFactor, hFactor)) {
                iterationsDoneForSprite = 0;
                subRectanglesInSubImage.clear();

                if (textureRunner >= spriteComponents.length) {
                    subImagePosition = OpenGLTextureProcessor.getDefaultTexture();
                    wFactor = 256 / tileSizeX;
                    hFactor = 256 / tileSizeY;
                }
                else {
                    subImagePosition = OpenGLTextureProcessor.texturesById.get(spriteComponents[textureRunner].textureId);
                    wFactor = spriteComponents[textureRunner].getWidth() / tileSizeX;
                    hFactor = spriteComponents[textureRunner].getHeight() / tileSizeY;
                }

                int splitCounter = 1;

                float x0 = subImagePosition.x / wFactor;
                float y0 = subImagePosition.y / hFactor;

                float x1 = subImagePosition.x2 / wFactor;
                float y1 = subImagePosition.y2 / hFactor;

                float x2 = subImagePosition.x3 / wFactor;
                float y2 = subImagePosition.y3 / hFactor;

                float x3 = subImagePosition.x4 / wFactor;
                float y3 = subImagePosition.y4 / hFactor;

                while (splitCounter <= Math.max(wFactor, hFactor)) {
                    float offsetX = splitCounter > wFactor ? wFactor : splitCounter;
                    float offsetY = splitCounter > hFactor ? hFactor : splitCounter;
                    subRectanglesInSubImage.add(new RectangularTextureCoordinates<>(
                            x0 + (offsetX * tileSizeX),
                            y0 + (offsetY * tileSizeY),
                            x1 + (offsetX * tileSizeX),
                            y1 + (offsetY * tileSizeY),
                            x2 + (offsetX * tileSizeX),
                            y2 + (offsetY * tileSizeY),
                            x3 + (offsetX * tileSizeX),
                            y3 + (offsetY * tileSizeY)));
                    splitCounter++;
                }
            }

            int masterCanvasWidth = OpenGLTextureProcessor.getWidth();
            int masterCanvasHeight = OpenGLTextureProcessor.getHeight();

            textureCoordinates[i] = subRectanglesInSubImage.get(iterationsDoneForSprite).x / masterCanvasWidth;
            textureCoordinates[i + 1] = subRectanglesInSubImage.get(iterationsDoneForSprite).y / masterCanvasHeight;
            textureCoordinates[i + 2] = subRectanglesInSubImage.get(iterationsDoneForSprite).x2 / masterCanvasWidth;
            textureCoordinates[i + 3] = subRectanglesInSubImage.get(iterationsDoneForSprite).y2 / masterCanvasHeight;
            textureCoordinates[i + 4] = subRectanglesInSubImage.get(iterationsDoneForSprite).x3 / masterCanvasWidth;
            textureCoordinates[i + 5] = subRectanglesInSubImage.get(iterationsDoneForSprite).y3 / masterCanvasHeight;
            textureCoordinates[i + 6] = subRectanglesInSubImage.get(iterationsDoneForSprite).x4 / masterCanvasWidth;
            textureCoordinates[i + 7] = subRectanglesInSubImage.get(iterationsDoneForSprite).y4 / masterCanvasHeight;

            iterationsDoneForSprite++;
            textureRunner++;
        }
    }

    // Extract sprites in row-order from top-left to bottom-right according to the positions on the Transformation component of each entity
    public SpriteComponent[] extractSprites(List<Entity> entityList) {
        List<TransformComponent> transformComponentsList = new ArrayList<>();
        List<SpriteComponent> spriteComponents = new ArrayList<>();

        for (Entity entity : entityList) {
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
            if (transformComponent != null) {
                transformComponentsList.add(transformComponent);
            }
        }
        transformComponentsList.sort(new TransformSort());

        for (TransformComponent transformComponent : transformComponentsList) {
            for (Entity entity : entityList) {
                TransformComponent transformComponentFromEntity = entity.getComponent(TransformComponent.class);
                if (transformComponentFromEntity != null && transformComponentFromEntity.equals(transformComponent)) {
                    spriteComponents.add(entity.getComponent(SpriteComponent.class));
                }
            }
        }

        SpriteComponent[] spriteComponentsArray = new SpriteComponent[spriteComponents.size()];

        return spriteComponents.toArray(spriteComponentsArray);
    }

    static class TransformSort implements Comparator<TransformComponent> {
        public int compare(TransformComponent a, TransformComponent b) {
            int weightA = 0;
            int weightB = 0;
            if (a.position.x == b.position.x && a.position.y == b.position.y) {
                // if they are on the same position on the grid, decide who gets rendered based on depth
                if (a.position.z >= b.position.z) weightA += 500;
                else weightB += 500;
            }
            if (a.position.y > b.position.y) {
                weightA += 1000;
            } else if (a.position.y < b.position.y) weightB += 1000;
            if (a.position.x > b.position.x) {
                weightA += 500;
            } else if (a.position.x < b.position.x) weightB += 500;
            return weightB - weightA;
        }
    }

}
