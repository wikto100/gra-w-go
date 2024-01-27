package org.grawgo.serwer.stany;

import org.grawgo.core.StoneColor;
import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.db.GameLogger;

public class BlackTurnState extends ThreadState {
    public BlackTurnState(GoThread player1, GoThread player2) {
        super(player1, player2);
    }

    @Override
    public void handleBlackPick() {
        // nie
        handleInvalid();
    }

    @Override
    public void handleWhitePick() {
        // nie
        handleInvalid();
    }

    @Override
    synchronized public void handlePlace(String command) {
        int[] coords;
        String response;
        boolean success;
        // tylko jesli czarny
        if (myPlayer.getPlayerString().equals("black")) {
            coords = serverParser.parseCoords(command);
            success = GoServer.getBoard().placeStone(coords, StoneColor.BLACK, StoneColor.WHITE);

            if (success) {
                response = serverParser.parseBoard("PLACE_RESPONSE$");
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                GameLogger.logGame(GoServer.getBoard().getSize(),0,0);
                GameLogger.logBoard(GoServer.getBoard().getTurn(),GoServer.getBoard().printBoard());
                otherPlayer.changeState(new WhiteTurnState(otherPlayer, myPlayer));
                myPlayer.changeState(new WhiteTurnState(myPlayer, otherPlayer));
            } else {
                myPlayer.out.println("ILLEGAL_MOVE_RESPONSE$");
            }
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    synchronized public void handleSkip() {
        // tylko jesli czarny
        String response;
        if (myPlayer.getPlayerString().equals("black")) {
            response = GoServer.getBoard().skip();
            if (response.equals("SKIP_RESPONSE$")) {
                response = serverParser.parseBoard(response);
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("skipped turn");
            } else if (response.equals("DECIDING_RESPONSE$")) {
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("Deciding score");
                myPlayer.changeState(new DecidingState(myPlayer,otherPlayer));
                otherPlayer.changeState(new DecidingState(otherPlayer,myPlayer));
                return;
            }
            otherPlayer.changeState(new WhiteTurnState(otherPlayer, myPlayer));
            myPlayer.changeState(new WhiteTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleExit() throws InterruptedException {
        super.handleExit();
    }

    @Override
    public void handleInvalid() {
        super.handleInvalid();
    }

    @Override
    public void handleSizeChange(int size) {
        handleInvalid();
    }

    @Override
    public void handleLoad(int gameno) {
        handleInvalid();
    }

    @Override
    public void handlePrev() {
        handleInvalid();
    }

    @Override
    public void handleNext() {
        handleInvalid();
    }

    @Override
    public void handleDead(String command) {
        handleInvalid();
    }

    @Override
    public void handleConfirm() {
        handleInvalid();
    }
}
