package org.grawgo.core;

public class Stone {
    private StoneColor stoneColor;
    public int lastChecked=0;
    public StoneColor owner=StoneColor.EMPTY;

    public Stone(StoneColor stoneColor) {
        this.stoneColor = stoneColor;
    }

    public StoneColor getStoneColor() {
        return stoneColor;
    }

    public void setStoneColor(StoneColor color){
        this.stoneColor=color;
    }
}
