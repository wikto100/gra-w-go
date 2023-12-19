package org.grawgo.core;

public class Stone {
    private final StoneColor stoneColor;
    public int lastChecked=0;

    public Stone(StoneColor stoneColor) {
        this.stoneColor = stoneColor;
    }

    public StoneColor getStoneColor() {
        return stoneColor;
    }
}
