package com.example.sharingapp;

public abstract class Command {

    private boolean isExecuted;
    public Command() {
        isExecuted = false;
    }

    public abstract void execute();

    public boolean isExecuted() {
        return isExecuted;
    }

    public  void setIsExecuted(boolean isExecuted) {
        this.isExecuted = isExecuted;
    }
}
