package com.bdv.ECS;

public class Component <T> {
    public static int nextId = 0;

    public <T> Component() {

    }

    public static <T> int getId() {
        return nextId++;
    }
}
