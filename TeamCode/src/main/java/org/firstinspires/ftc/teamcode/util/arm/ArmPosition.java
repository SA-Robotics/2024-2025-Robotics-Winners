package org.firstinspires.ftc.teamcode.util.arm;

public enum ArmPosition {
    ARM_DEFAULT(0),
    ARM_UP(1980),
    SLIDE_DEFAULT(0),
    SLIDE_UP(4000);

    private int ticks;
    private ArmPosition(int ticks) {
        this.ticks = ticks;
    }

    public int getTicks() {
        return ticks;
    }
}
