package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Entities.Entity;
import app.Math.Dimension;
import app.Math.PerlinNoise;
import app.Math.RGBAf;
import app.Texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GL_PERLIN extends ScriptGL {

    private final int rows = 100;
    private final int cols = 100;
    private final int[][] matrix = new int[rows][cols];
    private final Dimension tileSize;
    private final Random random = new Random();
    private int counter = 0;

    final SpriteSheet tile1 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 0, 0);
    final SpriteSheet tile2 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 1, 0);
    final SpriteSheet tile3 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 2, 0);
    final SpriteSheet tile4 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 0, 1);
    final SpriteSheet tile5 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 0, 2);
    final SpriteSheet tile6 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 1, 1);
    final SpriteSheet tile7 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 1, 2);
    final SpriteSheet tile8 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 2, 1);
    final SpriteSheet tile9 = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 2, 2);

    private SpriteSheet selectedTile;

    public GL_PERLIN() {
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
        double yOffset = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            double xOffset = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                EntityAPI entityAPI = new EntityAPI(null, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                entityAPI.setSpriteSheet(tile1);
                entityAPI.setScale(0.1f);
                double noise = PerlinNoise.noise(xOffset, yOffset);
                entityAPI.setRgbVector(new Vector3f((float) noise, (float) noise, (float) noise));
                this.entities.add(entityAPI);
                xOffset += 0.1;
            }
            yOffset += 0.1;
        }
    }

    @Override
    public void update() {
        counter++;
        if (counter < 100) return;

        int r = random.nextInt(2);
        int r2 = random.nextInt(2);

        if (r == 0 && r2 == 0) selectedTile = tile1;
        if (r == 1 && r2 == 0) selectedTile = tile2;
        if (r == 2 && r2 == 0) selectedTile = tile3;
        if (r == 0 && r2 == 1) selectedTile = tile4;
        if (r == 0 && r2 == 2) selectedTile = tile5;
        if (r == 1 && r2 == 1) selectedTile = tile6;
        if (r == 1 && r2 == 2) selectedTile = tile7;
        if (r == 2 && r2 == 1) selectedTile = tile8;
        if (r == 2 && r2 == 2) selectedTile = tile9;

        double yOffset = 0;
        int ptr = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            double xOffset = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                EntityAPI entityAPI = this.entities.get(ptr);
                entityAPI.setSpriteSheet(selectedTile);
                double noise = PerlinNoise.noise(xOffset, yOffset);
                entityAPI.setRgbVector(new Vector3f((float) noise, (float) noise, (float) noise));
                xOffset += 0.1;
                ptr++;
            }
            yOffset += 0.1;
        }
        counter = 0;
    }
}
