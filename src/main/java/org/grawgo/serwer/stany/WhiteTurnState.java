package org.grawgo.serwer.stany;

import org.grawgo.core.StoneColor;
import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;

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
    public void handlePlace(String command) {
        int[] coords;
        String response;
        if (myPlayer.getPlayerString().equals("WHITE")) {
            // kod ktory jest w goThread (mniejwiecej)
            coords = serverParser.parseCoords(command);
            // to jest bardzo brzydkie
            GoServer.getBoard().placeStone(coords, StoneColor.WHITE);
            response = serverParser.parseOutput(GoServer.getBoard());
            myPlayer.out.println(response);
            // todo: po moim ruchu wyświetl przeciwnikowi plansze
            // otherPlayer.out.println(response);

            otherPlayer.changeState(new BlackTurnState(otherPlayer, myPlayer));
            myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleSkip() {
        if (myPlayer.getPlayerString().equals("WHITE")) {
            // kod ktory jest w goThread (mniejwiecej)

            //
            otherPlayer.changeState(new BlackTurnState(otherPlayer, myPlayer));
            // todo: po moim ruchu wyświetl przeciwnikowi plansze
            myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleExit() {
        myPlayer.out.println("DISCONNECT_RESPONSE$0");
        System.out.println("client disconnected");
    }

    @Override
    public void handleInvalid() {
        myPlayer.out.println("INVALID_RESPONSE$0");
        System.out.println("invalid command");
    }
}
