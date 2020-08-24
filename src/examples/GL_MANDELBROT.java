package examples;

import engine.api.EntityAPI;
import engine.api.BdvScriptGL;
import engine.Bdv;
import engine.entities.Camera2D;
import engine.math.Complex;
import engine.math.Dimension;
import engine.math.RGBAf;
import engine.texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GL_MANDELBROT extends BdvScriptGL {

    private static final Logger LOGGER = Logger.getLogger(GL_MANDELBROT.class.getName());

    private final int rows = 100;
    private final int cols = 100;
    private final int[][] matrix = new int[rows][cols];
    private final Dimension tileSize;
    private final Random random = new Random();

    final double setDimensions = 100;
    final int maxIterations = 255;
    final double xCenter = 0;
    final double yCenter = 0;
    double size = 30;

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/res/spritesheet").getAbsolutePath();
    final SpriteSheet red = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(256, 256), 1, 1);
    final SpriteSheet green = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(256, 256), 1, 0);
    final SpriteSheet blue = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(256, 256), 2, 0);

    public GL_MANDELBROT() {
        this.camera2d = new Camera2D();
        this.entities = new ArrayList<>();
        this.resolution = new Dimension(1024, 768);
        this.background = new RGBAf(0, 0, 0, 255);
        this.FPS = 1;
        this.tileSize = new Dimension(resolution.width / rows, resolution.height / cols);

        this.init(this.entities, this.resolution, this.background);

    }

    @Override
    public void init(List<EntityAPI> entities, Dimension resolution, RGBAf background) {
        if (setDimensions > rows || setDimensions > cols) return;

        for (int i = 0; i < setDimensions; i++) {
            for (int j = 0; j < setDimensions; j++) {
                double x0 = xCenter - size / 2 + size * i / setDimensions;
                double y0 = yCenter - size / 2 + size * j / setDimensions;
                Complex z0 = new Complex(x0, y0);
                int pixel = maxIterations - mandelbrotSet(z0, maxIterations);
                matrix[i][(int) setDimensions - 1 - j] = pixel;
            }
        }
        int ptrI = 0;
        int ptrJ = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            ptrJ = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                if (matrix[ptrI][ptrJ] == 255) {
                    EntityAPI entitAPI = new EntityAPI(null, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                    entitAPI.setSpriteSheet(red);
                    entitAPI.setScale(0.09f);
                    entities.add(entitAPI);
                } else if (matrix[ptrI][ptrJ] == 0) {
                    EntityAPI entitAPI = new EntityAPI(null, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                    entitAPI.setSpriteSheet(green);
                    entitAPI.setScale(0.09f);
                    entities.add(entitAPI);
                } else {
                    EntityAPI entitAPI = new EntityAPI(null, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                    entitAPI.setSpriteSheet(blue);
                    entitAPI.setScale(0.09f);
                    entities.add(entitAPI);
                }
                ptrJ++;
            }
            ptrI++;
        }

    }

    @Override
    public void update() {
        for (int i = 0; i < setDimensions; i++) {
            for (int j = 0; j < setDimensions; j++) {
                double x0 = xCenter - size / 2 + size * i / setDimensions;
                double y0 = yCenter - size / 2 + size * j / setDimensions;
                Complex z0 = new Complex(x0, y0);
                int pixel = maxIterations - mandelbrotSet(z0, maxIterations);
                matrix[i][(int) setDimensions - 1 - j] = pixel;
            }
        }
        int ptrI = 0;
        int ptrJ = 0;
        int ptr = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            ptrJ = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                EntityAPI entityAPI = entities.get(ptr);
                if (matrix[ptrI][ptrJ] == 255) {
                    entityAPI.setSpriteSheet(red);
                } else if (matrix[ptrI][ptrJ] == 0) {
                    entityAPI.setSpriteSheet(green);
                } else {
                    entityAPI.setSpriteSheet(blue);
                }
                ptrJ++;
                ptr++;
            }
            ptrI++;
        }

//        if (size < 0) size = 30;
//        else size--;
        if (size != 2) size--;
    }

    // Check if c = a + ib is in Mandelbrot set
    public static int mandelbrotSet(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }

    public static void main(String[] args) {
        try {
            new Bdv(GL_MANDELBROT.class);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }
}
