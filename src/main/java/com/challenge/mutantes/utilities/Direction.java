package com.challenge.mutantes.utilities;

import java.awt.*;

public enum Direction {

    VERTICAL(new Point(1,0)),
    HORIZONTAL(new Point(0,1)),
    DIAGONAL_RIGHT(new Point(1,1)),
    DIAGONAL_LEFT(new Point(1,-1));


    public final Point label;

    private Direction(Point label) {
        this.label = label;
    }
}

