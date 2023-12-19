package org.grawgo.core;

public interface Rules {
    boolean isLegal(int x, int y, StoneColor color);
    void placeStone(int[] coords, StoneColor color, StoneColor enemy);
    boolean isDead(int x,int y, StoneColor ally);
    void kill(int x,int y,StoneColor ally);
    String skip();
// TODO: reszta zasad
}
