package org.grawgo.klient;

import java.net.*;
import java.io.*;

public class GoClient {
    private static PrintWriter clientOut;
    private static BufferedReader inFromServer;
    private static BufferedReader clientIn;
    private static ClientCommandParser clientParser;
    private static Socket socket;
    private static void joinServer() throws IOException{
        socket = new Socket("localhost", 4444);
        clientOut = new PrintWriter(socket.getOutputStream(), true);
        inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        clientIn = new BufferedReader(new InputStreamReader(System.in));
        clientParser = new ClientCommandParser();
    }
    private static void pickColor() throws IOException{
        String parsedUserInput;
        String color;
        String response;
        String command;
        System.out.println("Pick color (& wait): ");
        color = clientIn.readLine();
        parsedUserInput = clientParser.parseInputFromUser(color);
        clientOut.println(parsedUserInput);
        response = inFromServer.readLine();
        command = clientParser.parseCommandFromServer(response);
        //TODO: zle wybrany kolor
        if (command.equals("JOIN_SUCCESSFUL_RESPONSE")) {
            System.out.println("Playing as " + color);
        } else if (command.equals("JOIN_FAILED_RESPONSE")) {
            System.out.println("Select the other color: ");
        } else if (command.equals("COLOR_PICK_RESPONSE")) {
            System.out.println("Pick black/white: ");
        }
    }
    private static boolean play() throws IOException{
        String userInput;
        String parsedUserInput;
        String response;
        String command;
        System.out.print("Input move: ");
        userInput = clientIn.readLine();
        parsedUserInput = clientParser.parseInputFromUser(userInput);
        clientOut.println(parsedUserInput);
        response = inFromServer.readLine();
        command = clientParser.parseCommandFromServer(response);
        switch (command) {
            case "DISCONNECT_RESPONSE":
                System.out.println("disconnecting...");
                return false;
            case "INVALID_RESPONSE":
                System.out.println("invalid command");
                return true;
            case "SKIP_RESPONSE":
                System.out.println("skipped turn");
                return true;
            case "PLACE_RESPONSE":
                System.out.println("___________ TEST ________________");
                clientParser.parseBoardFromServer(response);
                return true;
            case "INVALID_TURN_RESPONSE":
                System.out.println("Not your turn! ");
                return true;
            case "END_GAME_RESPONSE":
                System.out.println("THE GAME HAS ENDED");
                System.out.println("WHITE: " + clientParser.parseDataFromServer(response, 0) + " BLACK: " + clientParser.parseDataFromServer(response, 1));
                return false;
            default:
                System.out.println("_____________UNHANDLED:"+ command);
                return true;
        }

    }
    public static void main(String[] args) {
        boolean isPlaying;
        try {
            joinServer();
            pickColor();
            do {
                isPlaying = play();
            } while (isPlaying); //Potencjalnie do zmiany
            socket.close();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}