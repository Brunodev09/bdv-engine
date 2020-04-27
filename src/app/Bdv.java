package app;
import app.Core.BdvRuntime;
import app.Math.Dimension;

import javax.swing.JFrame;

public class Bdv {
    private BdvRuntime bdvWin;
    private String title;
    private Dimension dimension;
    private int scale;
    private boolean visible;

    public Bdv(String title, int[] dimension, int scale, boolean visible) throws Exception {
        if (dimension.length != 2) throw new Exception("Dimension must be an array with 2 integers.");
        this.scale = scale;
        this.title = title;
        this.dimension = new Dimension(dimension[0], dimension[1]);
        this.visible = visible;

        this.bdvWin = new BdvRuntime(this.dimension.width, this.dimension.height, scale, this.title);
        this.bdvWin.frame.setResizable(false);
        this.bdvWin.frame.setTitle(title);
        this.bdvWin.frame.add(this.bdvWin);
        this.bdvWin.frame.pack();
        this.bdvWin.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.bdvWin.frame.setLocationRelativeTo(null);
        this.bdvWin.frame.setVisible(visible);

        this.bdvWin.start();
    }

    boolean setBackgroundColor(String color) {
        return true;
    }

    boolean capFramRate(int fps) {
        return true;
    }

}