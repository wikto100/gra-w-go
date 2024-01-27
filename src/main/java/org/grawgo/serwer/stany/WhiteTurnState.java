package org.grawgo.serwer.stany;

import org.grawgo.core.StoneColor;
import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.db.GameLogger;

public class WhiteTurnState extends ThreadState {
    public WhiteTurnState(GoThread player1, GoThread player2) {
        super(player1, player2);
    }

    @Override
    public void handleBlackPick() {
        handleInvalid();
    }

    @Override
    public void handleWhitePick() {
        handleInvalid();
    }

    @Override
    synchronized public void handlePlace(String command) {
        int[] coords;
        String response;
        boolean success;
        if (myPlayer.getPlayerString().equals("white")) {
            coords = serverParser.parseCoords(command);
            success = GoServer.getBoard().placeStone(coords, StoneColor.WHITE, StoneColor.BLACK);
            if(success){
                response = serverParser.parseBoard("PLACE_RESPONSE$");
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                GameLogger.logBoard(GoServer.getBoard().getTurn(),GoServer.getBoard().printBoard());
                otherPlayer.changeState(new BlackTurnState(otherPlayer, myPlayer));
                myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
            }
            else{
                myPlayer.out.println("ILLEGAL_MOVE_RESPONSE$");
            }
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    synchronized public void handleSkip() {
        String response;
        if (myPlayer.getPlayerString().equals("white")) {
            response = GoServer.getBoard().skip();
            if (response.equals("SKIP_RESPONSE$")) {
                response = serverParser.parseBoard(response);
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("skipped turn");
            } else if (response.equals("END_GAME_RESPONSE$")) {
                response += GoServer.getBoard().getScores();
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("Ending game");
                myPlayer.setRunning(false);
                otherPlayer.setRunning(false);
                return;
            }
            otherPlayer.changeState(new BlackTurnState(otherPlayer, myPlayer));
            myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
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
    public void handleConfirm() {
        handleInvalid();
    }
}
