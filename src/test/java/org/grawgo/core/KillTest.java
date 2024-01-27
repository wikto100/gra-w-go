package org.grawgo.core;
import org.grawgo.core.Board;
import org.grawgo.core.Stone;
import org.grawgo.core.StoneColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KillTest {
    private final Board testBoard = new Board(19);
    @Test
    public void killTest1(){
        int[] coords = new int[2];
        coords[0] = 1;
        coords[1] = 1;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 2;
        coords[1] = 1;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 1;
        coords[1] = 2;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        StoneColor result = testBoard.getStone(1,1).getStoneColor();
        assertEquals(result,StoneColor.EMPTY);
    }

    @Test
    public void killTest2(){
        int[] coords = new int[2];
        coords[0] = 8;
        coords[1] = 8;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 9;
        coords[1] = 8;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 7;
        coords[1] = 8;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 8;
        coords[1] = 7;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 8;
        coords[1] = 9;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        StoneColor result = testBoard.getStone(8,8).getStoneColor();
        assertEquals(result,StoneColor.EMPTY);
    }

    @Test
    public void killTest3(){
        int[] coords1 = new int[2];
        int[] coords2 = new int[2];
        coords1[1] = 1;
        coords2[1] = 2;
        for (int i=1; i<20; i++){
            coords1[0] = i;
            coords2[0] = i;
            testBoard.placeStone(coords1, StoneColor.WHITE,StoneColor.BLACK);
            testBoard.placeStone(coords2, StoneColor.BLACK,StoneColor.WHITE);
        }
        for (int i=1; i<20; i++){
            StoneColor result = testBoard.getStone(i,1).getStoneColor();
            assertEquals(result,StoneColor.EMPTY);
        }
    }
    //TODO: wiekszy ksztalt
}