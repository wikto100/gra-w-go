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
            ServerCommandParser serverParser = ServerCommandParser.getInstance();
            String command;
            String parsedCommand;
            String response;
            int row;
            int column;
            int coords[] = new int[2];
            label:
            while (true) {
                command = in.readLine();
                parsedCommand=serverParser.parseCommand(command);
                System.out.println(command);
                switch (parsedCommand) {
                    case "place":
                        coords=serverParser.parseData(command);
                        row=coords[0];
                        column=coords[1];
                        // TODO: sprawdz legalnosc ruchu
                        currBoard.placeStone(row,column,StoneColor.BLACK);
                        ////TODO: to powinna być robota ServerCommandParser (syntax: RESPONSE$data|data|data)
                        response = "PLACE_RESPONSE$";
                        // tutaj regex to komenda$rzad_planszy|rzad_planszy|rzad_planszy|rzad_planszy|
                        // zrob to przez stringbuildera
                        response += currBoard.printBoard();
                        ////
                        out.println(response);
                        break;
                    case "exit":
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