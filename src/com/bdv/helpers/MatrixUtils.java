package com.bdv.helpers;

import com.bdv.components.OpenGLCamera2DComponent;
import com.bdv.components.OpenGLCameraComponent;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

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

    public static Matrix4f createTransformationMatrix(org.lwjgl.util.vector.Vector3f translation, float rX, float rY,
                                                      float rZ, float sX, float sY, float sZ) {
        Matrix4f matrix = new Matrix4f();

        matrix.setIdentity();

        Matrix4f.translate(translation, matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(rX), new org.lwjgl.util.vector.Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rY), new org.lwjgl.util.vector.Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rZ), new org.lwjgl.util.vector.Vector3f(0, 0, 1), matrix, matrix);

        Matrix4f.scale(new org.lwjgl.util.vector.Vector3f(sX, sY, sZ), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createProjectionMatrix(int w, int h, float fov, float np, float fp) {
        Matrix4f matrix = new Matrix4f();

        float ratio = (float) w / (float) h;
        float scaleY = (1f / (float) Math.tan(Math.toRadians(fov / 2f))) * ratio;
        float scaleX = scaleY / ratio;
        float frustumLength = fp - np;

        matrix.m00 = scaleX;
        matrix.m11 = scaleY;
        matrix.m22 = -((fp + np) / frustumLength);
        matrix.m23 = -1;
        matrix.m32 = -((2 * np * fp) / frustumLength);
        matrix.m33 = 0;

        return matrix;
    }

    public static Matrix4f createOrthographicMatrix(int w, int h) {
        Matrix4f ortho = new Matrix4f();
        ortho.setIdentity();

        float zNear = 0.01f;
        float zFar = 100f;

        ortho.m00 = 2 / (float) w;
        ortho.m11 = 2 / -(float) h;
        ortho.m22 = -2 / (zFar - zNear);

        return ortho;
    }

    public static Matrix4f createViewMatrix(OpenGLCameraComponent camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new org.lwjgl.util.vector.Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new org.lwjgl.util.vector.Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        org.lwjgl.util.vector.Vector3f pos = camera.getPosition();
        org.lwjgl.util.vector.Vector3f negativePos = new org.lwjgl.util.vector.Vector3f(-pos.x, -pos.y, -pos.z);
        Matrix4f.translate(negativePos, viewMatrix, viewMatrix);

        return viewMatrix;
    }

    public static Matrix4f createViewMatrix(OpenGLCamera2DComponent camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new org.lwjgl.util.vector.Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new org.lwjgl.util.vector.Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        org.lwjgl.util.vector.Vector3f pos = camera.getPosition();
        org.lwjgl.util.vector.Vector3f negativePos = new Vector3f(-pos.x, -pos.y, 0.0f);
        Matrix4f.translate(negativePos, viewMatrix, viewMatrix);

        return viewMatrix;
    }
}
