package app;

import app.Network.TCPClient;
import app.Network.TCPServer;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            new Bdv("MATRIX_TEMPLATE");
        } catch (Exception e) {
            System.out.println("Error class -> " + e.toString());
            System.out.println("Stack:");
            e.printStackTrace();
        }
    }
}