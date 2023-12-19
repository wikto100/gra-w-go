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
        if (myPlayer.getPlayerString().equals("black")) {
            // kod ktory jest w goThread (mniejwiecej)
            coords = serverParser.parseCoords(command);
            // to jest bardzo brzydkie
            GoServer.getBoard().placeStone(coords, StoneColor.BLACK, StoneColor.WHITE);

            response = serverParser.parseBoard("PLACE_RESPONSE$");
            myPlayer.out.println(response);
            otherPlayer.out.println(response);

            otherPlayer.changeState(new WhiteTurnState(otherPlayer, myPlayer));
            myPlayer.changeState(new WhiteTurnState(myPlayer, otherPlayer));

        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleSkip() {
        // tylko jesli czarny
        String response;
        if (myPlayer.getPlayerString().equals("black")) {
            response = GoServer.getBoard().skip();
            if (response.equals("SKIP_RESPONSE$0")) {
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("skipped turn");
            }
            else if (response.equals("END_GAME_RESPONSE$")) {
                //TODO: policz wynik
                //TODO: szansa odrzucenia konca gry
                //TODO: zakoncz gre u obu graczy
                response += GoServer.getBoard().getScores();
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("Ending game");
            }
            otherPlayer.changeState(new WhiteTurnState(otherPlayer, myPlayer));
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
