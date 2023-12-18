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
}
