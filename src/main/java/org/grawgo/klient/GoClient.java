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
            String command;
            String color;
            System.out.println("Pick color");
            color=clientIn.readLine();
            text=clientParser.parseInput(color);
            clientOut.println(text);
            response = inFromServer.readLine();
            command = clientParser.parseResponse(response);
            //TODO: zle wybrany kolor
            if(command.equals("JOIN_SUCCESFULL_RESPONSE")){
                System.out.println("Playing as "+color);
            }
            do {
                System.out.print("Input command: ");
                input = clientIn.readLine();
                text=clientParser.parseInput(input);
                clientOut.println(text);
                response = inFromServer.readLine();
                command = clientParser.parseResponse(response);
                switch (command) {
                    case "DISCONNECT_RESPONSE":
                        System.out.println("disconnecting...");
                        break;
                    case "INVALID_RESPONSE":
                        System.out.println("invalid command");
                        break;
                    case "SKIP_RESPONSE":
                        System.out.println("skipped turn");
                        break;
                    case "PLACE_RESPONSE":
                        System.out.println("___________ TEST ________________");
                        clientParser.parseBoard(response);
                        break;
                    case "END_GAME_RESPONSE":
                        System.out.println("THE GAME HAS ENDED");
                        System.out.println("WHITE: "+clientParser.getData(response,0)+" BLACK: "+clientParser.getData(response,1));
                        input="exit";
                        break;
                    default:
                        System.out.println("____________ TEST _______________ default");
                        break;
                }
            } while (!input.equals("exit")); //Potencjalnie do zmiany
            socket.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}