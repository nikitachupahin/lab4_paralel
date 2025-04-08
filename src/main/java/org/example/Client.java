package org.example;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private Socket client;
    private int[][] matrix;
    private int matrixSize;
    private int threadsAmount;

    public void initializeConnection(String host, int port) throws IOException {
        client = new Socket(host, port);
        System.out.println("Client was initialized");
    }

    public void initializeMatrix(int matrixSize, int matrixMinValue, int matrixMaxValue) {
        this.matrixSize = matrixSize;
        this.matrix = new int[matrixSize][matrixSize];
        Random random = new Random();
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrix[i][j] = random.nextInt(matrixMaxValue - matrixMinValue) + matrixMinValue;
            }
        }
    }

    public void startUI() throws IOException {
        if (client == null) throw new IllegalStateException("Client is not initialized");

        printAvailableCommands();

        boolean hasConnection = true;
        Scanner scanner = new Scanner(System.in);

        while (hasConnection) {
            System.out.println("Enter command to server:");
            String messageToServer = scanner.nextLine().trim().toLowerCase();
            switch (messageToServer) {
                case "send threads amount":
                    sendThreadsAmount(scanner);
                    break;
                case "send matrix size":
                    sendMatrixSize(scanner);
                    break;
                case "send matrix":
                    sendMatrix();
                    break;
                case "end":
                    RequestHandler.sendMessage(client, "end");
                    hasConnection = false;
                    break;
                case "help":
                    printAvailableCommands();
                    break;
                default:
                    RequestHandler.sendMessage(client, messageToServer);
                    System.out.println("Response: " + RequestHandler.getMessage(client));
                    break;
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
        if (matrixSize > 0) {
            initializeMatrix(matrixSize, 0, 1000);
        }
    }

    private void sendMatrix() throws IOException {
        if (matrixSize <= 0) {
            System.out.println("You need to send a correct matrix size before");
            return;
        }
        RequestHandler.sendMessage(client, "send matrix");
        RequestHandler.sendMatrix(client, matrix);
        System.out.println("Client send matrix");
        System.out.println("Response: " + RequestHandler.getMessage(client));
    }

    private void printAvailableCommands() {
        System.out.println("\nAvailable commands:");
        System.out.println("  send threads amount - Set the number of threads");
        System.out.println("  send matrix size     - Set the size of the matrix");
        System.out.println("  send matrix          - Send generated matrix to server");
        System.out.println("  end                  - Close connection");
        System.out.println("  help                 - Show commands again\n");
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
