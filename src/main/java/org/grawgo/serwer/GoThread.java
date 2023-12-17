package org.grawgo.serwer;

import org.grawgo.core.Board;
import org.grawgo.core.StoneColor;
import org.grawgo.serwer.stany.ColorPickingState;
import org.grawgo.serwer.stany.ThreadState;

import java.io.*;
import java.net.*;

public class GoThread extends Thread implements ServerCommandInterface {
    private final Socket socket;
    private final Board currBoard;
    private final ServerCommandParser serverParser;
    private ThreadState currState;
    StoneColor player = null;
    InputStream input;
    BufferedReader in;
    OutputStream output;
    public PrintWriter out;

    public GoThread(Socket socket) {
        this.socket = socket;
        // zaczytaj stan planszy z serwera (może można to zrobić jakoś lepiej)
        this.currBoard = GoServer.getBoard();
        this.serverParser = ServerCommandParser.getInstance();
        this.currState = new ColorPickingState(this);
        // w zaleznosci od tego jaki jest stan, ustaw responsy
    }

    public void run() {
        try {
            input = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            output = socket.getOutputStream();
            out = new PrintWriter(output, true);

            String command;
            String parsedCommand;
            String response;
            int[] coords;
            command = in.readLine();
            parsedCommand = serverParser.parseCommand(command);
            //TODO czekaj na 2 graczy
            //// STAN WAIT_FOR_PLAYERS
            if (parsedCommand.equals("white")) {
                currState.handleWhitePick();
            } else if (parsedCommand.equals("black")) {
                currState.handleBlackPick();
            }
            ////
            game:
            while (true) {
                //TODO: Sprawdz czy wlasciwa tura
                command = in.readLine();
                parsedCommand = serverParser.parseCommand(command);
                switch (parsedCommand) {
                    case "place":
                        // w colorpickingstate NIC, waitingstate nic, blackturn tylko jesli moj kolor to black
                        //TODO: wyswietl plansze po ruchu przeciwnika
                        coords = serverParser.parseCoords(command);
                        currBoard.placeStone(coords, player);
                        //TODO: znajdz zbite piony
                        response = serverParser.parseOutput(currBoard);
                        out.println(response);
                        break;
                    case "skip":
                        //TODO sprawdz czy poprzedni ruch to skip
                        response = currBoard.skip();
                        if (response.equals("SKIP_RESPONSE$0")) {
                            out.println(response);
                            System.out.println("skipped turn");
                        } else if (response.equals("END_GAME_RESPONSE$")) {
                            //TODO: policz wynik
                            //TODO: wyswietl wynik i zakoncz gre u obu graczy
                            response += "0|0";
                            out.println(response);
                            System.out.println("Ending game");
                            //TODO: lepszy exit
                            break game;
                        }
                        break;
                    case "exit":
                        response = "DISCONNECT_RESPONSE$0";
                        out.println(response);
                        System.out.println("client disconnected");
                        // TODO lepszy exit
                        break game; // <- czy to na pewno jest bezpieczne?
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
        }
    }

    public void setPlayer(StoneColor player) {
        this.player = player;
    }
    public String getPlayerString(){
        if(this.player == null){
            return "NULL";
        }else if (this.player.equals(StoneColor.BLACK)){
            return "BLACK";
        } else if (this.player.equals(StoneColor.WHITE)) {
            return "WHITE";
        }
        return "NULL";
    }

    public void changeState(ThreadState threadState) {
        this.currState = threadState;
    }

    @Override
    public void handleBlackPick() {
        currState.handleBlackPick();
    }

    @Override
    public void handleWhitePick() {
        currState.handleWhitePick();
    }

    @Override
    public void handlePlace() {
        currState.handlePlace();
    }

    @Override
    public void handleSkip() {
        currState.handleSkip();
    }

    @Override
    public void handleExit() {
        currState.handleExit();
    }

    @Override
    public void handleInvalid() {
        currState.handleInvalid();
    }
}