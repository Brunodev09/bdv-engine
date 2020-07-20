package app.Templates;

import app.API.Script;
import app.Core.Interfaces.Entity;
import app.Core.Interfaces.Model;
import app.Math.Dimension;
import app.Math.RGBA;
import app.Math.Vector2f;
import app.Math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MATRIX_TEMPLATE extends Script {

    private double angle = 0;

    public MATRIX_TEMPLATE() {
        this.entities = new ArrayList<Entity>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBA(255, 20, 147, 255);
        this.windowTitle = "GRID";
        this.init(entities, resolution, background);
    }

    @Override
    public void init(List<Entity> entities, Dimension resolution, RGBA background) {
        this.entities.add(new Entity(new Vector3f(400, 400, -1), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Vector3f(200, 400, -1), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(0, 255, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Vector3f(400, 200, -1), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(0, 0, 255, 255), Model.POINT));

        this.entities.add(new Entity(new Vector3f(200, 200, -1), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 255, 255, 255), Model.POINT));


        this.entities.add(new Entity(new Vector3f(400, 400, -2), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Vector3f(200, 400, -2), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(0, 255, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Vector3f(400, 200, -2), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(0, 0, 255, 255), Model.POINT));

        this.entities.add(new Entity(new Vector3f(200, 200, -2), new Vector2f(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 255, 255, 255), Model.POINT));

    }

    @Override
    public void update() {

        angle += 0.01;
        for (Entity point : entities) {

            double[][] positionVector = new double[][]{
                    {(double) point.getInitialPosition().x},
                    {(double) point.getInitialPosition().y},
                    {(double) point.getInitialPosition().z}
            };

//        newX = centerX + (point2x-centerX)*Math.cos(x) - (point2y-centerY)*Math.sin(x);
//        newY = centerY + (point2x-centerX)*Math.sin(x) + (point2y-centerY)*Math.cos(x);

            double[][] rotation = getRotationMatrix(angle);
            double[][] translation = getTranslationMatrix(
                    -resolution.width / 2,
                    -resolution.height / 2,
                    0);
            double[][] translation2 = getTranslationMatrix(
                    resolution.width / 2,
                    resolution.height / 2,
                    0);

            double[][] translatedVector1 = matrixmult(translation, positionVector);
            double[][] rotatedPositionVector = matrixmult(rotation, translatedVector1);
            double[][] translatedVector2 = matrixmult(translation2, rotatedPositionVector);

            double distance = 2;
            double z = 1 / (distance - translatedVector2[2][0]);

            double[][] projectionMatrix = {
                    {z, 0, 0},
                    {0, z, 0},
                    {0, 0, z}
            };

            double[][] newPositionVector = matrixmult(projectionMatrix, translatedVector2);

            int x = (int) (newPositionVector[0][0]);
            int y = (int) (newPositionVector[1][0]);

            point.setPosition(new Vector3f(x, y));
        }

    }

    private double[][] getRotationMatrix(double angle) {
        return new double[][]{
                {Math.cos(angle), Math.sin(angle), 0},
                {-Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 1}
        };
    }

    private double[][] getTranslationMatrix(int tx, int ty, int tz) {
        return new double[][]{
                {1, 0, tx},
                {0, 1, ty},
                {0, 0, tz}
        };
    }

    public static double[][] matrixmult(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if (m1ColLength != m2RowLength)
            return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {         // rows from m1
            for (int j = 0; j < mRColLength; j++) {     // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

}
