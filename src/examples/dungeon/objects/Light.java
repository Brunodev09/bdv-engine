package examples.dungeon.objects;

import examples.dungeon.generation.Location;
import examples.dungeon.generation.WorldManager;
import examples.dungeon.tiles.Tile;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Light extends Actor {
    protected Vector3f rgbLight;
    protected int lightRadius;
    protected List<Tile> neighborsByRadius = new ArrayList<>();
    protected boolean shouldOscilate = true;
    protected List<Tile> oscilatingTiles = new ArrayList<>();

    public Light(Location location, Tile tile, Vector3f light, int lightRadius) {
        super(location, tile, 0, 0);
        this.rgbLight = light;
        this.lightRadius = lightRadius;
        this.type = "light";
        this.installLight();
    }

    private void installLight() {
        int x = this.currentLocation.getXGlobal();
        int y = this.currentLocation.getYGlobal();
        int z = this.currentLocation.getZGlobal();
        int xLocal = this.currentTile.getPositionX();
        int yLocal = this.currentTile.getPositionY();
        neighborsByRadius = WorldManager.tryGetChunk(x, y, z, xLocal, yLocal, lightRadius);
        for (Tile tile : neighborsByRadius) {
            tile.setLight(rgbLight);
        }
    }

    @Override
    public boolean action() {
        return oscilate();
    }

    private boolean oscilate() {
        for (int i = 0; i < neighborsByRadius.size(); i++) {
            int x = this.currentLocation.getXGlobal();
            int y = this.currentLocation.getYGlobal();
            int z = this.currentLocation.getZGlobal();
            if (i == lightRadius / 2 || i == neighborsByRadius.size() - (lightRadius / 2)) {
                Tile target = WorldManager.tryGetTile(x, y, z, neighborsByRadius.get(i).getPositionX() + 1,
                        neighborsByRadius.get(i).getPositionY());
                if (target == null) continue;
                shouldOscilate = !shouldOscilate;
                if (shouldOscilate) target.setLight(null);
                else target.setLight(rgbLight);
            }
        }
        return shouldOscilate;
    }

    public Vector3f getLight() {
        return rgbLight;
    }

    public void setLight(Vector3f light) {
        this.rgbLight = light;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    public void setLightRadius(int lightRadius) {
        this.lightRadius = lightRadius;
    }
}
