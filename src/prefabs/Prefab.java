package prefabs;

public class Prefab {

    public static final float[] Cube = {
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };

    public static final float[] Square = {
            -50.5f, 50.5f, 0,
            -50.5f, -50.5f, 0,
            50.5f, -50.5f, 0,
            50.5f, 50.5f, 0,
    };

    public static final int[] SquareIndexes = {
            0, 1, 3,
            3, 1, 2
    };

//    public static final float[] SquareTextureCoordinates = {
//            0, 0,
//            0, 1,
//            1, 1,
//            1, 0
//    };

    public static final float[] SquareTextureCoordinates = {
            1, 1,
            1, 0,
            0, 0,
            0, 1,
    };


    public static float[] squareFactory(float spriteSize) {
        return new float[] {
                -spriteSize, spriteSize, 0,
                -spriteSize, -spriteSize, 0,
                spriteSize, -spriteSize, 0,
                spriteSize, spriteSize, 0,
        };
    }

    public static float[] squareFactory(float x, float y, float w, float h) {

        float halfsideW = w / 2;
        float halfsideH = h / 2;

        return new float[] {
                x + halfsideW, y + halfsideH, 0,
                x + halfsideW, y - halfsideH, 0,
                x - halfsideW, y - halfsideH, 0,
                x - halfsideW, y + halfsideH, 0,
        };
    }

}
