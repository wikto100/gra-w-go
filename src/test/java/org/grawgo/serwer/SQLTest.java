package org.grawgo.serwer;

import org.grawgo.serwer.db.GameLoader;
import org.grawgo.serwer.db.GameLogger;
import org.junit.Test;

public class SQLTest {
    @Test
    public void testLoader() {
        GoServer.resetBoard(19);
        GameLoader.loadBoardDBSV(0);
        GameLoader.listGames();
        GameLoader.loadNext();
        GameLoader.loadPrev();
        GameLoader.close();
    }
    @Test
    public void testLogger(){
        GameLogger.isFoundInDB(0);
        GameLogger.setCurrGameID(0);
        GameLogger.logScores(21, 37);
        GameLogger.close();

        // reszte nie zeby nie zasmiecac bazy danych

    }

}
