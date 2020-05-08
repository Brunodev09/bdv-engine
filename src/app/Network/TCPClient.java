package app.Network;

import java.util.Scanner;
import java.net.Socket;
import java.io.IOException;

public class TCPClient {
    public static void connect(String ip, int port) throws IOException {
        if (ip.length() <= 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        var socket = new Socket(ip, port);
        var in = new Scanner(socket.getInputStream());
        System.out.println("Server response: " + in.nextLine());
    }
}