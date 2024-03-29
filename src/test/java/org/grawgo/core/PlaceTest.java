package org.grawgo.core;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaceTest {
    private final Board testBoard = new Board(19);
    boolean success;

    @Test
    public void offBoardTest(){
        int[] coords = {0,0};
        success=testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        assertFalse(success);
    }

    @Test
    public void deadTest(){
        int[] coords = new int[2];
        coords[0] = 4;
        coords[1] = 5;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 5;
        coords[1] = 6;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 6;
        coords[1] = 5;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 5;
        coords[1] = 4;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 5;
        coords[1] = 5;
        success=testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        assertFalse(success);
    }

    @Test
    public void placeTest(){
        int[] coords = new int[2];
        for(int i=1; i<20; i++){
            for(int j=1; j<19; j++){
                coords[0] = i;
                coords[1] = j;
                success=testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
                assertTrue(success);
            }
        }
    }

    @Test
    public void koTest(){
        int[] coords = new int[2];
        coords[0] = 2;
        coords[1] = 1;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 1;
        coords[1] = 2;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 3;
        coords[1] = 2;
        testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        coords[0] = 2;
        coords[1] = 2;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 2;
        coords[1] = 4;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 1;
        coords[1] = 3;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 3;
        coords[1] = 3;
        testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        coords[0] = 2;
        coords[1] = 3;
        success=testBoard.placeStone(coords, StoneColor.WHITE,StoneColor.BLACK);
        assertTrue(success);
        coords[0] = 2;
        coords[1] = 2;
        success=testBoard.placeStone(coords, StoneColor.BLACK,StoneColor.WHITE);
        assertFalse(success);
    }
}
