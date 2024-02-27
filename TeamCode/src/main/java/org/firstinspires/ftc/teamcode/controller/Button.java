package org.firstinspires.ftc.teamcode.controller;

public class Button {
    protected int duration;
    protected int prevDuration;

    /**
     *  Checks if the button was just pressed
     */
    public boolean onPress() {
        return duration == 1;
    }

    /**
     *  Checks if the button was just released
     */
    public boolean onRelease() {
        return prevDuration != duration && duration == 0;
    }

    /**
     *  Checks if the button is currently held
     */
    public boolean isHeld() {
        return duration > 0;
    }

    public void handleUpdate(boolean button) {
        prevDuration = duration;

        if(button)
            duration++;
        else
            duration = 0;
    }
}
