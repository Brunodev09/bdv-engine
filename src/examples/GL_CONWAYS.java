package examples;

import engine.api.EntityAPI;
import engine.api.ScriptGL;
import engine.Bdv;
import engine.entities.Camera2D;
import engine.math.Dimension;
import engine.math.RGBAf;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

// @TODO - Implement a logger
// @TODO - Implement custom exceptions
// @TODO - Make Script interface implement the Engine and not the Engine implement the Script inner workings

public class GL_CONWAYS extends ScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_CONWAYS.class.getName());

    private final int rows = 100;
    private final int cols = 100;
    private final int[][] matrix = new int[rows][cols];
    private final int[][] bufferedMatrix = new int[rows][cols];
    private final Dimension tileSize;
    private final Random random = new Random();

    private static final String whiteTexturePath = new File("src/examples/res/white").getAbsolutePath();
    private static final String blackTexturePath = new File("src/examples/res/black").getAbsolutePath();

    float r = 0;
    float g = 0;
    float b = 0;

    float r2 = 0;
    float g2 = 0;
    float b2 = 0;

    float r3 = 0;
    float g3 = 0;
    float b3 = 0;

    float r4 = 0;
    float g4 = 0;
    float b4 = 0;

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

        randomizeColors();

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
                    EntityAPI entityAPI = new EntityAPI(whiteTexturePath, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                    entityAPI.setScale(0.1f);
                    this.entities.add(entityAPI);
                }
                else {
                    EntityAPI entityAPI = new EntityAPI(blackTexturePath, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
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
                    entityAPI.setFile(whiteTexturePath);
                }
                else {
                    entityAPI.setFile(blackTexturePath);
                }
                // color customization
                if (i < 0 && j > 0) {
                    entityAPI.setRgbVector(new Vector3f(r, g, b));
                }
                else if (i > 0 && j > 0) {
                    entityAPI.setRgbVector(new Vector3f(r2, g2, b2));
                }
                else if (i > 0 && j < 0) {
                    entityAPI.setRgbVector(new Vector3f(r3, g3, b3));
                }
                else if (i < 0 && j < 0) {
                    entityAPI.setRgbVector(new Vector3f(r4, g4, b4));
                }
                else if (i == 0) {
                    entityAPI.setRgbVector(new Vector3f(0, 0, 0));
                }
                else {
                    entityAPI.setRgbVector(new Vector3f(0, 0, 0));
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

    public void randomizeColors() {
        r = random.nextFloat();
        g = random.nextFloat();
        b = random.nextFloat();

        r2 = random.nextFloat();
        g2 = random.nextFloat();
        b2 = random.nextFloat();

        r3 = random.nextFloat();
        g3 = random.nextFloat();
        b3 = random.nextFloat();

        r4 = random.nextFloat();
        g4 = random.nextFloat();
        b4 = random.nextFloat();
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

    public static void main(String[] args) {
        try {
            new Bdv(GL_CONWAYS.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}

