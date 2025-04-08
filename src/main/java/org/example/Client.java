package org.example;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket client;

    public void initializeConnection(String host, int port) throws IOException {
        client = new Socket(host, port);
        System.out.println("Client was initialized");
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.initializeConnection("localhost", 8081);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
