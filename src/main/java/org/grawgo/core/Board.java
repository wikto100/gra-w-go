package org.grawgo.core;


//TODO: to nie jest jeszcze thread-safe jako
// klasa statyczna współdzielona przez GoThready. Zaimplementujmy jakieś locki żeby było ok
public class Board implements Rules {
    private final Stone[][] stones;
    //TODO: zmieniaj currPlayer w zaleznosci od tego kto wykonal ruch (thread-safe) <-czy to musi byc thread-safe jezeli tylko jeden gracz naraz moze postawic kamien
    private final int size;
    private boolean previouslySkipped=false; // eeee

    public Board(int size) {
        this.size = size;
        stones = new Stone[size][size];
    }

    @Override
    public boolean isLegal(int x, int y) {
        //TODO: sprawdz oddechy kamienia
        return ((x >= 0 && x < this.size) &&
                (y >= 0 && y < this.size));
    }

    @Override
    public void placeStone(int[] coords, StoneColor stoneColor){
        int x = coords[0];
        int y = coords[1];
        if (isLegal(x, y)) {
            this.stones[x][y] = new Stone(stoneColor);
            this.previouslySkipped = false;
        } else {
            //TODO: zwroc komunikat o nieprawidlowym ruchu
        }
    }

    @Override
    public String skip(){
        if (previouslySkipped){
            return "END_GAME_RESPONSE$";
        }
        else{
            this.previouslySkipped=true;
            return "SKIP_RESPONSE$0";
        }
    }

    public String printBoard() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int lbl = 1; lbl <= this.size; lbl++ ){
            if(lbl < 10){
                stringBuilder.append(' ').append(lbl).append(' ');
            } else{
                stringBuilder.append(lbl).append(' ');
            }
        }
        stringBuilder.append('|');
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (stones[x][y] != null) {
                    if (stones[x][y].getStoneColor() == StoneColor.WHITE) {
                        stringBuilder.append(" ⬤ ");
                    } else {
                        stringBuilder.append(" ◯ ");
                    }
                }else{
                    stringBuilder.append(" ┼ ");
                }
            }
            stringBuilder.append(" ").append(x + 1);
            stringBuilder.append('|');


        }
        return stringBuilder.toString();
    }


}