package com.bdv.renders.opengl;

import com.bdv.ECS.Entity;
import com.bdv.components.SpriteComponent;
import com.bdv.components.TransformComponent;
import com.bdv.renders.opengl.helpers.RectangularTextureCoordinates;
import org.lwjgl.util.Dimension;

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
                    startX + tileSizeX, startY, 0,
                    startX, startY, 0,
                    startX, startY + tileSizeY, 0,
                    startX + tileSizeX, startY + tileSizeY, 0,
            };
            for (int j = 0; j < points.length; j++) {
                mesh[i + j] = points[j];
            }
            if (tilesInRow == tilesPerRow) {
                startY += tileSizeY;
                startX = 0;
                tilesInRow = 0;
            } else {
                startX += tileSizeX;
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
        Map<Integer, Boolean> filledIndexes = new HashMap<>();
        SpriteComponent[] spriteComponents = extractSprites(entityList);
        Map<SpriteComponent, List<Map<Integer, Float>>> spriteMeshes = extractIndexes(getSpriteToTransform(entityList), (width / (int) tileSizeX), tileSizeX, tileSizeY);

        for (Map.Entry<SpriteComponent, List<Map<Integer, Float>>> entry : spriteMeshes.entrySet()) {
            List<Map<Integer, Float>> value = entry.getValue();
            for (Map<Integer, Float> subKey : value) {
                for (Map.Entry<Integer, Float> subEntry : subKey.entrySet()) {
                    textureCoordinates[subEntry.getKey()] = subEntry.getValue();
                    filledIndexes.put(subEntry.getKey(), true);
                }
            }
        }

//        if (filledIndexes.size() < textureCoordinates.length) {
//
//            RectangularTextureCoordinates<Integer> subImagePosition = OpenGLTextureProcessor.getDefaultTexture();
//            List<RectangularTextureCoordinates<Float>> defPositions = getSubImagesInImage(new Dimension(256, 256), subImagePosition);
//
//            for (int i = 0; i < textureCoordinates.length - 7; i += 8) {
//                int it1 = 0;
//                int it2 = 0;
//                while (it1 < 8) {
//                    if (filledIndexes.get(i + it1) == null) {
//                        it2++;
//                    }
//                    it1++;
//                }
//                if (it2 == 8) {
//                    it1 = 1;
//                    while (it1 < 8) {
//                        // Since the default images rectangles have all the same texture we can get any of them
//                        textureCoordinates[i + it1] = defPositions.get(0).queryOrderedPoint(it1);
//                        it1++;
//                    }
//                }
//            }
//        }

//        int textureRunner = 0;
//        float wFactor = 0;
//        float hFactor = 0;
//        int iterationsDoneForSprite = 0;
//
//        RectangularTextureCoordinates<Integer> subImagePosition = null;
//        List<RectangularTextureCoordinates<Float>> subRectanglesInSubImage = new ArrayList<>();

//        for (int i = 0; i < textureCoordinates.length - 7; i += 8) {
//            if (iterationsDoneForSprite >= wFactor * hFactor) {
//                iterationsDoneForSprite = 0;
//                subRectanglesInSubImage.clear();
//
//                if (textureRunner >= spriteComponents.length) {
//                    subImagePosition = OpenGLTextureProcessor.getDefaultTexture();
//                    wFactor = 256 / tileSizeX;
//                    hFactor = 256 / tileSizeY;
//                } else {
//                    subImagePosition = OpenGLTextureProcessor.texturesById.get(spriteComponents[textureRunner].textureId);
//                    wFactor = spriteComponents[textureRunner].getWidth() / tileSizeX;
//                    hFactor = spriteComponents[textureRunner].getHeight() / tileSizeY;
//                }
//
//                float x0 = subImagePosition.x;
//                float y0 = subImagePosition.y;
//
//                float x1 = subImagePosition.x2 / wFactor;
//                float y1 = subImagePosition.y2 / hFactor;
//
//                float x2 = subImagePosition.x3 / wFactor;
//                float y2 = subImagePosition.y3 / hFactor;
//
//                float x3 = subImagePosition.x4 / wFactor;
//                float y3 = subImagePosition.y4 / hFactor;
//
//                int offsetX = 0;
//                int offsetY = 0;
//
//                while (offsetY < wFactor) {
//                    while (offsetX < hFactor) {
//                        subRectanglesInSubImage.add(new RectangularTextureCoordinates<>(
//                                x0 + (offsetX * tileSizeX),
//                                y0,
//                                x1 + (offsetX * tileSizeX),
//                                y1,
//                                x2 + (offsetX * tileSizeX),
//                                y2,
//                                x3 + (offsetX * tileSizeX),
//                                y3));
//
//                        offsetX++;
//                    }
//                    offsetX = 0;
//                    offsetY++;
//                    y0 += (offsetY * tileSizeY);
//                    y1 += (offsetY * tileSizeY);
//                    y2 += (offsetY * tileSizeY);
//                    y3 += (offsetY * tileSizeY);
//                }
//            }
//
//            int masterCanvasWidth = OpenGLTextureProcessor.getWidth();
//            int masterCanvasHeight = OpenGLTextureProcessor.getHeight();
//
//            // Texture order: counter clock wise -> ([1,0], [0,0], [0,1], [1,1])
//            textureCoordinates[i] = subRectanglesInSubImage.get(iterationsDoneForSprite).x2 / masterCanvasWidth;
//            textureCoordinates[i + 1] = subRectanglesInSubImage.get(iterationsDoneForSprite).y2 / masterCanvasHeight;
//            textureCoordinates[i + 2] = subRectanglesInSubImage.get(iterationsDoneForSprite).x / masterCanvasWidth;
//            textureCoordinates[i + 3] = subRectanglesInSubImage.get(iterationsDoneForSprite).y / masterCanvasHeight;
//            textureCoordinates[i + 4] = subRectanglesInSubImage.get(iterationsDoneForSprite).x4 / masterCanvasWidth;
//            textureCoordinates[i + 5] = subRectanglesInSubImage.get(iterationsDoneForSprite).y4 / masterCanvasHeight;
//            textureCoordinates[i + 6] = subRectanglesInSubImage.get(iterationsDoneForSprite).x3 / masterCanvasWidth;
//            textureCoordinates[i + 7] = subRectanglesInSubImage.get(iterationsDoneForSprite).y3 / masterCanvasHeight;
//
//            iterationsDoneForSprite++;
//            textureRunner++;
//        }
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

    public Map<SpriteComponent, TransformComponent> getSpriteToTransform(List<Entity> entityList) {
        Map<SpriteComponent, TransformComponent> out = new HashMap<>();

        for (Entity entity : entityList) {
            SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
            if (transformComponent != null && spriteComponent != null) {
                out.put(spriteComponent, transformComponent);
            }
        }
        return out;
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

    public Map<SpriteComponent, List<Map<Integer, Float>>> extractIndexes(Map<SpriteComponent, TransformComponent> map, int width, float tileSizeX, float tileSizeY) {

        Map<SpriteComponent, List<Map<Integer, Float>>> spriteMesh = new HashMap<>();

        for (Map.Entry<SpriteComponent, TransformComponent> entry : map.entrySet()) {

            SpriteComponent spriteComponent = entry.getKey();
            TransformComponent transformComponent = entry.getValue();

            List<RectangularTextureCoordinates<Float>> subRectanglesInSubImage;

            RectangularTextureCoordinates<Integer> subImagePositionInSheet = OpenGLTextureProcessor.texturesById.get(spriteComponent.textureId);
            subRectanglesInSubImage = getSubImagesInImage(new Dimension(spriteComponent.getWidth(), spriteComponent.getHeight()), subImagePositionInSheet);

            // Transforming screen pixel coordinate to tile coordinate
            int startX = (int) transformComponent.position.x / (int) tileSizeX;
            int startY = (int) transformComponent.position.y / (int) tileSizeY;

            List<Map<Integer, Float>> indexStruct = spriteMesh.get(spriteComponent);

            if (indexStruct == null) {
                indexStruct = new ArrayList<>();
                spriteMesh.put(spriteComponent, indexStruct);
            }

            int y = 0;
            int it = 0;
            while (y < spriteComponent.getHeight() / (int) tileSizeY) {
                int x = 0;
                while (x < spriteComponent.getWidth() / (int) tileSizeX) {
                    // Transforming from tile coordinate to 1d array coordinate
                    // (y - 1) * w + x
                    int auxY = y == 0 ? startY : startY + y;
                    int auxX = x == 0 ? startX : startX + x;
                    int indexInMeshArray = 8 * (auxY * width + auxX);

                    Map<Integer, Float> indexedMap = new HashMap<>();
                    indexStruct.add(indexedMap);

                    int innerIndex = 1;

                    while (innerIndex <= 8) {
                        indexedMap.put(indexInMeshArray + innerIndex, subRectanglesInSubImage.get(it).queryOrderedPoint(innerIndex));
                        innerIndex++;
                    }
                    x++;
                    it++;
                }
                y++;
            }
        }
        return spriteMesh;
    }

    public List<RectangularTextureCoordinates<Float>> getSubImagesInImage(Dimension dimension, RectangularTextureCoordinates<Integer> subImagePositionInSheet) {
        List<RectangularTextureCoordinates<Float>> result = new ArrayList<>();

        float wFactor = dimension.getWidth() / (float) this.tx;
        float hFactor = dimension.getHeight() / (float) this.ty;

        float x0 = subImagePositionInSheet.x / wFactor;
        float y0 = subImagePositionInSheet.y / hFactor;

        float x1 = subImagePositionInSheet.x2 / wFactor;
        float y1 = subImagePositionInSheet.y2 / hFactor;

        float x2 = subImagePositionInSheet.x3 / wFactor;
        float y2 = subImagePositionInSheet.y3 / hFactor;

        float x3 = subImagePositionInSheet.x4 / wFactor;
        float y3 = subImagePositionInSheet.y4 / hFactor;

        int offsetX = 0;
        int offsetY = 0;

        while (offsetY < wFactor) {
            while (offsetX < hFactor) {
                result.add(new RectangularTextureCoordinates<>(
                        x0 + (offsetX * this.tx),
                        y0,
                        x1 + (offsetX * this.tx),
                        y1,
                        x2 + (offsetX * this.tx),
                        y2,
                        x3 + (offsetX * this.tx),
                        y3));

                offsetX++;
            }
            offsetX = 0;
            offsetY++;
            y0 += (offsetY * this.ty);
            y1 += (offsetY * this.ty);
            y2 += (offsetY * this.ty);
            y3 += (offsetY * this.ty);
        }

        return result;
    }
}
