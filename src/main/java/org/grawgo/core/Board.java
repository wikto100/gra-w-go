package org.grawgo.core;

public class Board implements Rules {
    private final Stone[][] stones;
    private final int size;
    private volatile boolean previouslySkipped = false;
    private int turn = 1; //isDead jednak potrzebuje nowej tury na kazde wywolanie
    private int lastMove = 1; //ostatni zaakceptowany ruch  (do Ko)
    private int realTurn = 1; //tura do db
    private int whitePoints = 0;
    private int blackPoints = 0;
    private boolean isLogged = false;

    public Board(int size) {
        this.size = size;
        stones = new Stone[size + 2][size + 2];
        for (int i = 0; i <= size + 1; i++) {
            for (int j = 0; j <= size + 1; j++) {
                if (j == 0 || j == size + 1 || i == 0 || i == size + 1) {
                    this.stones[i][j] = new Stone(StoneColor.BORDER);
                } else {
                    this.stones[i][j] = new Stone(StoneColor.EMPTY);
                }
            }
        }
    }

    @Override
    public boolean isLegal(int x, int y, StoneColor color, StoneColor enemy) {
        if (!isInBounds(x, y)) {
            return false;
        } else if (isKo(x, y, color, enemy)) {
            return false;
        }
        return this.stones[x][y].getStoneColor() == StoneColor.EMPTY;
    }

