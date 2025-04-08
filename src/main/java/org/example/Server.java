package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverListener;

    public void initialize(int port) throws IOException {
        serverListener = new ServerSocket(port);
        System.out.println("Server was initialized");
    }

    public void startListening(int serverBacklog) {
        System.out.println("Server start listening");
        while (true) {
            try {
                Socket client = serverListener.accept();
                System.out.println("Server get a client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.initialize(8081);
            server.startListening(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
