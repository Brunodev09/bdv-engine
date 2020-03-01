package app;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;

public class Window extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;

    private Thread thread;
    public JFrame frame;
    private boolean running = false;

    public Window() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);

        frame = new JFrame();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Graphics");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(running) {

        }
    }


}