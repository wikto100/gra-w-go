package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.ServerCommandInterface;
import org.grawgo.serwer.ServerCommandParser;

public abstract class ThreadState implements ServerCommandInterface {
    protected final GoThread myPlayer;
    protected volatile GoThread otherPlayer;
    ServerCommandParser serverParser;

    public ThreadState(GoThread myPlayer, GoThread otherPlayer) {
        this.myPlayer = myPlayer;
        this.otherPlayer = otherPlayer;
        this.serverParser = ServerCommandParser.getInstance();
    }
    // generyczne metody
    public void handleInvalid() {
        myPlayer.out.println("INVALID_RESPONSE$0");
        System.out.println("invalid command");
    }
    public void handleExit() throws InterruptedException {
        myPlayer.out.println("DISCONNECT_RESPONSE$0");
        if (otherPlayer != null)
            otherPlayer.out.println("DISCONNECT_RESPONSE$0");
        System.out.println("disconnecting...");
        Thread.sleep(1000);
        myPlayer.setRunning(false);
        if (otherPlayer != null)
            otherPlayer.setRunning(false);
    }
}
