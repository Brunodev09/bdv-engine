package engine.core;

import engine.api.ChunkAPI;
import engine.api.ChunkManagerAPI;
import engine.math.Dimension;
import engine.meshes.ChunkMesh;
import engine.texture.*;
import engine.meshes.RectangleMesh;
import engine.api.EntityAPI;
import engine.entities.Camera;
import engine.entities.Camera2D;
import engine.entities.Entity;
import engine.entities.Lightsource;
import engine.models.BufferedModel;
import engine.models.Model;
import engine.models.TexturedModel;
import engine.video.ModelParser;
import engine.video.Pipeline;
import engine.video.RenderManager;
import engine.video.VAOManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

// @TODO - Set this up as a gradle project and remove dependencies from source code
// @TODO - Imgui binding
// @TODO - Implement custom exceptions
// @TODO - Fix class structure of Entity and EntityAPI to be more flexible towards spritesheets

public class Engine {

    Configuration configuration;
    Pipeline pipe = new Pipeline();
    Map<Integer, Entity> toRender = new HashMap<>();
    Map<Integer, Entity> toRenderChunk = new HashMap<>();
    List<EntityAPI> toRenderChunkFromScript = new ArrayList<>();
    Map<Integer, Lightsource> lights = new HashMap<>();
    Map<String, Integer> textures = new HashMap<>();
    Map<String, Integer> sprites = new HashMap<>();
    List<EntityAPI> scriptEntities;
    Map<String, ProcessedBufferedImage> processedImages = new HashMap<>();
    Map<String, Model> models = new HashMap<>();
    Map<float[], Model> modelsDemo = new HashMap<>();
    Map<float[], Boolean> modelsChunkDemo = new HashMap<>();
    Model rectangleModel;
    List<ChunkMesh> chunkMeshes = new ArrayList<>();
    ChunkManagerAPI chunkManager = null;
    double lastTime;
    double lastUpdate;
    int frames = 0;

    Logger LOG = Logger.getLogger(Engine.class.getName());

    public void loop(Configuration config) {

        if (config.script == null) return;

        configuration = config;

        RenderManager.createRender(config.WIDTH, config.HEIGHT, config.TITLE, config.script.background);

        scriptEntities = config.script.entities;

        if (config.script.camera == null) {

            Camera2D cam2d = config.script.camera2d == null ? new Camera2D() : config.script.camera2d;

            if (config.script.chunkRendering) {
                chunkManager = configuration.script.chunkManager;
                chunkBasedRendering();
            }
            else proceduralRendering(scriptEntities);

            while (!RenderManager.shouldExit()) {

                RenderManager.toggleDebugShaderMode(config.script.debugShader);
                if (!configuration.script.chunkRendering) createAndUpdateFormerEntity(scriptEntities);
                else {
                    createAndUpdateFormerEntity(toRenderChunkFromScript);
                    updateInnerTextureCoordinates();
                    if (!configuration.script.chunkManager.getUnChunkedEntities().isEmpty()) {
                        createAndUpdateFormerEntity(configuration.script.chunkManager.getUnChunkedEntities());
                    }
                }

                if (lastUpdate == 0.0) {
                    lastUpdate = currentTimeMillis();
                }

                if (currentTimeMillis() - lastUpdate >= 1000) {
                    if (config.script.logFps) {
                        LOG.info("FPS -> " + frames);
                    }
                    frames = 0;
                    lastUpdate = currentTimeMillis();
                }

                frames++;
                config.script.update();

                if (config.script.chunkRendering) {
                    toRenderChunk.forEach((key, val) -> {
                        if (val.shouldRender()) RenderManager.processEntity(val);
                    });
                } else {
                    toRender.forEach((key, val) -> {
                        if (val.shouldRender()) RenderManager.processEntity(val);
                    });
                }

                RenderManager.renderBatch(cam2d);
                RenderManager.updateRender(config.FPS);
            }
            // Freeing memory
            if (config.script.inputAPI != null) {
                config.script.inputAPI.destroy();
            }

        } else {
            procedural3dRendering();
        }

        RenderManager.runCollector();
        pipe.runCollector();
        RenderManager.closeRender();
    }

    private void createMesh(ChunkManagerAPI chunkManager, List<ChunkMesh> chunkMeshes) {
        for (ChunkAPI chunk : chunkManager.getChunks()) {
            SpriteSheet[] spriteSheets = new SpriteSheet[chunk.getChunkSize()];
            for (int i = 0; i < chunk.getChunk().size(); i++) {
                spriteSheets[i] = chunk.getChunk().get(i).getSpriteSheet();
            }

            ChunkMesh chunkMesh = new ChunkMesh(
                    chunk.getTileSize().width,
                    chunk.getTileSize().height,
                    chunk.getTilesPerRow(),
                    chunk.getChunkSize(),
                    chunk.getOpenGlPosition().x,
                    chunk.getOpenGlPosition().y,
                    spriteSheets,
                    chunk.shouldRender());
            chunkMeshes.add(chunkMesh);
        }
    }

