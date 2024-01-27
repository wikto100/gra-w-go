package org.grawgo.serwer.stany;

import org.grawgo.serwer.GoServer;
import org.grawgo.serwer.GoThread;
import org.grawgo.serwer.db.GameLoader;

public class SizePickingState extends ThreadState {

    public SizePickingState(GoThread myPlayer, GoThread otherPlayer) {
        super(myPlayer, otherPlayer);
    }

    @Override
    public void handleSizeChange(int size){
        if (size == 9 || size == 19 || size == 13) {
            //z gameloadera z bazy, wypisz wszystkie gry z tabeli games
            // Game     Scores
            // 1        b0 w0
            // 2        b41 w74
            myPlayer.out.println("SIZE_RESPONSE$" + GameLoader.listGames());
            // gameloader set/reset w zaleznosci czy juz jest w bazie danych
            GoServer.resetBoard(size);
            myPlayer.changeState(new GameLoadingState(myPlayer, otherPlayer));
        } else {
            handleInvalid();
        }

    }

    @Override
    public void handleLoad(int gameno) {
        handleInvalid();
    }

    @Override
    public void handlePrev() {
        handleInvalid();
    }

    @Override
    public void handleNext() {
        handleInvalid();
    }

    @Override
    public void handleConfirm() {
        handleInvalid();
    }

    @Override
    public void handleBlackPick(){
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

    @Override
    public void handleExit() throws InterruptedException {
        super.handleExit();
    }

    @Override
    public void handleInvalid() {
        super.handleInvalid();
    }
}
