package app;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            Bdv loader = new Bdv("bdv-engine-test", new int[] {800, 600}, 1, true);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}