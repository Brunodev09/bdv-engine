package engine.api;

import engine.texture.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class ChunkManagerAPI {
    private final List<ChunkAPI> chunks = new ArrayList<>();
    private final SpriteSheet spriteSheet;
    private final List<EntityAPI> unChunkedEntities = new ArrayList<>();
    private boolean updateChunk = false;
    private static int globalId = 0;
    private final int id;

    public ChunkManagerAPI(SpriteSheet spriteSheet) {
        globalId++;
        this.id = globalId;
        this.spriteSheet = spriteSheet;
    }

    public List<ChunkAPI> getChunks() {
        return chunks;
    }

    public void addChunk(ChunkAPI chunk) {
        chunks.add(chunk);
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public EntityAPI getUnChunkedEntities(int index) {
        return this.unChunkedEntities.get(index);
    }

    public void setUnChunkedEntities(EntityAPI entity) {
        this.unChunkedEntities.add(entity);
    }

    public List<EntityAPI> getUnChunkedEntities() {
        return unChunkedEntities;
    }

    public boolean isUpdateChunk() {
        return updateChunk;
    }

    public void setUpdateChunk(boolean updateChunk) {
        this.updateChunk = updateChunk;
    }

    public int getId() {
        return id;
    }
}