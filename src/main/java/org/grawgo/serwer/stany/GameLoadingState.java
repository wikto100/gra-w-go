package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.db.GameLoader;
import org.grawgo.serwer.db.GameLogger;

public class GameLoadingState extends ThreadState {

    public GameLoadingState(GoThread myPlayer, GoThread otherPlayer) {
        super(myPlayer, otherPlayer);
    }

    @Override
    public void handleSizeChange(int size) {
        handleInvalid();
    }

    @Override
    public void handleLoad(int gameno) {
       // metoda nnic nie robi jak gameno nie ma w bazie, jak jest to ustawia ją jako board na serwerze
        // i printuje uzytkownikowi załadowany board
        if(GameLogger.isFoundInDB(gameno)){
            // zdeserializuj GameLoaderem na serwer
            GameLoader.loadBoardDBSV(gameno);
        }
        GameLogger.setCurrGameID(gameno);
        // to mozna zamienic na print planszy ale na razie piwo
        myPlayer.out.println("LOAD_RESPONSE$"+gameno);

    }

    @Override
    public void handlePrev() {
        GameLoader.loadPrev();
        myPlayer.out.println(serverParser.parseBoard("PREV_RESPONSE$"));

    }

    @Override
    public void handleNext() {
        GameLoader.loadNext();
        myPlayer.out.println(serverParser.parseBoard("NEXT_RESPONSE$"));

    }

    @Override
    public void handleDead(String command) {
        handleInvalid();
    }

    @Override
    public void handleConfirm() {
        myPlayer.out.println("CONFIRM_RESPONSE$");
        myPlayer.changeState(new ColorPickingState(myPlayer,otherPlayer));
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
        handleInvalid();
    }

    @Override
    public void handleSkip() {
        myPlayer.out.println("SKIP_OPTION_RESPONSE$");
        myPlayer.changeState(new ColorPickingState(myPlayer,otherPlayer));
    }

}
