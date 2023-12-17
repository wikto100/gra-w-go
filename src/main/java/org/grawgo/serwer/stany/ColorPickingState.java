package org.grawgo.serwer.stany;

import org.grawgo.core.StoneColor;
import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;

public class ColorPickingState extends ThreadState {
    public ColorPickingState(GoThread goThread) {
        super(goThread);
    }

    @Override
    public void handleBlackPick() {
        if (!GoServer.isBlackConnected()) {
            goThread.setPlayer(StoneColor.BLACK);
            GoServer.connect("black");

            // TEST
            try {
                synchronized(goThread){
                goThread.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            goThread.out.println("JOIN_SUCCESSFUL_RESPONSE$0");
            System.out.println("black player connected");
            //
        } else {
            goThread.out.println("JOIN_FAILED_RESPONSE$0");
        }
    }

    @Override
    public void handleWhitePick() {
        // ustawiam swoj kolor na bialy. czekam dopoki każdy z graczy nie ma ustawionego koloru (ide do waiting state), w waiting state odpowiadam na nic
        if (!GoServer.isWhiteConnected()) {
            goThread.setPlayer(StoneColor.WHITE);
            GoServer.connect("white");
            goThread.out.println("JOIN_SUCCESSFUL_RESPONSE$0");
            System.out.println("white player connected");
            synchronized(GoServer.threads.get(0)){
            GoServer.threads.get(0).notify();}
        } else {
            goThread.out.println("JOIN_FAILED_RESPONSE$0");
        }
    }

    @Override
    public void handlePlace() {
        goThread.out.println("WAIT_RESPONSE$"+goThread.getPlayerString());
    }

    @Override
    public void handleSkip() {
        goThread.out.println("WAIT_RESPONSE$"+goThread.getPlayerString());
    }

    @Override
    public void handleExit() {
        goThread.out.println("DISCONNECT_RESPONSE$0");
        System.out.println("client disconnected");
    }

    @Override
    public void handleInvalid() {
        goThread.out.println("INVALID_RESPONSE$0");
        System.out.println("invalid command");
    }
}
