package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;

public class ColorPickingState extends ThreadState {
    public ColorPickingState(GoThread player1, GoThread player2) {
        super(player1, player2);
    }
    @Override
    public void handleBlackPick() throws InterruptedException {
        if (!GoServer.isBlackConnected()) {
            myPlayer.setPlayer("black");
            GoServer.connect("black");
            if (otherPlayer == null)
                otherPlayer = GoServer.getFirstWhiteSleeper();
            if (otherPlayer != null) {
                synchronized (otherPlayer) {
                    otherPlayer.notify();
                }
            } else {
                synchronized (myPlayer) {
                    myPlayer.wait();
                    otherPlayer = GoServer.findOther(myPlayer);
                }
            }
            myPlayer.out.println("JOIN_SUCCESSFUL_RESPONSE$0");
            System.out.println("black player connected");
            // Przejdź do stanu BlackTurn
            myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("JOIN_FAILED_RESPONSE$0");
        }
    }

    @Override
    public void handleWhitePick() throws InterruptedException {
        if (!GoServer.isWhiteConnected()) {
            myPlayer.setPlayer("white");
            GoServer.connect("white");
            if (otherPlayer == null)
                otherPlayer = GoServer.getFirstBlackSleeper();
            if (otherPlayer != null) {
                // jesli jest jakis czekający czarny gracz to go budzę
                synchronized (otherPlayer) {
                    otherPlayer.notify();
                }
            } else {
                // jak nie ma czekającego czarnego gracza to czekam aż czarny gracz mnie obudzi
                synchronized (myPlayer) {
                    myPlayer.wait();
                    otherPlayer = GoServer.findOther(myPlayer);
                }
            }
            //Thread.sleep(100); <- JESLI THREAD-0 sie WYWALA to odkomentowac to
            // znajdz kto mnie ma
            // czarny gracz mnie obudzil
            myPlayer.out.println("JOIN_SUCCESSFUL_RESPONSE$0");
            System.out.println("white player connected");
            // przejdź do stanu BlackTurn
            myPlayer.changeState(new BlackTurnState(myPlayer, otherPlayer));
        } else {
            myPlayer.out.println("JOIN_FAILED_RESPONSE$0");
        }
    }


    @Override
    public void handlePlace(String command) {
        myPlayer.out.println("COLOR_PICK_RESPONSE$" + myPlayer.getPlayerString());
    }

    @Override
    public void handleSkip() {
        myPlayer.out.println("COLOR_PICK_RESPONSE$" + myPlayer.getPlayerString());
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
