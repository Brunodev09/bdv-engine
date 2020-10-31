package com.bdv.renders.opengl;

import java.util.ArrayList;
import java.util.List;

public class OpenGLMemoryManager {
    private int vid;
    private List<Integer> vboPointers = new ArrayList<>();
    private int id;
    public static int globalId = 0;

    public OpenGLMemoryManager(int vid) {
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
