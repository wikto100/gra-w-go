package org.grawgo.core;

public interface Rules {
    boolean isLegal(int x, int y);
    void placeStone(int[] coords, StoneColor stoneColor);
    String skip();
// TODO: reszta zasad
}
