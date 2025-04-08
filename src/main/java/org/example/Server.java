package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.example.MatrixRebuilder;

public class Server {
    private ServerSocket serverListener;
    private int clientNumber = 0;
    private boolean serverIsRunning = true;

    public void initialize(int port) throws IOException {
        serverListener = new ServerSocket(port);
        System.out.println("Server was initialized");
    }

    public void startListening() {
        System.out.println("Server start listening");
        while (serverIsRunning) {
            try {
                Socket client = serverListener.accept();
                System.out.println("Server get a client");
                clientNumber++;
                new Thread(() -> handleClient(client)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleClient(Socket client) {
        boolean hasConnection = true;
        int threadsAmount = 0;
        int matrixSize = 0;
        System.out.println("Client connected");

        try {
            while (hasConnection) {
                String command = RequestHandler.getMessage(client);
                switch (command) {
                    case "send threads amount":
                        threadsAmount = RequestHandler.getUnsignedInt(client, "Can not use a negative threads amount");
                        break;
                    case "send matrix size":
                        matrixSize = RequestHandler.getUnsignedInt(client, "Can not use a negative matrix size");
                        break;
                    case "end":
                        System.out.println("Client requested to end connection.");
                        client.shutdownInput();
                        client.shutdownOutput();
                        client.close();
                        hasConnection = false;
                        break;
                    default:
                        RequestHandler.sendMessage(client, "Can not understand a command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.initialize(8081);
            server.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
