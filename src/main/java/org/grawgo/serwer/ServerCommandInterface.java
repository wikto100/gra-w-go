package org.grawgo.serwer;

public interface ServerCommandInterface {
    // dobrze by bylo miec wszystkie komendy tutaj

    void handleSizeChange(int size);

    void handleLoad(int gameno);
    void handlePrev();
    void handleNext();
    void handleDead(String command);
    void handleConfirm();
    void handleBlackPick() throws InterruptedException;

    void handleWhitePick() throws InterruptedException;

    void handlePlace(String command);

    void handleSkip();

    void handleExit() throws InterruptedException;

    void handleInvalid();
}
