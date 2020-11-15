package com.bdv.renders.opengl;

import com.bdv.exceptions.OpenGLTextureProcessorException;
import com.bdv.renders.opengl.helpers.RectangularTextureCoordinates;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class OpenGLTextureProcessor {

    public static final Map<String, RectangularTextureCoordinates<Integer>> texturesById = new HashMap<>();

    private static BufferedImage masterCanvas;
    private static Graphics2D graphics2D;

    private static final Deque<RectangularTextureCoordinates<Integer>> textureMapper = new LinkedList<>();

    private static int width;
    private static int height;

    private OpenGLTextureProcessor() {
    }

    public static void init(int width, int height) throws OpenGLTextureProcessorException {
        if (masterCanvas != null) {
            throw new OpenGLTextureProcessorException("The master canvas can only be initialized once.");
        }
        OpenGLTextureProcessor.width = width;
        OpenGLTextureProcessor.height = height;
        masterCanvas = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        graphics2D = masterCanvas.createGraphics();
        graphics2D.setPaint(Color.PINK);
        graphics2D.fillRect(0, 0, width, height);

    }

    public static BufferedImage merge(String id, BufferedImage toMerge) throws OpenGLTextureProcessorException {
        if (masterCanvas == null) {
            throw new OpenGLTextureProcessorException("The master canvas has not been initialized.");
        }
        if (toMerge.getWidth() > masterCanvas.getWidth() || toMerge.getHeight() > masterCanvas.getHeight()) {
            throw new OpenGLTextureProcessorException("This sprite dimensions are incompatible with the master canvas.");
        }

        RectangularTextureCoordinates<Integer> latestInserted = textureMapper.peekFirst();
        if (latestInserted == null) {
            latestInserted = new RectangularTextureCoordinates<>(0, 0, 0, 0);
        }

        int x0 = latestInserted.x2;
        int y0 = latestInserted.y;
        int x1 = x0 + toMerge.getWidth();
        int y1 = y0 + toMerge.getHeight();

        // X -> Sprite1(16 px)
        // Y -> Sprite2(2 px)
        // Z -> chosen slots for Sprite3(4 slots) that we want to find free pixels on the canvas
        // 0 -> Free pixels

        // X X X X Y Y
        // X X X X Z Z
        // X X X X Z Z
        // X X X X 0 0
        // 0 0 0 0 0 0
        // 0 0 0 0 0 0

        if (x1 > masterCanvas.getWidth()) {
            // Need to go down the canvas and find somewhere free to fit new texture
            // Need to search everywhere below y1 (y0 + h)
            boolean free = false;
            int testX = toMerge.getWidth();
            int testY = y1 + toMerge.getHeight(); // use negative if y-axis grows downwards
            int conqueredNodes = 0;

            while (!free) {
                Iterator<RectangularTextureCoordinates<Integer>> it = textureMapper.iterator();
                while (it.hasNext()) {
                    RectangularTextureCoordinates<Integer> nextNode = it.next();

                    if (nextNode.x <= testX && nextNode.y4 > testY) {
                        testX += nextNode.x2;
                        if (testX + toMerge.getWidth() > masterCanvas.getWidth()) {
                            testX = toMerge.getWidth();
                            testY = nextNode.y4 + toMerge.getHeight();
                            if (testY > masterCanvas.getHeight()) {
                                // @TODO - Maybe calculate the area in pixels overflown by the sprite
                                throw new OpenGLTextureProcessorException("Cannot fit sprite into master canvas.");
                            } else if (testX > masterCanvas.getWidth() - 256 && testY >= masterCanvas.getHeight() - 256) {
                                throw new OpenGLTextureProcessorException("Cannot fit sprite into master canvas. Invading default texture pixels.");
                            }
                        }
                        break;
                    } else {
                        conqueredNodes++;
                    }
                }
                if (conqueredNodes == textureMapper.size()) {
                    free = true;
                }
            }
            textureMapper.push(new RectangularTextureCoordinates<>(testX - toMerge.getWidth(), testY - toMerge.getHeight(), testX, testY));
        } else {
            textureMapper.push(new RectangularTextureCoordinates<>(x0, y0, x1, y1));
        }

        final RectangularTextureCoordinates<Integer> current = textureMapper.peekFirst();

        if (current == null)
            throw new OpenGLTextureProcessorException("TextureMapper stack is empty. This should never happen.");

        texturesById.put(id, current);

        Color oldColor = graphics2D.getColor();
        graphics2D.setColor(oldColor);
        graphics2D.drawImage(toMerge, current.x, current.y, current.x2, current.y3, null);

        graphics2D.dispose();

        return masterCanvas;
    }

    public static RectangularTextureCoordinates<Integer> getDefaultTexture() {
        return new RectangularTextureCoordinates<>(OpenGLTextureProcessor.width - 256, OpenGLTextureProcessor.height - 256, 256, 256);
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public static BufferedImage getMasterCanvas() {
        return masterCanvas;
    }
}
