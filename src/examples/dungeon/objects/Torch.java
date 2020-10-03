package examples.dungeon.objects;

import engine.texture.SpriteSheet;
import examples.dungeon.generation.Location;
import examples.dungeon.mob.FactionTypes;
import examples.dungeon.tiles.Tile;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.io.File;

public class Torch extends Actor {

    private static final String SPRITESHEET_FILE_PATH = new File("src/examples/dungeon/assets/assetsComplete").getAbsolutePath();
    protected SpriteSheet sprite = new SpriteSheet(SPRITESHEET_FILE_PATH, new Rectangle(39, 39), 9, 10, new Rectangle(783, 1176));

    public Torch(Location location, Tile tile, int width, int height, Vector3f light, int lightRadius) {
        super(location, tile, width, height, "torch");
        Actor lightActor = new Light(location, tile, light, lightRadius);
        addSubActor(lightActor);
        setType("torch");
    }

    public Torch(Location location, Tile tile, int width, int height) {
        super(location, tile, width, height, "torch");
        fov = 20;
        emitsLight = true;
        setType("torch");
    }

    @Override
    public SpriteSheet getSprite() {
        return sprite;
    }
}
