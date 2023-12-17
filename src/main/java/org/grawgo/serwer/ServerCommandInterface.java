package org.grawgo.serwer;

public interface ServerCommandInterface {
    // dobrze by bylo miec wszystkie komendy tutaj
    void handleBlackPick();
    void handleWhitePick();
    void handlePlace();
    void handleSkip();
    void handleExit();
    void handleInvalid();
}
