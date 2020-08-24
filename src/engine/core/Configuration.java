package engine.core;

import engine.api.BdvScriptGL;

public class Configuration {
    public final int WIDTH;
    public final int HEIGHT;
    public final int FPS;
    public final String TITLE;
    public final BdvScriptGL script;

    public Configuration(int WIDTH, int HEIGHT, int FPS, String TITLE, BdvScriptGL script) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.FPS = FPS;
        this.TITLE = TITLE;
        this.script = script;
    }

    public Configuration() {
        this.WIDTH = 800;
        this.HEIGHT = 600;
        this.FPS = 60;
        this.TITLE = "Unnamed video service";
        this.script = null;
    }

}
