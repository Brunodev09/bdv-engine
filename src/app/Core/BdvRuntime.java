package app.Core;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class BdvRuntime extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    public static int width;
    public static int height;
    public static int scale;
    public static String title;

    private Thread thread;
    public JFrame frame;
    private boolean running = false;

    private BufferedImage image;
    private int[] pixels;

    private int fps = 60;

    public BdvRuntime(int w, int h, int pScale, String pTitle) {
        width = w;
        height = h;
        scale = pScale;
        title = pTitle;
        Dimension size = new Dimension(width * scale, height * scale);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        setPreferredSize(size);

        load();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    private void load() {
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
        requestFocus();
        long before = System.currentTimeMillis();
        int counter = 0;
        while (running) {
            long now = System.currentTimeMillis();
            long delta = now - before;
            if (counter < this.fps && delta < 1000) {
                counter++;
//                System.out.println(counter + " , " + delta);
                frame.setTitle(title + " | " + counter  + " FPS");
                update();
                render();
            }
            else if (delta > 1000) {
                before = now;
                counter = 0;
            }
        }
        stop();
    }

    public void update() {
    }

    public void render() {
        BufferStrategy buffer = getBufferStrategy();
        if (buffer == null) {
            createBufferStrategy(3);
            return;
        }

        Arrays.fill(pixels, 0x17202A);

        Graphics display = buffer.getDrawGraphics();
        display.drawImage(image, 0, 0, getWidth(), getHeight(), null);

//        display.setColor(Color.BLACK);
//        display.fillRect(0, 0, getWidth(), getHeight());
        display.dispose();
        buffer.show();
    }


}