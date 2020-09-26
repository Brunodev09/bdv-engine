package engine.api;

import engine.texture.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class ChunkManagerAPI {
    private final List<ChunkAPI> chunks = new ArrayList<>();
    private final SpriteSheet spriteSheet;

    public ChunkManagerAPI(SpriteSheet spriteSheet) {
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
}
