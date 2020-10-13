package com.bdv.helpers;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferOperations {
    public static FloatBuffer convertFloatToFloatBuffer(float[] array) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array);
        // Preparing for get operations
        buffer.flip();

        return buffer;
    }

    public static IntBuffer convertIntToIntBuffer(int[] array) {
        IntBuffer buffer = BufferUtils.createIntBuffer(array.length);
        buffer.put(array);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFloatBuffer(int size) {
        return BufferUtils.createFloatBuffer(size);
    }
}
