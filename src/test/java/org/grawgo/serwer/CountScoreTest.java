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
        coords[0] = 1;
        coords[1] = 3;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 3;
        coords[1] = 1;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 2;
        coords[1] = 2;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 19;
        coords[1] = 17;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 18;
        coords[1] = 18;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 17;
        coords[1] = 19;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        int result = testBoard.countScore(StoneColor.WHITE);
        assertEquals(result,3);
        result = testBoard.countScore(StoneColor.BLACK);
        assertEquals(result,3);
        // * * W
        // * W
        // W
        //
    }

    @Test
    public void scoreTest2(){
        int[] coords = new int[2];
        coords[0] = 11;
        for(int i = 1;i<20;i++){
            coords[1] = i;
            testBoard.placeStone(coords,StoneColor.WHITE,StoneColor.BLACK);
        }
        coords[0] = 1;
        coords[1] = 1;
        testBoard.placeStone(coords,StoneColor.BLACK,StoneColor.WHITE);
        int res = testBoard.countScore(StoneColor.WHITE);
        assertEquals(res, 8*19);
    }
    
    @Test
    public void scoreTest3(){
        int[] coords = new int[2];
        coords[0] = 10;
        for(int i = 1;i<19;i++){
            coords[1] = i;
            testBoard.placeStone(coords,StoneColor.WHITE,StoneColor.BLACK);
        }
        coords[0] = 5;
        coords[1] = 5;
        testBoard.placeStone(coords,StoneColor.BLACK,StoneColor.WHITE);
        int res = testBoard.countScore(StoneColor.WHITE);
        assertEquals(res, 0);
    }

    @Test
    public void scoreTest4(){
        int[] coords = new int[2];
        coords[0] = 11;
        for(int i = 1;i<20;i++){
            coords[1] = i;
            testBoard.placeStone(coords,StoneColor.WHITE,StoneColor.BLACK);
        }
        coords[0] = 11;
        coords[1] = 19;
        testBoard.placeStone(coords,StoneColor.BLACK,StoneColor.WHITE);
        int res = testBoard.countScore(StoneColor.BLACK);
        assertEquals(res, 0);
    }
}
