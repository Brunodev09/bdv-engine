package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.Dimension;
import app.Math.RGBAf;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GL_CONWAYS extends ScriptGL {

    private final int rows = 100;
    private final int cols = 100;
    private final int[][] matrix = new int[rows][cols];
    private final int[][] bufferedMatrix = new int[rows][cols];
    private final Dimension tileSize;
    private final Random random = new Random();

    public GL_CONWAYS() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 10;

        this.tileSize = new Dimension(resolution.width / rows, resolution.height / cols);
        this.init(this.entities, this.resolution, this.background);
    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (random.nextInt(9) == 1) {
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        int ptrI = 0;
        int ptrJ = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            ptrJ = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                if (matrix[ptrI][ptrJ] == 1) {
                    EntityAPI entityAPI = new EntityAPI("white", new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                    entityAPI.setScale(0.1f);
                    this.entities.add(entityAPI);
                }
                else {
                    EntityAPI entityAPI = new EntityAPI("black", new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                    entityAPI.setScale(0.1f);
                    this.entities.add(entityAPI);
                }
                ptrJ++;
            }
            ptrI++;
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!shouldDie(i, j)) {
                    bufferedMatrix[i][j] = 1;
                } else {
                    bufferedMatrix[i][j] = 0;
                }
            }
        }

        int ptrI = 0;
        int ptrJ = 0;
        int ptr = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            ptrJ = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                EntityAPI entityAPI = this.entities.get(ptr);

                if (matrix[ptrI][ptrJ] == 1) {
                    entityAPI.setFile("white");
                }
                else {
                    entityAPI.setFile("black");
                }
                ptrJ++;
                ptr++;
            }
            ptrI++;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = bufferedMatrix[i][j];
            }
        }
    }

    public boolean shouldDie(int x, int y) {
        int neighbours = 0;
        boolean cellStatus = matrix[x][y] != 0;

        if (x < rows - 1 && matrix[x + 1][y] == 1) {
            neighbours++;
        }
        if (x > 0 && matrix[x - 1][y] == 1) {
            neighbours++;
        }
        if (y < cols - 1 && matrix[x][y + 1] == 1) {
            neighbours++;
        }
        if (y > 0 && matrix[x][y - 1] == 1) {
            neighbours++;
        }
        if (x < rows - 1 && y < cols - 1 && matrix[x + 1][y + 1] == 1) {
            neighbours++;
        }
        if (x > 0 && y < cols - 1 && matrix[x - 1][y + 1] == 1) {
            neighbours++;
        }
        if (x < rows - 1 && y > 0 && matrix[x + 1][y - 1] == 1) {
            neighbours++;
        }
        if (x > 0 && y > 0 && matrix[x - 1][y - 1] == 1) {
            neighbours++;
        }

        if (cellStatus) {
            if (neighbours < 2 || neighbours > 3) return true;
            return neighbours != 2 && neighbours != 3;
        }
        else if (!cellStatus && neighbours == 3) return false;
        else return true;
    }
}
