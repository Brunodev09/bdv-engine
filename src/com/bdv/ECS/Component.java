package com.bdv.ECS;

import java.util.logging.Logger;


public abstract class Component <T> {
    public static int nextId = 0;
    public Logger logger = Logger.getLogger(getClass().getName());

    public <T> Component() {
    }

    public static <T> int getId() {
        return nextId;
    }

    public static <T> int getNextId() {
        return nextId + 1;
    }

    public Logger getLogger() {
        return logger;
    }
}
