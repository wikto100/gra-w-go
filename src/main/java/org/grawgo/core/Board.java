package org.grawgo.core;

import org.grawgo.exc.IllegalMoveException;


//TODO: to nie jest jeszcze thread-safe jako
// klasa statyczna współdzielona przez GoThready. Zaimplementujmy jakieś locki żeby było ok
public class Board implements Rules {
    private final Stone[][] stones;
    //TODO: zmieniaj currPlayer w zaleznosci od tego kto wykonal ruch (thread-safe)
    private StoneColor currPlayer;
    private final int size;

    public Board(int size) {
        this.size = size;
        // czarne zaczynają
        this.currPlayer = StoneColor.BLACK;
        stones = new Stone[size][size];
    }

    @Override
    public boolean isLegal(int x, int y, StoneColor stoneColor) {
        //TODO: && (this.currPlayer.equals(stoneColor)) <-nie wiem czy nie lepiej sprawdzac ture w GoThread przy podaniu komendy
        return ((x >= 0 && x < this.size) &&
                (y >= 0 && y < this.size));
    }

    @Override
    public void placeStone(int x, int y, StoneColor stoneColor) throws IllegalMoveException {
        if (isLegal(x, y, stoneColor)) {
            this.stones[x][y] = new Stone(stoneColor);
        } else {
            throw new IllegalMoveException();
        }
    }

    public String printBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (stones[x][y] != null) {
                    if (stones[x][y].getStoneColor() == StoneColor.WHITE) {
                        stringBuilder.append('W');
                    } else {
                        stringBuilder.append('B');
                    }
                }else{
                    stringBuilder.append('+');
                }
            }
            stringBuilder.append('|');

        }
        return stringBuilder.toString();
    }
}