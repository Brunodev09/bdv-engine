package com.bdv.ECS;

import java.util.logging.Logger;

public class Component <T> {
    public static int nextId = 1;
    public Logger logger = Logger.getLogger(getClass().getName());

    public <T> Component() {
        ++nextId;
    }

    public static <T> int getId() {
        return nextId++;
    }

    public Logger getLogger() {
        return logger;
    }
}
