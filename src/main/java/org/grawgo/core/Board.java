package org.grawgo.core;

import org.grawgo.exc.IllegalMoveException;


//TODO: to nie jest jeszcze thread-safe jako
// klasa statyczna współdzielona przez GoThready. Zaimplementujmy jakieś locki żeby było ok
public class Board implements Rules {
    private final Stone[][] stones;
    //TODO: zmieniaj currPlayer w zaleznosci od tego kto wykonal ruch (thread-safe) <-czy to musi byc thread-safe jezeli tylko jeden gracz naraz moze postawic kamien
    private StoneColor currPlayer;
    private final int size;
    private boolean previouslySkipped=false;
    public boolean whiteConnected=false;
    public boolean blackConnected=false;

    public Board(int size) {
        this.size = size;
        // czarne zaczynają
        this.currPlayer = StoneColor.BLACK;
        stones = new Stone[size][size];
    }

    @Override
    public boolean isLegal(int x, int y, StoneColor stoneColor) {
        //TODO: && (this.currPlayer.equals(stoneColor)) <-nie wiem czy nie lepiej sprawdzac ture w GoThread przy podaniu komendy
        //TODO: sprawdz oddechy kamienia
        return ((x >= 0 && x < this.size) &&
                (y >= 0 && y < this.size));
    }

    @Override
    public void placeStone(int x, int y, StoneColor stoneColor) throws IllegalMoveException {
        if (isLegal(x, y, stoneColor)) {
            this.stones[x][y] = new Stone(stoneColor);
            this.previouslySkipped = false;
        } else {
            throw new IllegalMoveException();
        }
    }

    @Override
    public String skip(){
        if (previouslySkipped==true){
            return "END_GAME_RESPONSE$";
        }
        else{
            this.previouslySkipped=true;
            return "SKIP_RESPONSE$0";
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

    public void connect(String player){
        if(player.equals("white")){
            this.whiteConnected=true;
        }
        else if(player.equals("black")){
            this.blackConnected=true;
        }
    }
}