    @Override
    public boolean isKo(int x, int y, StoneColor color, StoneColor enemy){
        if (this.stones[x][y].lastChecked == 0 || this.stones[x][y].lastChecked <= this.lastMove - 4){
            return false;
        }
        StoneColor top, right, bottom, left;
        top = this.stones[x - 1][y].getStoneColor();
        bottom = this.stones[x + 1][y].getStoneColor();
        right = this.stones[x][y + 1].getStoneColor();
        left = this.stones[x][y - 1].getStoneColor();
        if (top == StoneColor.EMPTY || bottom == StoneColor.EMPTY || right == StoneColor.EMPTY || left == StoneColor.EMPTY){
            return false;
        }
        if (top == enemy){
            if ((this.stones[x - 1][y - 1].getStoneColor() == color || this.stones[x - 1][y - 1].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x - 2][y].getStoneColor() == color || this.stones[x - 2][y].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x - 1][y + 1].getStoneColor() == color || this.stones[x - 1][y + 1].getStoneColor() == StoneColor.BORDER)){
                return true;
             }
        }
        if (bottom == enemy){
            if ((this.stones[x + 1][y - 1].getStoneColor() == color || this.stones[x + 1][y - 1].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x + 2][y].getStoneColor() == color || this.stones[x + 2][y].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x + 1][y + 1].getStoneColor() == color || this.stones[x + 1][y + 1].getStoneColor() == StoneColor.BORDER)){
                return true;
             }
        }
        if (right == enemy){
            if ((this.stones[x - 1][y + 1].getStoneColor() == color || this.stones[x - 1][y + 1].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x][y + 2].getStoneColor() == color || this.stones[x][y + 2].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x + 1][y + 1].getStoneColor() == color || this.stones[x + 1][y + 1].getStoneColor() == StoneColor.BORDER)){
                return true;
             }
        }
        if (left == enemy){
            if ((this.stones[x - 1][y - 1].getStoneColor() == color || this.stones[x - 1][y - 1].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x][y - 2].getStoneColor() == color || this.stones[x][y - 2].getStoneColor() == StoneColor.BORDER)
             && (this.stones[x + 1][y - 1].getStoneColor() == color || this.stones[x + 1][y - 1].getStoneColor() == StoneColor.BORDER)){
                return true;
             }
        }
        return false;
    }

    @Override
    public boolean placeStone(int[] coords, StoneColor color, StoneColor enemy) {
        int x = coords[0];
        int y = coords[1];
        System.out.println("Turn " + this.realTurn);
        if (isLegal(x, y, color, enemy)) {
            this.stones[x][y].setStoneColor(color);
            if (this.isDead(x - 1, y, enemy)) {
                this.kill(x - 1, y, enemy);
            }
            this.turn++;
            if (this.isDead(x + 1, y, enemy)) {
                this.kill(x + 1, y, enemy);
            }
            this.turn++;
            if (this.isDead(x, y + 1, enemy)) {
                this.kill(x, y + 1, enemy);
            }
            this.turn++;
            if (this.isDead(x, y - 1, enemy)) {
                this.kill(x, y - 1, enemy);
            }
            this.turn++;
            if (this.isDead(x, y, color)) {
                this.stones[x][y].setStoneColor(StoneColor.EMPTY);
                System.out.println("illegal move");
                return false;
            } else {
                this.previouslySkipped = false;
                this.realTurn++;
                this.lastMove = this.turn;
                System.out.println("move accepted");
                return true;
            }
        } else {
            System.out.println("illegal move");
            return false;
        }
    }

    @Override
    public boolean isDead(int x, int y, StoneColor ally) {
        Stone top, right, bottom, left;
        if (this.stones[x][y].getStoneColor() != ally) {
            return false;
        }
        this.stones[x][y].lastChecked = this.turn;
        top = this.stones[x - 1][y];
        bottom = this.stones[x + 1][y];
        right = this.stones[x][y + 1];
        left = this.stones[x][y - 1];
        if (top.getStoneColor() == StoneColor.EMPTY || bottom.getStoneColor() == StoneColor.EMPTY
                || left.getStoneColor() == StoneColor.EMPTY || right.getStoneColor() == StoneColor.EMPTY) {
            return false;
        }
        if (top.getStoneColor() == ally && top.lastChecked != this.turn) {
            if (!this.isDead(x - 1, y, ally)) {
                return false;
            }
        }
        if (bottom.getStoneColor() == ally && bottom.lastChecked != this.turn) {
            if (!this.isDead(x + 1, y, ally)) {
                return false;
            }
        }
        if (right.getStoneColor() == ally && right.lastChecked != this.turn) {
            if (!this.isDead(x, y + 1, ally)) {
                return false;
            }
        }
        if (left.getStoneColor() == ally && left.lastChecked != this.turn) {
            return this.isDead(x, y - 1, ally);
        }
        return true;
    }

    @Override
    public void kill(int x, int y, StoneColor ally) {
        Stone top, right, bottom, left;
        this.stones[x][y].setStoneColor(StoneColor.EMPTY);

        if (ally == StoneColor.BLACK) {
            this.whitePoints++;
        } else {
            this.blackPoints++;
        }

        top = this.stones[x - 1][y];
        bottom = this.stones[x + 1][y];
        right = this.stones[x][y + 1];
        left = this.stones[x][y - 1];

        if (top.getStoneColor() == ally) {
            kill(x - 1, y, ally);
        }
        if (bottom.getStoneColor() == ally) {
            kill(x + 1, y, ally);
        }
        if (right.getStoneColor() == ally) {
            kill(x, y + 1, ally);
        }
        if (left.getStoneColor() == ally) {
            kill(x, y - 1, ally);
        }
    }

    @Override
    public String skip() {
        System.out.println("Turn " + this.realTurn);
        this.turn=this.turn+4;
        this.lastMove=this.lastMove+4;
        this.realTurn++;
        if (previouslySkipped) {
            return "DECIDING_RESPONSE$";
        } else {
            this.previouslySkipped = true;
            return "SKIP_RESPONSE$";
        }
    }

    @Override
    public boolean remove(int[] coords) {
        int x = coords[0];
        int y = coords[1];
        if (!isInBounds(x, y)) {
            return false;
        }
        if (this.stones[x][y].getStoneColor() == StoneColor.BLACK){
            this.kill(x, y, StoneColor.BLACK);
            return true;
        } else if (this.stones[x][y].getStoneColor() == StoneColor.WHITE){
            this.kill(x, y, StoneColor.WHITE);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public int countScore(StoneColor color) {
        for (int i = 1; i <= this.size; i++) {
            for (int j = 1; j <= this.size; j++) {
                if (this.stones[i][j].getStoneColor() == StoneColor.EMPTY && this.stones[i][j].lastChecked != this.turn
                        && this.stones[i][j].owner == StoneColor.EMPTY) {
                    this.turn++;
                    if (this.isTerritory(i, j, color)) {
                        this.paint(i, j, color);
                    }
                }
            }
        }
        if (color == StoneColor.WHITE) {
            return this.whitePoints;
        } else {
            return this.blackPoints;
        }
    }

    public boolean isTerritory(int x, int y, StoneColor color) {
        this.stones[x][y].lastChecked = this.turn;
        if (this.stones[x][y].getStoneColor() == color || this.stones[x][y].getStoneColor() == StoneColor.BORDER) {
            return true;
        }
        if (this.stones[x][y].getStoneColor() != StoneColor.EMPTY) {
            return false;
        }
        if (this.stones[x - 1][y].lastChecked != this.turn) {
            if (!this.isTerritory(x - 1, y, color)) {
                return false;
            }
        }
        if (this.stones[x + 1][y].lastChecked != this.turn) {
            if (!this.isTerritory(x + 1, y, color)) {
                return false;
            }
        }
        if (this.stones[x][y + 1].lastChecked != this.turn) {
            if (!this.isTerritory(x, y + 1, color)) {
                return false;
            }
        }
        if (this.stones[x][y - 1].lastChecked != this.turn) {
            return this.isTerritory(x, y - 1, color);
        }
        return true;
    }

    public void paint(int x, int y, StoneColor color) {
        Stone top, right, bottom, left;
        this.stones[x][y].owner = color;

        if (color == StoneColor.WHITE) {
            this.whitePoints++;
        } else {
            this.blackPoints++;
        }

        top = this.stones[x - 1][y];
        bottom = this.stones[x + 1][y];
        right = this.stones[x][y + 1];
        left = this.stones[x][y - 1];

        if (top.getStoneColor() == StoneColor.EMPTY && top.owner == StoneColor.EMPTY) {
            paint(x - 1, y, color);
        }
        if (bottom.getStoneColor() == StoneColor.EMPTY && bottom.owner == StoneColor.EMPTY) {
            paint(x + 1, y, color);
        }
        if (right.getStoneColor() == StoneColor.EMPTY && right.owner == StoneColor.EMPTY) {
            paint(x, y + 1, color);
        }
        if (left.getStoneColor() == StoneColor.EMPTY && left.owner == StoneColor.EMPTY) {
            paint(x, y - 1, color);
        }
    }

    public boolean isInBounds(int x, int y) {
        return x >= 1 && x <= this.size && y >= 1 && y <= this.size;
    }

    public String getScores() {
        countScore(StoneColor.WHITE);
        String res = String.valueOf(this.whitePoints);
        res += "|";
        countScore(StoneColor.BLACK);
        res += String.valueOf(this.blackPoints);
        res += "|";
        return res;
    }

    public String printBoard() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int lbl = 1; lbl <= this.size; lbl++) {
            if (lbl < 10) {
                stringBuilder.append(' ').append(lbl).append(' ');
            } else {
                stringBuilder.append(lbl).append(' ');
            }
        }
        stringBuilder.append('|');
        for (int x = 1; x < this.size + 1; x++) {
            for (int y = 1; y < this.size + 1; y++) {
                if (stones[x][y].getStoneColor() == StoneColor.WHITE) {
                    stringBuilder.append(" W ");
                } else if (stones[x][y].getStoneColor() == StoneColor.BLACK) {
                    stringBuilder.append(" B ");
                } else {
                    stringBuilder.append(" + ");
                }
            }
            stringBuilder.append(" ").append(x);
            stringBuilder.append('|');

        }
        return stringBuilder.toString();
    }

    public int getSize() {
        return size;
    }

    public int getBlackPoints() {
        return blackPoints;
    }

    public int getWhitePoints() {
        return whitePoints;
    }

    public boolean isLogged() {
        return this.isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    // Do testow
    public Stone getStone(int x, int y) {
        return this.stones[x][y];
    }

    public int getTurn() {
        return this.realTurn;
    }

    public void setTurn(int turn) {
        this.realTurn = turn;
        this.turn = turn;
        this.lastMove = turn;
    }

    public void setBoard(String serialized) {
        serialized = serialized.replaceAll("[\\s\\d]", "");
        String[] rows = serialized.split("\\|");
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            for (int j = 0; j < row.length(); j++) {
                char x = row.charAt(j);
                if (x == 'B') {
                    stones[i][j+1] = new Stone(StoneColor.BLACK);
                } else if (x == 'W') {
                    stones[i][j+1] = new Stone(StoneColor.WHITE);
                } else{
                    stones[i][j+1] = new Stone(StoneColor.EMPTY);
                }
            }
        }
    }
}