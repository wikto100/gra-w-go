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
        this.currState = new ColorPickingState(this, null);
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
            ////
            game:
            while (true) {
                //TODO: Sprawdz czy wlasciwa tura
                command = in.readLine();
                parsedCommand = serverParser.parseCommand(command);
                switch (parsedCommand) {
                    case "white":
                        currState.handleWhitePick();
                    case "black":
                        currState.handleBlackPick();
                    case "place":
                        currState.handlePlace(command);
                        break;
                    case "skip":
                        // nie ruszam ci tego, przenieś do odp metod
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
                        currState.handleExit();
                        break game; // <- czy to na pewno jest bezpieczne?
                    default:
                        currState.handleInvalid();
                        break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // to wszystko niech idzie do jakiejs klasy ThreadUtil
    public void setPlayer(String player) {
        if (player.equals("black")) {
            this.player = StoneColor.BLACK;
        } else if (player.equals("white")) {
            this.player = StoneColor.WHITE;
        }
    }

    public String getPlayerString() {
        if (this.player == null) {
            return "NULL";
        } else if (this.player.equals(StoneColor.BLACK)) {
            return "BLACK";
        } else if (this.player.equals(StoneColor.WHITE)) {
            return "WHITE";
        }
        return "NULL";
    }

    public GoThread getOpponent() {
        return this.currState.getOtherPlayer();
    }

    public void changeState(ThreadState threadState) {
        this.currState = threadState;
    }

    @Override
    public void handleBlackPick() throws InterruptedException {
        currState.handleBlackPick();
    }

    @Override
    public void handleWhitePick() throws InterruptedException {
        currState.handleWhitePick();
    }

    @Override
    public void handlePlace(String command) {
        currState.handlePlace(command);
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