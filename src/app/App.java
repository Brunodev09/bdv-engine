package app;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            Bdv loader = new Bdv("bdv-engine-test", new int[] { 800, 600 }, 1, true);
            loader.exec("SHAPES_TEMPLATE");
        } catch (Exception e) {
            System.out.println("Error class -> " + e.toString());
            System.out.println("Stack:");
            e.printStackTrace();
        }
    }
}