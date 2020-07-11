package app.Math;

public class MatrixUtils {

    public static float[][] Multiplication(float[][] A, float[][] B) {
        int AColLength = A[0].length;
        int BRowLength = B.length;
        if (AColLength != BRowLength)
            return null;
        int mRRowLength = A.length;
        int mRColLength = B[0].length;
        float[][] mResult = new float[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {
            for (int j = 0; j < mRColLength; j++) {
                for (int k = 0; k < AColLength; k++) {
                    mResult[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return mResult;
    }

    public static float[][] CreateTranslationMatrix(float tx, float ty) {
        return new float[][]{
                {1, 0, tx},
                {0, 1, ty},
                {0, 0, 1}
        };
    }

    public static float[][] CreateRotationMatrix(float angle) {
        return new float[][]{
                {(float) Math.cos(angle), (float) Math.sin(angle), 0},
                {(float) -Math.sin(angle), (float) Math.cos(angle), 0},
                {0, 0, 1}
        };
    }
}
