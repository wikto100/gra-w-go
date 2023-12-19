package org.grawgo.core;

public interface Rules {
    boolean isLegal(int x, int y);
    void placeStone(int[] coords, StoneColor stoneColor, StoneColor enemy);
    boolean isDead(int x,int y, StoneColor ally);
    void kill(int x,int y,StoneColor ally);
    String skip();
// TODO: reszta zasad
}
