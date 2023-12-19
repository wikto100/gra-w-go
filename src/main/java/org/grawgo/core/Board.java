package org.grawgo.core;


//TODO: to nie jest jeszcze thread-safe jako
// klasa statyczna współdzielona przez GoThready. Zaimplementujmy jakieś locki żeby było ok
public class Board implements Rules {
    private final Stone[][] stones;
    private final int size;
    private boolean previouslySkipped=false; // eeee
    private int turn=1;
    private int whitePoints=0;
    private int blackPoints=0;

    public Board(int size) {
        this.size = size;
        stones = new Stone[size][size];
    }

    @Override
    public boolean isLegal(int x, int y, StoneColor color) {
        //TODO: ko (punkt 5 zasad)
        if(isInBounds(x,y)==false){
            return false;
        }
        else if(this.stones[x][y]!=null){
            return false;
        }
        return true;
    }

    @Override
    public void placeStone(int[] coords, StoneColor color, StoneColor enemy){
        int x = coords[0];
        int y = coords[1];
        System.out.println("Turn "+this.turn);
        if (isLegal(x, y, color)) {
            this.stones[x][y] = new Stone(color);
            //Nie jestem pewien tego miejsca wywolywania ale inaczej trzeba by gdzies w state zapisac board
            if (x!=0){
                if (this.isDead(x-1,y,enemy)==true){
                    this.kill(x-1,y,enemy);
                }
            }
            if (x!=this.size-1){
                if (this.isDead(x+1,y,enemy)==true){
                    this.kill(x+1,y,enemy);
                }
            }
            if (y!=this.size-1){
                if (this.isDead(x,y+1,enemy)==true){
                    this.kill(x,y+1,enemy);
                }
            }
            if (y!=0){
                if (this.isDead(x,y-1,enemy)==true){
                    this.kill(x,y-1,enemy);
                }
            }
            if (this.isDead(x,y,color)==true){
                this.stones[x][y]=null;
                System.out.println("illegal move");
                //TODO: zwroc komunikat o nieprawidlowym ruchu
            }
            else{
                this.previouslySkipped = false;
                this.turn++;
                System.out.println("move accepted");
            }
        } else {
            System.out.println("illegal move");
            //TODO: zwroc komunikat o nieprawidlowym ruchu
        }
    }

    @Override
    public boolean isDead(int x,int y,StoneColor ally){
        Stone top,right,bottom,left;
        //Bardzo mi sie nie podoba jak tu sprawdzam granice
        if(this.stones[x][y]==null || this.stones[x][y].getStoneColor()!=ally){
            return false;
        }
        this.stones[x][y].lastChecked=this.turn;
        if(x!=0){top=this.stones[x-1][y];}
        else{
            top=new Stone(ally);
            top.lastChecked=this.turn;
        }
        if(x!=this.size-1){bottom=this.stones[x+1][y];}
        else{
            bottom=new Stone(ally);
            bottom.lastChecked=this.turn;
        }
        if(y!=this.size-1){right=this.stones[x][y+1];}
        else{
            right=new Stone(ally);
            right.lastChecked=this.turn;
        }
        if(y!=0){left=this.stones[x][y-1];}
        else{
            left=new Stone(ally);
            left.lastChecked=this.turn;
        }
        if(top==null || bottom==null || left==null || right==null){
            return false;
        }
        if(top.getStoneColor()==ally && top.lastChecked!=this.turn){
            if(this.isDead(x-1,y,ally)==false){
                return false;
            }
        }
        if(bottom.getStoneColor()==ally && bottom.lastChecked!=this.turn){
            if(this.isDead(x+1,y,ally)==false){
                return false;
            }
            }
        if(right.getStoneColor()==ally && right.lastChecked!=this.turn){
            if(this.isDead(x,y+1,ally)==false){
                return false;
            }
        }
        if(left.getStoneColor()==ally && left.lastChecked!=this.turn){
            if(this.isDead(x,y-1,ally)==false){
                return false;
            }
        }
        return true;
    }

    @Override
    public void kill(int x,int y, StoneColor ally){
        Stone top,right,bottom,left;
        this.stones[x][y]=null;

        if(ally==StoneColor.BLACK){this.whitePoints++;}
        else{this.blackPoints++;}

        if(x!=0){top=this.stones[x-1][y];}
        else{top=null;}
        if(x!=this.size-1){bottom=this.stones[x+1][y];}
        else{bottom=null;}
        if(y!=this.size-1){right=this.stones[x][y+1];}
        else{right=null;}
        if(y!=0){left=this.stones[x][y-1];}
        else{left=null;}
        
        if(top!=null && top.getStoneColor()==ally){
            kill(x-1,y,ally);
        }
        if(bottom!=null && bottom.getStoneColor()==ally){
            kill(x+1,y,ally);
        }
        if(right!=null && right.getStoneColor()==ally){
            kill(x,y+1,ally);
        }
        if(left!=null && left.getStoneColor()==ally){
            kill(x,y-1,ally);
        }
    }

    @Override
    public String skip(){
        this.turn++;
        if (previouslySkipped){
            return "END_GAME_RESPONSE$";
        }
        else{
            this.previouslySkipped=true;
            return "SKIP_RESPONSE$0";
        }
    }

    public boolean isInBounds(int x,int y){
        if(x>=0 && x<=this.size-1 && y>=0 && y<=this.size-1){
            return true;
        }
        return false;
    }

    public String getScores(){
        String res=String.valueOf(this.whitePoints);
        res+="|";
        res+=String.valueOf(this.blackPoints);
        return res;
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
                        //stringBuilder.append(" ⬤ "); Nie obsluguje mi terminal tych znakow
                        stringBuilder.append(" W ");
                    } else {
                        //stringBuilder.append(" ◯ ");
                        stringBuilder.append(" B ");
                    }
                }else{
                    //stringBuilder.append(" ┼ ");
                    stringBuilder.append(" + ");
                }
            }
            stringBuilder.append(" ").append(x + 1);
            stringBuilder.append('|');


        }
        return stringBuilder.toString();
    }

}