package org.grawgo.serwer;


import org.grawgo.core.Board;
import org.grawgo.core.StoneColor;
import org.grawgo.exc.IllegalMoveException;

import java.io.*;
import java.net.*;

public class GoThread extends Thread {
    // TODO: każdy wątek powinien mieć dostęp do CommandParser jako singletonu
    private Socket socket;
    private Board currBoard;
    public GoThread(Socket socket) {

        this.socket = socket;
        // zaczytaj stan planszy z serwera (może można to zrobić jakoś lepiej)
        this.currBoard = GoServer.getBoard();
    }

    /**
     * Glowna funkcja watku
     */
    public void run() {
        try {

            InputStream input = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter out = new PrintWriter(output, true);
            String command;
            String response;
            label:
            while (true) {
                command = in.readLine();
                System.out.println(command);
                switch (command) {
                    case "place":
                        // TODO: wymyśl jakiś regex i parser dla komend wysyłanych z klienta
                        currBoard.placeStone(1,2, StoneColor.BLACK);
                        currBoard.placeStone(3,4, StoneColor.BLACK);
                        currBoard.placeStone(5,6, StoneColor.WHITE);

                        ////TODO: to powinna być robota ServerCommandParser (syntax: RESPONSE$data|data|data)
                        response = "PLACE_RESPONSE$";
                        // tutaj regex to komenda$rzad_planszy|rzad_planszy|rzad_planszy|rzad_planszy|
                        // zrob to przez stringbuildera
                        response += currBoard.printBoard();
                        ////
                        out.println(response);
                        break;
                    case "exit$": //zmieniony az dodam server parser
                        response = "DISCONNECT_RESPONSE$0";
                        out.println(response);
                        System.out.println("client disconnected");
                        // TODO lepszy exit
                        break label; // <- czy to na pewno jest bezpieczne?
                    default:
                        response = "INVALID_RESPONSE$0";
                        out.println(response);
                        System.out.println("invalid command");
                        break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IllegalMoveException e) {
            throw new RuntimeException(e);
        }
    }
}