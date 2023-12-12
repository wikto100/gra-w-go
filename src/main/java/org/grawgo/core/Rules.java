package org.grawgo.core;

import org.grawgo.exc.IllegalMoveException;

public interface Rules {
    boolean isLegal(int x, int y, StoneColor stoneColor);
    void placeStone(int x, int y, StoneColor stoneColor) throws IllegalMoveException;
// TODO: reszta zasad
}
