package com.bdv;

import com.bdv.api.BdvScript;
import com.bdv.exceptions.InvalidInstance;
import com.bdv.game.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
    private final Game game;

    public <T> Main(Class<T> script)
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            InvalidInstance {

        Constructor<T> constructor = script.getConstructor();
        T instance = constructor.newInstance();

        if (!(instance instanceof BdvScript)) {
            throw new InvalidInstance();
        }
        this.game = new Game((BdvScript) instance);
    }
}