    private void chunkBasedRendering() {
        if (!chunkManager.getChunks().isEmpty()) {

            createMesh(chunkManager, chunkMeshes);

            for (ChunkMesh mesh : chunkMeshes) {
                BufferedModel defaultData2D = new BufferedModel(
                        mesh.getMesh(),
                        mesh.getTextureCoordinates(),
                        mesh.getIndexes());

                Model mdl = pipe.loadDataToVAO(defaultData2D.getVertices(), defaultData2D.getTextures(), defaultData2D.getIndexes());

                modelsDemo.put(mesh.getTextureCoordinates(), mdl);
                modelsChunkDemo.put(mesh.getTextureCoordinates(), true);

                EntityAPI entityAPI = new EntityAPI(null,
                        new Vector3f(mesh.getxPos(), mesh.getyPos(), 0),
                        new Dimension((int) (mesh.getTilesPerRow() * mesh.getTileSizeX()),
                                (int) (mesh.getTilesPerRow() * mesh.getTileSizeY())),
                        new Vector2f(0, 0));

                entityAPI.setSpriteSheet(chunkManager.getSpriteSheet());
                entityAPI.setShouldRender(mesh.shouldRender());
                entityAPI.setUv(mesh.getTextureCoordinates());
                entityAPI.setRenderSpriteRetroCompatibility(false);
                toRenderChunkFromScript.add(entityAPI);
                textureAndPlaceBackEntity(entityAPI, mdl);
            }
        }
    }

    public void proceduralRendering(List<EntityAPI> entitiesToProcess) {
        for (EntityAPI entity : entitiesToProcess) {
            Model mdl = null;

            float[] textureStrategy;
            if (entity.isRenderSpriteRetroCompatibility()) textureStrategy = RectangleMesh.SquareTextureCoordinates;
            else {
                Rectangle subImageSize = entity.getSpriteSheet().getTile();
                Rectangle fullImageSize = entity.getSpriteSheet().getFullImageSize();
                float uOffset = entity.getSpriteSheet().getTileX();
                float vOffset = entity.getSpriteSheet().getTileY();
                float u = (float) subImageSize.width / fullImageSize.width;
                float v = (float) subImageSize.height / fullImageSize.height;

                textureStrategy = new float[]{
                        (u * uOffset), (vOffset * v),
                        (u + (u * uOffset)), (vOffset * v),
                        (u + (u * uOffset)), (v + (vOffset * v)),
                        (u * uOffset), (v + (vOffset * v)),
                };
                entity.setUv(textureStrategy);
            }

            boolean shouldInsert = true;
            for (Map.Entry<float[], Model> entryModel : modelsDemo.entrySet()) {
                float[] prevTexture = entryModel.getKey();
                float[] texturesScope;
                if (entity.isRenderSpriteRetroCompatibility()) texturesScope = textureStrategy;
                else texturesScope = entity.getUv();
                if (Arrays.equals(prevTexture, texturesScope)) {
                    shouldInsert = false;
                    break;
                }
            }
            if (shouldInsert) {
                BufferedModel defaultData2D = new BufferedModel(
                        RectangleMesh.squareFactory(0, 0, entity.getWidth(), entity.getHeight()),
                        textureStrategy,
                        RectangleMesh.SquareIndexes);

                mdl = pipe.loadDataToVAO(defaultData2D.getVertices(), defaultData2D.getTextures(), defaultData2D.getIndexes());
                modelsDemo.put(textureStrategy, mdl);
            } else {
                mdl = findMatchingModelWithTextureCoordinatesBuffer(entity.getUv());
            }
            textureAndPlaceBackEntity(entity, mdl);
        }
    }

