package app.Network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {

    public static void listen(int port) throws Exception {
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("The server is running at port " + port);
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new SocketThreadInstance(listener.accept()));
            }
        }
    }

    private static class SocketThreadInstance implements Runnable {
        private final Socket socket;

        SocketThreadInstance(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                while (in.hasNextLine()) {
                    out.println(in.nextLine().toUpperCase());
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }
    }
}