package com.teama.mapsubsystem.pathfinding;

import com.teama.mapsubsystem.pathfinding.TextualDirection.Direction;

import java.awt.*;
import javax.swing.*;


public class DirectionAdapter {
    private Direction d;

    public DirectionAdapter(Direction d) {
        this.d = d;
    }


    public String getDescription() {
        return d.getDescription();
    }

    public String getDistance() {
        return Double.toString(d.getLengthOfPath());
    }

    public Icon getIcon(){return d.getIcon();}
}
