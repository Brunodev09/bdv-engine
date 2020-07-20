package app.Video;

/*
 ----OBJ file format example----
 Vertexes P(x, y, z)
    v 3.227124 -0.065127 -1.000000
    v 3.227124 -0.065127 1.000000
    v -3.227125 -0.065127 1.000000
 Textures P(x, y)
    vt 0.533523 0.942320
    vt 0.905299 0.919749
    vt 0.905299 0.942320
 Normals P(x, y, z)
    vn -0.6862 -0.6100 0.3962
    vn -0.6862 -0.6100 -0.3962
    vn -0.7566 0.4865 0.4368
    vn -0.7566 0.4865 -0.4368
 Faces (vertex/texture/normal)
    f 38/1/1 45/2/1 41/3/1
    f 27/4/2 40/5/2 31/6/2
    f 5/7/3 2/8/3 1/9/3

    Mapping line 17 -> Triangle(38, 45, 41), TextureCoords(1, 2, 3), Normals(1, 1, 1)
    Mapping line 17 -> Triangle((x,y,z),(x',y',z'),(x'',y'',z'')) TextureCoords((x,y),(x',y')),
                       Normals((x,y,z),(x',y',z'),(x'',y'',z''))
*/

import app.Models.BufferedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelParser {
    private static final String _EXT = ".obj";

    public static BufferedModel parseOBJ(String file) {
        try {
            FileReader reader = new FileReader("src/res/" + file + _EXT);
            BufferedReader bf = new BufferedReader(reader);
            String line;
            List<Vector3f> vertexList = new ArrayList<>();
            List<Vector2f> textureCoordsList = new ArrayList<>();
            List<Vector3f> normalsList = new ArrayList<>();
            List<Integer> indexesList = new ArrayList<>();

            float[] vertexArr = null;
            float[] texturesArr = null;
            float[] normalsArr = null;

            int[] indexesArr = null;

            while (true) {
                line = bf.readLine();
                String[] curr = line.split(" ");

                // If its a vertex
                if (line.startsWith("v ")) {
                    vertexList.add(
                            new Vector3f(
                                    Float.parseFloat(curr[1]),
                                    Float.parseFloat(curr[2]),
                                    Float.parseFloat(curr[3])));
                }
                // If its a texture coordinate
                else if (line.startsWith("vt ")) {
                    textureCoordsList.add(
                            new Vector2f(
                                    Float.parseFloat(curr[1]),
                                    Float.parseFloat(curr[2])));
                }
                // If its a normal vector coordinate
                else if (line.startsWith("vn ")) {
                    normalsList.add(
                            new Vector3f(
                                    Float.parseFloat(curr[1]),
                                    Float.parseFloat(curr[2]),
                                    Float.parseFloat(curr[3])));
                }
                // If its a face
                else if (line.startsWith("f ")) {
                    texturesArr = new float[vertexList.size() * 2];
                    normalsArr = new float[vertexList.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bf.readLine();
                    continue;
                }

                List<String[]> triangles = new ArrayList<>();
                triangles.add(line.split(" ")[1].split("/"));
                triangles.add(line.split(" ")[2].split("/"));
                triangles.add(line.split(" ")[3].split("/"));

                for (String[] tri : triangles) {
                    _parseVertex(tri, indexesList, textureCoordsList, normalsList, texturesArr, normalsArr);
                }
                triangles = null;

                line = bf.readLine();
            }

            bf.close();

            vertexArr = new float[vertexList.size() * 3];
            indexesArr = new int[indexesList.size()];

            int vertexPtr = 0;
            for (Vector3f vertex : vertexList) {
                vertexArr[vertexPtr++] = vertex.x;
                vertexArr[vertexPtr++] = vertex.y;
                vertexArr[vertexPtr++] = vertex.z;
            }

            int indexPtr = 0;
            for (int index : indexesList) {
                indexesArr[indexPtr] = indexesList.get(indexPtr);
                indexPtr++;
            }

            return new BufferedModel(vertexArr, texturesArr, normalsArr, indexesArr);

        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    private static void _parseVertex(String[] vertexData,
                                     List<Integer> indexes,
                                     List<Vector2f> textures,
                                     List<Vector3f> normals,
                                     float[] textureArr,
                                     float[] normalsArr) {
        int currVtxPointer = Integer.parseInt(vertexData[0]) - 1;
        indexes.add(currVtxPointer);
        Vector2f currTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArr[currVtxPointer * 2] = currTex.x;
        textureArr[currVtxPointer * 2 + 1] = 1 - currTex.y;
        Vector3f currNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArr[currVtxPointer * 3] = currNormal.x;
        normalsArr[currVtxPointer * 3 + 1] = currNormal.y;
        normalsArr[currVtxPointer * 3 + 2] = currNormal.z;
    }
}

