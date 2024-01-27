package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.ServerCommandInterface;
import org.grawgo.serwer.db.GameLogger;

public class DecidingState extends ThreadState {
    public DecidingState(GoThread myPlayer, GoThread otherPlayer) {
        super(myPlayer, otherPlayer);
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
        String response;
        GoServer.getBoard().remove(serverParser.parseCoords(command));
        response = serverParser.parseBoard("DEAD_RESPONSE$");
        myPlayer.out.println(response);
        otherPlayer.out.println(response);
    }

    @Override
    public void handleConfirm() {
        String response = "END_GAME_RESPONSE$";
        response = response + GameLogger.getCurrGameID() + "|" + GoServer.getBoard().getScores();
        myPlayer.out.println(response);
        otherPlayer.out.println(response);
        GameLogger.logScores(GoServer.getBoard().getBlackPoints(), GoServer.getBoard().getWhitePoints());
        myPlayer.setRunning(false);
        otherPlayer.setRunning(false);
    }

    @Override
    public void handleBlackPick() throws InterruptedException {
        handleInvalid();
    }

    @Override
    public void handleWhitePick() throws InterruptedException {
        handleInvalid();
    }

    @Override
    public void handlePlace(String command) {
        handleInvalid();
    }

    @Override
    public void handleSkip() {
        handleInvalid();
    }

    @Override
    public void handleExit() throws InterruptedException {
        super.handleExit();
    }

    @Override
    public void handleInvalid() {
        super.handleInvalid();
    }
}
