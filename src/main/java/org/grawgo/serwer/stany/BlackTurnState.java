package org.grawgo.serwer.stany;

import org.grawgo.core.StoneColor;
import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;

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
    public void handlePlace(String command) {
        int[] coords;
        String response;
        // tylko jesli czarny
        if (myPlayer.getPlayerString().equals("BLACK")) {
            // kod ktory jest w goThread (mniejwiecej)
            coords = serverParser.parseCoords(command);
            // to jest bardzo brzydkie
            GoServer.getBoard().placeStone(coords, StoneColor.BLACK);

            response = serverParser.parseOutput(GoServer.getBoard());
            myPlayer.out.println(response);
            // todo: po moim ruchu wyświetl przeciwnikowi plansze
            //otherPlayer.out.println(response);
            otherPlayer.changeState(new WhiteTurnState(otherPlayer, myPlayer));
            myPlayer.changeState(new WhiteTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleSkip() {
        // tylko jesli czarny
        if (myPlayer.getPlayerString().equals("BLACK")) {
            // kod ktory jest w goThread (mniejwiecej)

            //
            otherPlayer.changeState(new WhiteTurnState(otherPlayer, myPlayer));
            // todo: po moim ruchu wyświetl przeciwnikowi plansze
            myPlayer.changeState(new WhiteTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleExit() {
        // ok i disconnect
        myPlayer.out.println("DISCONNECT_RESPONSE$0");
    }

    @Override
    public void handleInvalid() {
        // jak wszedzie
        myPlayer.out.println("INVALID_RESPONSE$0");
        System.out.println("invalid command");
    }
}
