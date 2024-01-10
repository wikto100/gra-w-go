package org.grawgo.core;

public interface Rules {
    boolean isLegal(int x, int y);
    boolean placeStone(int[] coords, StoneColor color, StoneColor enemy);
    boolean isDead(int x,int y, StoneColor ally);
    void kill(int x,int y,StoneColor ally);
    String skip();
    int countScore(StoneColor color);
// TODO: reszta zasad
}
