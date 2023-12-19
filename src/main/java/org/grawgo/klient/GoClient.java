package org.grawgo.klient;

import java.net.*;
import java.io.*;

public class GoClient {
    private static PrintWriter clientOut;
    private static BufferedReader inFromServer;
    private static BufferedReader clientIn;
    private static ClientCommandParser clientParser;
    private static Socket socket;
    private static boolean joinedFlag = false;
    private static void joinServer() throws IOException {
        socket = new Socket("localhost", 4444);
        clientOut = new PrintWriter(socket.getOutputStream(), true);
        inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        clientIn = new BufferedReader(new InputStreamReader(System.in));
        clientParser = new ClientCommandParser();
    }

    private static boolean play() throws IOException {
        String userInput;
        String parsedUserInput;
        String response;
        String command;
        String userPrompt = joinedFlag ? "Input move: ": "Pick color (&wait): ";
        System.out.print(userPrompt);
        userInput = clientIn.readLine();
        parsedUserInput = clientParser.parseInputFromUser(userInput);
        clientOut.println(parsedUserInput);
        response = inFromServer.readLine();
        command = clientParser.parseCommandFromServer(response);
        switch (command) {
            case "JOIN_SUCCESSFUL_RESPONSE":
                System.out.println("Playing as " + userInput);
                System.out.println(" __________________________ GO ___________________________");
                if(userInput.equals("white")){
                    System.out.println("Waiting for black players move...");
                }
                response = inFromServer.readLine();
                System.out.print(clientParser.parseBoardFromServer(response));
                System.out.println(" __________________________ ** ___________________________");
                joinedFlag = true;
                return true;
            case "JOIN_FAILED_RESPONSE":
                System.out.println("Select the other color!!");
                return true;
            case "COLOR_PICK_RESPONSE":
                System.out.println("Pick black/white!!");
                return true;
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
                System.out.print(clientParser.parseBoardFromServer(response));
                System.out.println(" ___________________________ ** ___________________________");
                System.out.println("OK! Waiting for other players move...");
                response = inFromServer.readLine();
                System.out.print(clientParser.parseBoardFromServer(response));
                System.out.println(" __________________________ ** ___________________________");
                return true;
            case "INVALID_TURN_RESPONSE":
                System.out.println("Not your turn!");
                return true;
            case "END_GAME_RESPONSE":
                System.out.println("THE GAME HAS ENDED");
                System.out.println("WHITE: " + clientParser.parseDataFromServer(response, 0) + " BLACK: " + clientParser.parseDataFromServer(response, 1));
                return false;
            default:
                System.out.println("ERROR UNHANDLED:" + command);
                return true;
        }

    }

    public static void main(String[] args) {
        boolean isPlaying;
        try {
            joinServer();
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