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
            StoneColor player=null;
            String command;
            String parsedCommand;
            String response;
            int row;
            int column;
            int coords[] = new int[2];
            command = in.readLine();
            parsedCommand=serverParser.parseCommand(command);
            //TODO do przeniesienia do board && nie thread-safe
            //TODO czekaj na 2 graczy
            if(parsedCommand.equals("white") && currBoard.whiteConnected==false){
                player=StoneColor.WHITE;
                currBoard.connect(parsedCommand);
                response = "JOIN_SUCCESFULL_RESPONSE$0";
                out.println(response);
                System.out.println("white player connected");
            }
            else if(parsedCommand.equals("black") && currBoard.blackConnected==false){
                player=StoneColor.BLACK;
                currBoard.connect(parsedCommand);
                response = "JOIN_SUCCESFULL_RESPONSE$0";
                out.println(response);
                System.out.println("black player connected");
            }
            else{
                //TODO nieprawidlowy wybor koloru
                System.out.println("color error placeholder");
            }
            label:
            while (true) {
                //TODO: Sprawdz czy wlasciwa tura
                command = in.readLine();
                parsedCommand=serverParser.parseCommand(command);
                switch (parsedCommand) {
                    case "place":
                        //TODO: wyswietl plansze po ruchu przeciwnika
                        coords=serverParser.parseData(command);
                        row=coords[0];
                        column=coords[1];
                        currBoard.placeStone(row,column,player);
                        //TODO: znajdz zbite piony
                        response=serverParser.parseOutput(currBoard);
                        out.println(response);
                        break;
                    case "skip":
                        //TODO sprawdz czy poprzedni ruch to skip
                        response = currBoard.skip();
                        if (response.equals("SKIP_RESPONSE$0")){
                            out.println(response);
                            System.out.println("skipped turn");
                        }
                        else if (response.equals("END_GAME_RESPONSE$")){
                            //TODO: policz wynik
                            response+="0|0";
                            out.println(response);
                            System.out.println("Ending game");
                            //TODO: lepszy exit
                            break label;
                        }
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