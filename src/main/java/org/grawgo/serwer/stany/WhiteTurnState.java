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
        if (myPlayer.getPlayerString().equals("white")) {
            // kod ktory jest w goThread (mniejwiecej)
            coords = serverParser.parseCoords(command);
            // to jest bardzo brzydkie
            GoServer.getBoard().placeStone(coords, StoneColor.WHITE, StoneColor.BLACK);

            response = serverParser.parseBoard("PLACE_RESPONSE$");
            myPlayer.out.println(response);
            otherPlayer.out.println(response);

            otherPlayer.changeState(new BlackTurnState(otherPlayer, myPlayer));
            myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("INVALID_TURN_RESPONSE$");
        }
    }

    @Override
    public void handleSkip() {
        String response;
        if (myPlayer.getPlayerString().equals("white")) {
            response = GoServer.getBoard().skip();
            if (response.equals("SKIP_RESPONSE$0")) {
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("skipped turn");
            }
            else if (response.equals("END_GAME_RESPONSE$")) {
                //TODO: skip dziala koniec gry jest do zrobienia
                response += GoServer.getBoard().getScores();
                myPlayer.out.println(response);
                otherPlayer.out.println(response);
                System.out.println("Ending game");
            }
            otherPlayer.changeState(new BlackTurnState(otherPlayer, myPlayer));
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
