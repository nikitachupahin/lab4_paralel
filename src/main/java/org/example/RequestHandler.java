package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    private static final int STANDARD_BUFFER_SIZE = 128;

    public static void sendMessage(Socket client, String message) throws IOException {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        client.getOutputStream().write(messageBytes);
        client.getOutputStream().flush();
    }

    public static String getMessage(Socket client) throws IOException {
        byte[] buffer = new byte[STANDARD_BUFFER_SIZE];
        int count = client.getInputStream().read(buffer);
        return new String(buffer, 0, count, StandardCharsets.UTF_8).trim().toLowerCase();
    }

    public static void sendIntValue(Socket client, int value) throws IOException {
        byte[] buffer = ByteBuffer.allocate(4).putInt(value).array();
        client.getOutputStream().write(buffer);
        client.getOutputStream().flush();
    }

    public static int getUnsignedInt(Socket client, String message) throws IOException {
        byte[] buffer = new byte[4];
        client.getInputStream().read(buffer);
        int val = ByteBuffer.wrap(buffer).getInt();
        sendMessage(client, val > 0 ? "data were recorded" : message);
        return val;
    }
}
