package app.Templates;

import app.API.EntityAPI;
import app.API.ScriptGL;
import app.Entities.Camera2D;
import app.Math.Dimension;
import app.Math.RGBAf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    }

    @Override
    public void update() {

    }
}
