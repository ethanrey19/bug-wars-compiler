package me.ethan.moves;

import me.ethan.models.Bug;

public interface Command {
    public void execute(Bug bug);
    public int getSize();
}
