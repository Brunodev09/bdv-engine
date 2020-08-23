package engine.models;

import engine.texture.ModelTexture;

public class Terrain {
    private static final float TILE_SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    private float x;
    private float z;
    private BufferedModel mdl;
    private ModelTexture texture;
    private Model processedModel;

    public Terrain(int px, int pz, ModelTexture texture) {
        this.texture = texture;
        this.x = px * TILE_SIZE;
        this.z = pz * TILE_SIZE;

        this.mdl = this.getTerrainData();
    }

    private BufferedModel getTerrainData() {
        int count = (int) Math.pow(VERTEX_COUNT, 2);
        // (x,y,z)
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        // (x,y)
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPtr = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                // x component
                vertices[vertexPtr * 3] = -(float) j / ((float) VERTEX_COUNT - 1) * TILE_SIZE;
                // y component, height of each tile
                vertices[vertexPtr * 3 + 1] = 0;
                // z component -> fill one stack of x components with the same z
                vertices[vertexPtr * 3 + 2] = -(float) i / ((float) VERTEX_COUNT - 1) * TILE_SIZE;

                // normal vectors in flat square terrains -> v = (0, 1, 0)
                normals[vertexPtr * 3] = 0;
                normals[vertexPtr * 3 + 1] = 1;
                normals[vertexPtr * 3 + 2] = 0;

                textureCoords[vertexPtr * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPtr * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);

                vertexPtr++;

            }
        }
        int indicesPtr = 0;
        for (int z = 0; z < VERTEX_COUNT - 1; z++) {
            for (int x = 0; x < VERTEX_COUNT - 1; x++) {
                int topLeft = (z * VERTEX_COUNT) + x;
                int topRight = topLeft + 1;

                int bottomLeft = ((z + 1) * VERTEX_COUNT) + x;
                int bottomRight = bottomLeft + 1;

                indices[indicesPtr++] = topLeft;
                indices[indicesPtr++] = bottomLeft;
                indices[indicesPtr++] = topRight;
                indices[indicesPtr++] = topRight;
                indices[indicesPtr++] = bottomLeft;
                indices[indicesPtr++] = bottomRight;
            }
        }
        return new BufferedModel(vertices, textureCoords, normals, indices);
    }

    public static float getTileSize() {
        return TILE_SIZE;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public BufferedModel getMdl() {
        return mdl;
    }

    public void setTexture(ModelTexture texture) {
        this.texture = texture;
    }

    public void setMdl(BufferedModel mdl) {
        this.mdl = mdl;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void setProcessedModel(Model processedModel) {
        this.processedModel = processedModel;
    }

    public Model getProcessedModel() {
        return processedModel;
    }
}

