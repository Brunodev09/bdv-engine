package app.Templates;

import app.API.Script;
import app.Core.Interfaces.Entity;
import app.Core.Interfaces.Model;
import app.Math.Dimension;
import app.Math.Point;
import app.Math.RGBA;

import java.util.ArrayList;
import java.util.List;

public class MATRIX_TEMPLATE extends Script {

    private int counter = 0;
    private double angle = 0;
    private int[][] pointsInitialPositions;

    public MATRIX_TEMPLATE() {
        this.entities = new ArrayList<Entity>();
        this.resolution = new Dimension(800, 600);
        this.background = new RGBA(255, 20, 147, 255);

        pointsInitialPositions = new int[][] {
                { resolution.width / 2, resolution.width / 2 },
                { (resolution.width / 2) + 100, resolution.width / 2 },
                { resolution.width / 2, (resolution.width / 2) + 100 },
                { (resolution.width / 2) + 100, (resolution.width / 2) + 100 },
        };
        this.init(entities, resolution, background);
    }

    @Override
    public void init(List<Entity> entities, Dimension resolution, RGBA background) {
        this.entities.add(new Entity(new Point<>(0, 0), new Point<>(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Point<>(1, 0), new Point<>(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Point<>(0, 1), new Point<>(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255), Model.POINT));

        this.entities.add(new Entity(new Point<>(1, 1), new Point<>(5.0f, 5.0f),
                new Dimension(50, 50), new RGBA(255, 0, 0, 255), Model.POINT));

    }

    @Override
    public void update() {

        angle += 0.01;
        int ptr = 0;
        int ptr2 = -1;
        for (Entity point : entities) {
            double[][] projectionMatrix = {
                    { 1, 0, 0 },
                    { 0, 1, 0 },
                    { 0, 0, 1 }
            };

            double[][] positionVector = new double[][] {
                    { (double) pointsInitialPositions[ptr][0] },
                    { (double) pointsInitialPositions[ptr2 + 1][0] },
                    { 0 }
            };

            double[][] rotation = getRotationMatrix(angle);
            double[][] translation = getTranslationMatrix(resolution.width / 2, resolution.height / 2);
            double[][] newPositionVector = matrixmult(projectionMatrix, positionVector);
            double[][] rotatedPositionVector = matrixmult(rotation, newPositionVector);
//            double[][] translatedVector = matrixmult(translation, rotatedPositionVector);


            point.setPosition(
                    new Point<Integer>(
                            (int) rotatedPositionVector[0][0],
                            (int) rotatedPositionVector[1][0]));
            ptr++;
            ptr2++;
        }

    }

    private double[][] getRotationMatrix(double angle) {
        return new double[][] {
                { Math.cos(angle), -Math.sin(angle), 0 },
                { Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 1 }
        };
    }

    private double[][] getTranslationMatrix(int tx, int ty) {
        return new double[][] {
                { 1, 0, 0 },
                { 0, 1, 0 },
                { tx, ty, 1 }
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