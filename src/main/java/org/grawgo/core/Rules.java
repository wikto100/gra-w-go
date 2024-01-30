package org.grawgo.core;


public interface Rules {
    boolean isLegal(int x, int y, StoneColor color, StoneColor enemy);
    boolean isKo(int x, int y, StoneColor color, StoneColor enemy);
    boolean placeStone(int[] coords, StoneColor color, StoneColor enemy);
    boolean isDead(int x,int y, StoneColor ally);
    void kill(int x,int y,StoneColor ally);
    String skip();
    boolean remove(int[] coords);
    int countScore(StoneColor color);
// TODO: reszta zasad
}
