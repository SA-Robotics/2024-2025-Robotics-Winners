package org.firstinspires.ftc.teamcode.controller;

public class FloatingButton extends Button {

    private double value;
    private double prevValue;

    /**
     * Gets the current value of the button being held
     */
    public double getValue() {
        return value;
    }

    /**
     * Determines if the current button's magnitude is increasing
     */
    public boolean isIncreasing() {
        return value > prevValue;
    }

    /**
     * Determines if the current button's magnitude is decreasing
     */
    public boolean isDecreasing() {
        return value < prevValue;
    }

    public void handleUpdate(double value) {
        prevDuration = duration;
        prevValue = this.value;
        this.value = value;

        if(Math.abs(value) > 0)
            duration++;
        else
            duration = 0;
    }
}
