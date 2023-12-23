package me.ethan.moves;

import me.ethan.models.Bug;

public class Attack implements Command{
    @Override
    public void execute(Bug bug) {
        // attack code
    }

    @Override
    public int getSize() {
        return 0;
    }
}
