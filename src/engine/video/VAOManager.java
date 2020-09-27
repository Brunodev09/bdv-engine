package engine.video;

import java.util.ArrayList;
import java.util.List;

public class VAOManager {
    private int vid;
    private List<Integer> vboPointers = new ArrayList<>();
    private int id;
    public static int globalId = 0;

    public VAOManager(int vid) {
        this.vid = vid;
        globalId++;
        this.id = globalId;
    }

    public int getVid() {
        return vid;
    }

    public List<Integer> getVboPointers() {
        return vboPointers;
    }

    public void addVbo(int vboId) {
        this.vboPointers.add(vboId);
    }

    public int getId() {
        return id;
    }
}
