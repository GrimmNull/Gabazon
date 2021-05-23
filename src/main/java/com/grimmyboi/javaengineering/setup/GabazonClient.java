package com.grimmyboi.javaengineering.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GabazonClient {
    public static String sendCommandToServer(String request,Integer money) throws IOException {
        String serverAddress = "127.0.0.1";
        String response;
        int PORT = 8100;
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            out.println(money);
            response = in.readLine();
        } catch (UnknownHostException e) {
            response="No server listening... " + e;
        }
        return response;
    }
}
