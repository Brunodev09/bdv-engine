package app;
import javax.swing.JFrame;

public class Loader {
    public Loader(Window win) {
        win.frame.setResizable(false);
        win.frame.setTitle("bdv-engine");
        win.frame.add(win);
        win.frame.pack();
        win.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.frame.setLocationRelativeTo(null);
        win.frame.setVisible(true);
    }
}