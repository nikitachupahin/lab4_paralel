package org.example;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client;
    private int matrixSize;
    private int threadsAmount;

    public void initializeConnection(String host, int port) throws IOException {
        client = new Socket(host, port);
        System.out.println("Client was initialized");
    }

    public void startUI() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean hasConnection = true;

        while (hasConnection) {
            System.out.println("Enter command to server:");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "send threads amount":
                    sendThreadsAmount(scanner);
                    break;
                case "send matrix size":
                    sendMatrixSize(scanner);
                    break;
                case "end":
                    RequestHandler.sendMessage(client, "end");
                    hasConnection = false;
                    break;
                default:
                    RequestHandler.sendMessage(client, command);
                    System.out.println("Response: " + RequestHandler.getMessage(client));
            }
        }
    }

    private void sendThreadsAmount(Scanner scanner) throws IOException {
        RequestHandler.sendMessage(client, "send threads amount");
        System.out.println("Enter threads amount: ");
        while (!scanner.hasNextInt()) {
            System.out.println("You need to enter a number, try again: ");
            scanner.next();
        }
        threadsAmount = scanner.nextInt();
        scanner.nextLine();
        RequestHandler.sendIntValue(client, threadsAmount);
        System.out.println("Response: " + RequestHandler.getMessage(client));
    }

    private void sendMatrixSize(Scanner scanner) throws IOException {
        RequestHandler.sendMessage(client, "send matrix size");
        System.out.println("Enter matrix size: ");
        while (!scanner.hasNextInt()) {
            System.out.println("You need to enter a number, try again: ");
            scanner.next();
        }
        matrixSize = scanner.nextInt();
        scanner.nextLine();
        RequestHandler.sendIntValue(client, matrixSize);
        System.out.println("Client send matrix size");
        System.out.println("Response: " + RequestHandler.getMessage(client));
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.initializeConnection("localhost", 8081);
            client.startUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
