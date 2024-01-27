package org.grawgo.klient;

import java.net.*;
import java.io.*;

public class GoClient {
    private static PrintWriter clientOut;
    private static BufferedReader inFromServer;
    private static BufferedReader clientIn;
    private static ClientCommandParser clientParser;
    private static Socket socket;
    private static ClientState joinedState = ClientState.SIZE;

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
        String userPrompt = "";
        switch (joinedState) {
            case SIZE:
                userPrompt = "Pick size ( size 19 | size 13 | size 9 ): ";
                break;
            case COLOR:
                userPrompt = "Pick color ( white | black ) [& wait] : ";
                break;
            case PLAYING:
                userPrompt = "Input move ( place x y | skip ): ";
                break;
            case GAME_LOAD:
                userPrompt = "Load game ( load x ) & ( prev | next | confirm ): ";
                break;
        }
        System.out.print(userPrompt);
        userInput = clientIn.readLine();
        parsedUserInput = clientParser.parseInputFromUser(userInput);
        clientOut.println(parsedUserInput);
        response = inFromServer.readLine();
        command = clientParser.parseCommandFromServer(response);
        switch (command) {
            case "SIZE_RESPONSE":
                System.out.println("Board size was set. GAMES: " + clientParser.parseBoardFromServer(response));
                joinedState = ClientState.GAME_LOAD;
                return true;
            case "SKIP_OPTION_RESPONSE":
                System.out.println("Skipping setup...");
                joinedState = ClientState.COLOR;
                return true;
            case "LOAD_RESPONSE":
                System.out.println("Game no. " + clientParser.parseDataFromServer(response, 0) + " loaded from db");
                return true;
            case "PREV_RESPONSE":
                System.out.println(clientParser.parseBoardFromServer(response));
                return true;
            case "NEXT_RESPONSE":
                System.out.println(clientParser.parseBoardFromServer(response));
                return true;
            case "CONFIRM_RESPONSE":
                System.out.println("confirmed");
                joinedState = ClientState.COLOR;
                return true;

            case "JOIN_SUCCESSFUL_RESPONSE":
                System.out.println("Playing as " + userInput);
                System.out.println(" __________________________ GO ___________________________");
                if (userInput.equals("white")) {
                    System.out.println("Waiting for black players move...");
                }
                response = inFromServer.readLine();
                if (clientParser.parseCommandFromServer(response).equals("DISCONNECT_RESPONSE")) {
                    System.out.println("OPPONENT DISCONNECTED");
                    return false;
                }
                System.out.print(clientParser.parseBoardFromServer(response));
                System.out.println(" __________________________ ** ___________________________");
                joinedState = ClientState.PLAYING;
                return true;
            case "JOIN_FAILED_RESPONSE":
                System.out.println("Select the other color!!");
                return true;
            case "DISCONNECT_RESPONSE":
                System.out.println("disconnecting...");
                return false;
            case "INVALID_RESPONSE":
                System.out.println("invalid command");
                return true;
            case "ILLEGAL_MOVE_RESPONSE":
                System.out.println("illegal move");
                return true;
            case "SKIP_RESPONSE":
                System.out.println("Skipped turn");
                System.out.println(" ___________________________ ** ___________________________");
                System.out.println("OK! Waiting for other players move...");
                response = inFromServer.readLine();
                if (clientParser.parseCommandFromServer(response).equals("END_GAME_RESPONSE")) {
                    System.out.println("THE GAME HAS ENDED");
                    System.out.println("WHITE: " + clientParser.parseDataFromServer(response, 0) + " BLACK: " + clientParser.parseDataFromServer(response, 1));
                    return false;
                } else if (clientParser.parseCommandFromServer(response).equals("DISCONNECT_RESPONSE")) {
                    System.out.println("OPPONENT DISCONNECTED");
                    return false;
                }

                System.out.println(clientParser.parseBoardFromServer(response));
                System.out.println(" __________________________ ** ___________________________");
                return true;
            case "PLACE_RESPONSE":
                System.out.print(clientParser.parseBoardFromServer(response));
                System.out.println(" ___________________________ ** ___________________________");
                System.out.println("OK! Waiting for other players move...");
                response = inFromServer.readLine();
                if (clientParser.parseCommandFromServer(response).equals("DISCONNECT_RESPONSE")) {
                    System.out.println("OPPONENT DISCONNECTED");
                    return false;
                }
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
            System.out.print(
                    "Instrukcja:\n" +
                    "1. Wybierz rozmiar planszy (size), albo pomiń ustawienia (skip)\n" +
                    "2. Załaduj grę z bazy (load x) (jeśli nie ma gier, wpisz load cokolwiek)\n" +
                    "3. Przewijaj tury z załadowanej gry (prev-poprzednia/next-następna)\n" +
                    "4. Potwierdź wybór tury (confirm)\n"+
                    "5. Wybierz kolor (white/black) i zaczekaj\n" +
                    "6. Ustawianie kamieni - (place x y)\n" +
                    "7. Pomijanie tury - (skip)\n" +
                    "8. Poddanie gry (exit)\n");

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