package me.ethan.moves;

import me.ethan.models.Bug;

public class Forward implements Command{
    @Override
    public void execute(Bug bug) {
        // move forward code
    }

    @Override
    public int getSize() {
        return 0;
    }
}
