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
        super(location, tile, 0, 0, "light");
        this.rgbLight = light;
        this.lightRadius = lightRadius;
        this.installLight();
        setType("light");
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
        int x = this.currentLocation.getXGlobal();
        int y = this.currentLocation.getYGlobal();
        int z = this.currentLocation.getZGlobal();

        for (int i = 0; i < neighborsByRadius.size(); i++) {
            if (i == lightRadius / 2) {
                Tile target = WorldManager.tryGetTile(x, y, z, neighborsByRadius.get(i).getPositionX() - 1,
                        neighborsByRadius.get(i).getPositionY());
                Tile target2 = WorldManager.tryGetTile(x, y, z, neighborsByRadius.get(i).getPositionX() + lightRadius,
                        neighborsByRadius.get(i).getPositionY());
                Tile target3 = WorldManager.tryGetTile(x, y, z, neighborsByRadius.get(i).getPositionX() + lightRadius / 2,
                        neighborsByRadius.get(i).getPositionY() - (lightRadius / 2) - 1);
                Tile target4 = WorldManager.tryGetTile(x, y, z, neighborsByRadius.get(i).getPositionX() + lightRadius / 2,
                        neighborsByRadius.get(i).getPositionY() + (lightRadius / 2));


                if (shouldOscilate) {
                    if (target != null) target.setLight(null);
                    if (target2 != null) target2.setLight(null);
                    if (target3 != null) target3.setLight(null);
                    if (target4 != null) target4.setLight(null);
                }
                else {
                    if (target != null) {
                        target.setLight(rgbLight);
                        target.setHidden(false);
                    }
                    if (target2 != null) {
                        target2.setLight(rgbLight);
                        target2.setHidden(false);
                    }
                    if (target3 != null) {
                        target3.setLight(rgbLight);
                        target3.setHidden(false);
                    }
                    if (target4 != null) {
                        target4.setLight(rgbLight);
                        target4.setHidden(false);
                    }

                }
                shouldOscilate = !shouldOscilate;
            }
        }
        return true;
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
