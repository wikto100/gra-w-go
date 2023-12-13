package org.grawgo.serwer;

import org.grawgo.core.Board;
import org.grawgo.core.StoneColor;
import org.grawgo.exc.IllegalMoveException;

import java.io.*;
import java.net.*;

public class GoThread extends Thread {
    private Socket socket;
    private Board currBoard;
    private ServerCommandParser serverParser = ServerCommandParser.getInstance();
    public GoThread(Socket socket) {

        this.socket = socket;
        // zaczytaj stan planszy z serwera (może można to zrobić jakoś lepiej)
        this.currBoard = GoServer.getBoard();
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter out = new PrintWriter(output, true);
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
                switch (parsedCommand) {
                    case "place":
                        coords=serverParser.parseData(command);
                        row=coords[0];
                        column=coords[1];
                        currBoard.placeStone(row,column,StoneColor.BLACK);
                        //TODO: znajdz zbite piony
                        response=serverParser.parseOutput(currBoard);
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