    private void textureAndPlaceBackEntity(EntityAPI entity, Model mdl) {
        int textureId = getTextureId(entity);
        int newTexure = checkForImageProcessing(entity, textureId);

        if (newTexure != 0) textureId = newTexure;

        ModelTexture texture2D = new ModelTexture(textureId);
        if (entity.getRgbVector() != null) {
            texture2D.setColorOffset(entity.getRgbVector());
        }
        if (entity.isAmbientLightOn()) {
            texture2D.setAmbientLightToggle(true);
            texture2D.setAmbientLight(entity.getAmbientLight());
        }
        if (entity.isGlowing()) {
            texture2D.setToggleGlow(true);
            texture2D.setGlowColor(entity.getColorGlow());
        }
        if (entity.isPlayer()) {
            texture2D.setPlayer(true);
        }
        TexturedModel tmdl2 = new TexturedModel(mdl, texture2D);
        Entity formerEntity = new Entity(tmdl2,
                entity.getPosition(),
                entity.getRotationX(),
                entity.getRotationY(),
                entity.getRotationZ(),
                entity.getScaleX(),
                entity.getScaleY(),
                entity.getScaleZ());
        entity.setLink(formerEntity.getId());
        formerEntity.setShouldRender(entity.shouldRender());
        if (configuration.script.chunkRendering) {
            toRenderChunk.put(formerEntity.getId(), formerEntity);
        } else {
            toRender.put(formerEntity.getId(), formerEntity);
        }
    }

    public void procedural3dRendering() {
        Camera cam = configuration.script.camera;
        Lightsource light = new Lightsource(new Vector3f(300, 300, -30), new Vector3f(1, 1, 1));

        for (EntityAPI entity : scriptEntities) {
            BufferedModel data = ModelParser.parseOBJ(entity.getModel());
            Model mdl = pipe.loadDataToVAO(data.getVertices(), data.getTextures(), data.getNormals(), data.getIndexes());
            ModelTexture texture = new ModelTexture(pipe.loadTexture(entity.getFile()));
            texture.setShineDamper(10);
            texture.setReflectivity(1);
            TexturedModel tmdl2 = new TexturedModel(mdl, texture);
            Entity formerEntity = new Entity(tmdl2,
                    entity.getPosition(),
                    entity.getRotationX(),
                    entity.getRotationY(),
                    entity.getRotationZ(),
                    entity.getScaleX(),
                    entity.getScaleY(),
                    entity.getScaleZ());
            entity.setLink(formerEntity.getId());
            toRender.put(formerEntity.getId(), formerEntity);
        }

        while (!RenderManager.shouldExit()) {
            double thisTime = currentTimeMillis();
            double delta = thisTime - lastTime;
            lastTime = thisTime;

            configuration.script.update();
            createAndUpdateFormerEntity(scriptEntities);
            cam.move(delta);

            toRender.forEach((key, val) -> RenderManager.processEntity(val));

            RenderManager.renderBatch(light, cam);
            RenderManager.updateRender(configuration.FPS);
        }
    }

    private void createAndUpdateFormerEntity(List<EntityAPI> entitiesToProcess) {
        // @TODO - Reflect 2-ways the changes made Entity <-> EntityAPI

        for (EntityAPI entity : entitiesToProcess) {
            Entity former = null;
            if (configuration.script.chunkRendering) {
                former = toRenderChunk.get(entity.getLink());
            } else {
                former = toRender.get(entity.getLink());
            }

            if (former.getPosition().x != entity.getPosition().x ||
                    former.getPosition().y != entity.getPosition().y ||
                    former.getPosition().z != entity.getPosition().z) {
                former.setPosition(entity.getPosition());
            }
            if (former.shouldRender() != entity.shouldRender()) {
                former.setShouldRender(entity.shouldRender());
            }
            if (former.getRotX() != entity.getRotationX()
                    || former.getRotY() != entity.getRotationY()
                    || former.getRotZ() != entity.getRotationZ()) {
                former.setRotX(entity.getRotationX());
                former.setRotY(entity.getRotationY());
                former.setRotZ(entity.getRotationZ());
            }
            if (entity.getEditModel()) {
                int textureId = getTextureId(entity);
                ModelTexture texture2D = new ModelTexture(textureId);
                if (entity.getRgbVector() != null) {
                    texture2D.setColorOffset(entity.getRgbVector());
                }
                if (entity.isAmbientLightOn()) {
                    texture2D.setAmbientLightToggle(true);
                    texture2D.setAmbientLight(entity.getAmbientLight());
                }
                if (entity.isGlowing()) {
                    texture2D.setToggleGlow(true);
                    texture2D.setGlowColor(entity.getColorGlow());
                }
                if (entity.isPlayer()) {
                    texture2D.setPlayer(true);
                }
                if (entity.isRenderSpriteRetroCompatibility()) entity.setUv(RectangleMesh.SquareTextureCoordinates);
                TexturedModel tmdl2 = new TexturedModel(findMatchingModelWithTextureCoordinatesBuffer(entity.getUv()), texture2D);
                former.setModel(tmdl2);
                entity.setEditModel(false);
            }
        }
    }

