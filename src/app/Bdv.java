package app;
import app.API.Script;
import app.Core.BdvRuntime;
import app.Math.Dimension;
import app.Templates.GRID_TEMPLATE;
import app.Templates.SHAPES_TEMPLATE;

import javax.swing.JFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;

// @TODO - Make a logger manager
// @TODO - Implement texture reading and spritesheet abstraction

public class Bdv {
    private final BdvRuntime bdvWin;
    private String title;
    private Dimension dimension;
    private int scale;
    private int backgroundColor = 0x892D6F;

    public Bdv(String script) throws Exception {
        try {
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            Path filePath = Paths.get(currentDir.toString(), "src", "app", "Templates");
            File temp = new File(filePath.toString());
            String[] dataInDir = temp.list();
            String templateToLoad = "TemplateNotFound";
            for (String file : dataInDir) {
                if (file.contains(script)) templateToLoad = file.split(".java")[0];
            }
            if (templateToLoad.equals("TemplateNotFound")) throw new Exception("Template " + script + " not found in Templates folder.");
            Class<?> classReflection = Class.forName("app.Templates." + templateToLoad);
            Constructor<?> constructor = classReflection.getConstructor();
            // Could've done: clazz.getConstructor(String.class, Integer.class);
            // if I didn't have @NoArgsConstructor and instead had a constructor(String s, int i);
            Object instance = constructor.newInstance();
            Script instanceConversion = (Script) instance;

            this.scale = 1;
            this.title = "Default Window";
            this.dimension = new Dimension(instanceConversion.resolution.width, instanceConversion.resolution.height);
            this.bdvWin = new BdvRuntime(this.dimension.width, this.dimension.height, this.scale, this.title);
            this.bdvWin.frame.setResizable(false);
            this.bdvWin.frame.setTitle(title);
            this.bdvWin.frame.add(this.bdvWin);
            this.bdvWin.frame.pack();
            this.bdvWin.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.bdvWin.frame.setLocationRelativeTo(null);
            this.bdvWin.frame.setVisible(true);

            this.bdvWin.start();
            this.bdvWin.setTemplate(instanceConversion);
        } catch (ClassNotFoundException | FileNotFoundException | IndexOutOfBoundsException e) {
            throw new Exception(e);
        }

    }

    int[] getDimension() {
        return new int[] { this.dimension.width, this.dimension.height };
    }

    boolean setDimension(int[] dimension) throws Exception {
        if (dimension.length != 2) throw new Exception("Dimension must be an array with 2 integers.");
        this.dimension = new Dimension(dimension[0], dimension[1]);
        return true;
    }

    int getScale() {
        return this.scale;
    }

    boolean setScale(int scale) {
        this.scale = scale;
        this.bdvWin.setScale(this.scale);
        return true;
    }

    boolean setBackgroundColor(int color) {
        this.backgroundColor = color;
        return true;
    }

    long getBackgroundColor() {
        return this.backgroundColor;
    }

    String getTitle() {
        return this.title;
    }

    boolean setTitle(String title) {
        this.title = title;
        this.bdvWin.setTitle(this.title);
        return true;
    }

    boolean capFramRate(int fps) {
        this.bdvWin.setFps(fps);
        return true;
    }

}