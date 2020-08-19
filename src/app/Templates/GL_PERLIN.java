package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.Dimension;
import app.Math.PerlinNoise;
import app.Math.RGBAf;
import app.Texture.SpriteSheet;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GL_PERLIN extends ScriptGL {

    private final int rows = 100;
    private final int cols = 100;
    private final int[][] matrix = new int[rows][cols];
    private final Dimension tileSize;
    final SpriteSheet red = new SpriteSheet("spritesheet2", new Rectangle(256, 256), 0, 0);

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
        int ptrI = 0;
        int ptrJ = 0;
        double yOffset = 0;
        for (int i = -rows / 2; i < rows / 2; i++) {
            ptrJ = 0;
            double xOffset = 0;
            for (int j = -cols / 2; j < cols / 2; j++) {
                EntityAPI entityAPI = new EntityAPI(null, new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                entityAPI.setSpriteSheet(red);
                entityAPI.setScale(0.1f);
                double noise = PerlinNoise.noise(xOffset, yOffset) * 255;
                entityAPI.setRgbVector(new Vector3f((float) noise, (float) noise, (float) noise));
//                entityAPI.setRgb(new RGBAf((float) noise, (float) noise, (float) noise, 255.0f));
                this.entities.add(entityAPI);
                ptrJ++;
                xOffset += 0.1;
            }
            yOffset += 0.1;
            ptrI++;
        }
    }

    @Override
    public void update() {

    }
}
