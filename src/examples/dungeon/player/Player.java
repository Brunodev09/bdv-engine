package examples.dungeon.player;

import engine.api.EntityAPI;
import examples.dungeon.generation.Location;
import examples.dungeon.tiles.PlayerTile;
import examples.dungeon.tiles.Tile;

public class Player implements Cloneable {
    private Tile playerTile;
    private Tile previousTile;
    private EntityAPI playerEntity;
    private Location currentLocation;

    public Player(int x, int y) {
        playerTile = new PlayerTile(x, y);
    }

    public Tile getPlayerTile() {
        return playerTile;
    }

    public void setPreviousTile(Tile previousTile) {
        this.previousTile = previousTile;
    }

    public Tile getPreviousTile() {
        return previousTile;
    }

    public EntityAPI getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(EntityAPI playerEntity) {
        this.playerEntity = playerEntity;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