    private void updateInnerTextureCoordinates() {
        chunkMeshes.clear();
        createMesh(chunkManager, chunkMeshes);
        if (!findMatchingTextureCoordinates(chunkMeshes.get(0).getTextureCoordinates())) {
            List<VAOManager> managers = pipe.getManagers();
            pipe.updateTextureDataInVAO(managers.get(0).getId(), chunkMeshes.get(0).getTextureCoordinates());
            modelsChunkDemo.clear();
        }
    }

    // TODO - Make a test with a controlled number of sprites and assert that the texture map is holding that exact controlled number
    private int getTextureId(EntityAPI entity) {
        int id = 0;
        String spriteKey = null;
        if (entity.getSpriteSheet() != null && entity.getSpriteSheet().getFile() != null && entity.getSpriteSheet().getFullImageSize() == null) {
            spriteKey = entity.getSpriteSheet().getFile() + "" + entity.getSpriteSheet().getTileX() + entity.getSpriteSheet().getTileY();
        } else if (entity.getSpriteSheet() != null && entity.getSpriteSheet().getFullImageSize() != null) {
            spriteKey = entity.getSpriteSheet().getFile() + "" + entity.getSpriteSheet().getFullImageSize().width + "," + entity.getSpriteSheet().getFullImageSize().height;
        }
        if (textures.get(entity.getFile()) == null || entity.getSpriteSheet() != null) {
            if (entity.getSpriteSheet() != null && sprites.get(spriteKey) == null && entity.getUv() != null && !entity.isRenderSpriteRetroCompatibility()) {
                id = pipe.loadTexture(entity.getSpriteSheet().getFile());
                sprites.put(spriteKey, id);
            } else if (entity.getSpriteSheet() != null && sprites.get(spriteKey) == null) {
                id = pipe.loadTextureFromSpritesheet(entity.getSpriteSheet());
                sprites.put(spriteKey, id);
            } else if (entity.getSpriteSheet() != null && sprites.get(spriteKey) != null) {
                id = sprites.get(spriteKey);
            } else if (entity.getSpriteSheet() == null && textures.get(entity.getFile()) == null) {
                id = pipe.loadTexture(entity.getFile());
                textures.put(entity.getFile(), id);
            }
        } else {
            id = textures.get(entity.getFile());
        }
        return id;
    }

    private int checkForImageProcessing(EntityAPI entityAPI, int textureId) {
        // TODO - Setting this to 0 is dangerous
        int resultingTextureId = 0;
        if (entityAPI.getRgb() != null && entityAPI.getEditModel()) {

            entityAPI.setEditModel(false);
            BufferedImage image = TextureCache.getImage(textureId);

            if (image != null) {
                String key = entityAPI.getRgb().toString();
                ProcessedBufferedImage sameProcessed = processedImages.get(key);
                if (sameProcessed != null) {
                    // there is already a texture associated with this image processing work
                    resultingTextureId = sameProcessed.getTextureId();
                } else {
                    // new texture must be generated
                    if (entityAPI.getSpriteSheet() != null) {
                        ImageProcessor.filterImage(image, entityAPI.getRgb(), entityAPI.getSpriteSheet());
                        resultingTextureId = pipe.generateTexture(image, entityAPI.getSpriteSheet());
                    } else {
                        ImageProcessor.filterImage(image, entityAPI.getRgb());
                        resultingTextureId = pipe.generateTexture(image);
                    }
                    ProcessedBufferedImage processed = new ProcessedBufferedImage(image,
                            resultingTextureId,
                            entityAPI.getFile(),
                            entityAPI.getRgb());

                    processedImages.putIfAbsent(entityAPI.getRgb().toString(), processed);
                }
            }

        }
        return resultingTextureId;
    }

    private Model findMatchingModelWithTextureCoordinatesBuffer(float[] textureCoordinates) {
        Model indexModel = null;
        for (Map.Entry<float[], Model> entryModel : modelsDemo.entrySet()) {
            float[] prevTexture = entryModel.getKey();
            if (Arrays.equals(prevTexture, textureCoordinates)) {
                indexModel = entryModel.getValue();
                break;
            }
        }
        return indexModel;
    }

    private boolean findMatchingTextureCoordinates(float[] textureCoordinates) {
        boolean found = false;
        for (Map.Entry<float[], Boolean> entryModel : modelsChunkDemo.entrySet()) {
            float[] prevTexture = entryModel.getKey();
            if (Arrays.equals(prevTexture, textureCoordinates)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static float currentTimeMillis() {
        return (float) glfwGetTime() * 1000;
    }

}
