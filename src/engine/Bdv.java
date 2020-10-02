package engine;

import engine.api.BdvScript;
import engine.api.BdvScriptGL;
import engine.core.BdvRuntime;
import engine.core.Configuration;
import engine.core.Engine;
import engine.math.Dimension;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Bdv {
    private String title;
    private Dimension dimension;
    private int scale;
    private int backgroundColor = 0x892D6F;
    private BdvRuntime bdvWin;
    private final Engine engine = new Engine();

    public Bdv(Class<?> yourScriptClass) throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {

        Constructor<?> constructor = yourScriptClass.getConstructor();
        BdvScript pureInstanceConversion = null;
        BdvScriptGL instanceConversionGL = null;
        Object instance = constructor.newInstance();

        try {
            pureInstanceConversion = (BdvScript) instance;
        } catch (Exception e) {
            instanceConversionGL = (BdvScriptGL) instance;
        }

        if (pureInstanceConversion != null) {
            this.scale = 1;
            this.title = "Default Bdv Window";
            this.dimension = new Dimension(pureInstanceConversion.resolution.width, pureInstanceConversion.resolution.height);
            this.bdvWin = new BdvRuntime(this.dimension.width, this.dimension.height, this.scale, this.title);
            this.bdvWin.frame.setResizable(false);
            this.bdvWin.frame.setTitle(title);
            this.bdvWin.frame.add(this.bdvWin);
            this.bdvWin.frame.pack();
            this.bdvWin.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.bdvWin.frame.setLocationRelativeTo(null);
            this.bdvWin.frame.setVisible(true);

            this.bdvWin.setTemplate(pureInstanceConversion);
            this.bdvWin.start();
        } else if (instanceConversionGL != null) {
            instanceConversionGL.FPS = instanceConversionGL.FPS == 0 ? 60 : instanceConversionGL.FPS;
            instanceConversionGL.windowTitle = instanceConversionGL.windowTitle == null ? "Default Bdv Window" : instanceConversionGL.windowTitle;
            Configuration config = new Configuration(instanceConversionGL.resolution.width,
                    instanceConversionGL.resolution.height,
                    instanceConversionGL.FPS,
                    instanceConversionGL.windowTitle, instanceConversionGL);
            engine.loop(config);
        }
    }

    int[] getDimension() {
        return new int[]{this.dimension.width, this.dimension.height};
    }

    void setDimension(int[] dimension) throws Exception {
        if (dimension.length != 2) throw new Exception("Dimension must be an array with 2 integers.");
        this.dimension = new Dimension(dimension[0], dimension[1]);
    }

    int getScale() {
        return this.scale;
    }

    void setScale(int scale) {
        this.scale = scale;
    }

    void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    long getBackgroundColor() {
        return this.backgroundColor;
    }

    String getTitle() {
        return this.title;
    }

    void setTitle(String title) {
        this.title = title;
    }

}