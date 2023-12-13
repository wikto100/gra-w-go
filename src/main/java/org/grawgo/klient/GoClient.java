package org.grawgo.klient;

import java.net.*;
import java.io.*;

public class GoClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4444);
            PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(System.in));
            ClientCommandParser clientParser = new ClientCommandParser();
            String text;
            String input;
            String response;
            do {
                System.out.print("Input command: ");
                input = clientIn.readLine();
                text=clientParser.parseInput(input);
                clientOut.println(text);
                response = inFromServer.readLine();
                /////TODO: to powinna być robota ClientCommandParser
                String command = response.split("\\$")[0];
                String data = response.split("\\$")[1];
                /////
                switch (command) {
                    case "DISCONNECT_RESPONSE":
                        System.out.println("disconnecting...");
                        break;
                    case "INVALID_RESPONSE":
                        System.out.println("invalid command");
                        break;
                    case "PLACE_RESPONSE":
                        System.out.println("___________ TEST ________________");
                        /////TODO: to powinna być robota ClientCommandParser
                        String[] splitData = data.split("\\|");
                        for (String splitDatum : splitData) {
                            System.out.println(splitDatum);
                        }
                        /////
                        break;
                    default:
                        System.out.println("____________ TEST _______________ default");
                        break;
                }
            } while (!input.equals("exit"));
            socket.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}