package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.ServerCommandInterface;

public abstract class ThreadState implements ServerCommandInterface {
    protected GoThread goThread;

    public ThreadState(GoThread goThread){
        this.goThread = goThread;
    }

}
