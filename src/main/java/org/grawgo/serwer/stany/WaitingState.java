package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;

public class WaitingState extends ThreadState{
    public WaitingState(GoThread goThread) {
        super(goThread);
    }

    @Override
    public void handleBlackPick() {

    }

    @Override
    public void handleWhitePick() {

    }

    @Override
    public void handlePlace() {

    }

    @Override
    public void handleSkip() {

    }

    @Override
    public void handleExit() {
        //GoServer.disconnect();
    }

    @Override
    public void handleInvalid() {

    }
}
