package org.grawgo.serwer;
import org.grawgo.core.Board;
import org.grawgo.core.StoneColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountScoreTest {
    private final Board testBoard = new Board(19);
    @Test
    public void scoreTest1(){
        int[] coords = new int[2];
        coords[0] = 0;
        coords[1] = 2;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 2;
        coords[1] = 0;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 1;
        coords[1] = 1;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        int result = testBoard.countScore(StoneColor.WHITE);
        assertEquals(result,3);
        // * * W
        // * W
        // W
        //
    }
    @Test
    public void scoreTest2(){
        int[] coords = new int[2];
        coords[0] = 10;
        for(int i = 0;i<19;i++){
            coords[1] = i;
            testBoard.placeStone(coords,StoneColor.WHITE,StoneColor.BLACK);
        }
        //testBoard.placeStone(coords,StoneColor.BLACK,StoneColor.WHITE);
        int res = testBoard.countScore(StoneColor.WHITE);
        assertEquals(res, 10*19);
    }

    @Test
    public void scoreTest3(){
        int[] coords = new int[2];
        coords[0] = 10;
        for(int i = 0;i<19;i++){
            coords[1] = i;
            testBoard.placeStone(coords,StoneColor.WHITE,StoneColor.BLACK);
        }
        coords[0] = 5;
        coords[1] = 5;
        testBoard.placeStone(coords,StoneColor.BLACK,StoneColor.WHITE);
        int res = testBoard.countScore(StoneColor.WHITE);
        assertEquals(res, 0);
    }    @Test
    public void scoreTest4(){
        int[] coords = new int[2];
        coords[0] = 10;
        for(int i = 0;i<19;i++){
            coords[1] = i;
            testBoard.placeStone(coords,StoneColor.WHITE,StoneColor.BLACK);
        }
        coords[0] = 10;
        coords[1] = 18;
        testBoard.placeStone(coords,StoneColor.BLACK,StoneColor.WHITE);
        int res = testBoard.countScore(StoneColor.BLACK);
        assertEquals(res, 0);
    }
}
