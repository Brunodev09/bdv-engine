package app;

import app.API.Script;
import app.API.ScriptGL;
import app.Core.BdvRuntime;
import app.Core.Configuration;
import app.Core.Engine;
import app.Math.Dimension;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;

// @TODO - Make a logger manager
// @TODO - Implement texture reading and spritesheet abstraction

public class Bdv {
    private String title;
    private Dimension dimension;
    private int scale;
    private int backgroundColor = 0x892D6F;
    private BdvRuntime bdvWin;

    public Bdv(String script) throws Exception {
        try {
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            Path filePath = Paths.get(currentDir.toString(), "src", "app", "Templates");
            File temp = new File(filePath.toString());
            String[] dataInDir = temp.list();
            String templateToLoad = "TemplateNotFound";
            for (String file : dataInDir) {
                if (file.split(".java")[0].equals(script)) templateToLoad = file.split(".java")[0];
            }
            if (templateToLoad.equals("TemplateNotFound"))
                throw new Exception("Template " + script + " not found in Templates folder.");
            Class<?> classReflection = Class.forName("app.Templates." + templateToLoad);
            Constructor<?> constructor = classReflection.getConstructor();
            // Could've done: clazz.getConstructor(String.class, Integer.class);
            // if I didn't have @NoArgsConstructor and instead had a constructor(String s, int i);

            Script pureInstanceConversion = null;
            ScriptGL instanceConversionGL = null;
            Object instance = constructor.newInstance();

            try {
                pureInstanceConversion = (Script) instance;
            } catch (Exception e) {
                instanceConversionGL = (ScriptGL) instance;
            }

            if (pureInstanceConversion != null) {
                this.scale = 1;
                this.title = "Default Window";
                this.dimension = new Dimension(pureInstanceConversion.resolution.width, pureInstanceConversion.resolution.height);
                this.bdvWin = new BdvRuntime(this.dimension.width, this.dimension.height, this.scale, this.title);
                this.bdvWin.frame.setResizable(false);
                this.bdvWin.frame.setTitle(title);
                this.bdvWin.frame.add(this.bdvWin);
                this.bdvWin.frame.pack();
                this.bdvWin.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.bdvWin.frame.setLocationRelativeTo(null);
                this.bdvWin.frame.setVisible(true);

                this.bdvWin.start();
                this.bdvWin.setTemplate(pureInstanceConversion);
            } else {
                Configuration config = new Configuration(instanceConversionGL.resolution.width,
                        instanceConversionGL.resolution.height,
                        60,
                        instanceConversionGL.windowTitle, instanceConversionGL);
                Engine.loop(config);
            }

        } catch (ClassNotFoundException | FileNotFoundException | IndexOutOfBoundsException e) {
            throw new Exception(e);
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