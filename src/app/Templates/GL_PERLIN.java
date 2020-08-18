package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.Dimension;
import app.Math.PerlinNoise;
import app.Math.RGBAf;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GL_PERLIN extends ScriptGL {

    private final int rows = 100;
    private final int cols = 100;
    private final int[][] matrix = new int[rows][cols];
    private final Dimension tileSize;

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
                EntityAPI entityAPI = new EntityAPI("grey", new Vector3f(tileSize.width * i, tileSize.height * j, 0), new Vector2f(0, 0));
                entityAPI.setScale(1.0f);
                double noise = PerlinNoise.noise(xOffset, yOffset);
                entityAPI.setRgb(new app.Math.Vector3f((float) noise, (float) noise, (float) noise));
                this.entities.add(entityAPI);
                ptrJ++;
                xOffset += 0.01;
            }
            yOffset += 0.01;
            ptrI++;
        }
    }

    @Override
    public void update() {

    }
}
