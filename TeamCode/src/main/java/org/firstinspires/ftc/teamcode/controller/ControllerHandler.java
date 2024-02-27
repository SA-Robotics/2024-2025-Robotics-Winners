package org.firstinspires.ftc.teamcode.controller;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ControllerHandler {
    public Gamepad gp;
    public Button x, y, a, b, leftBumper, rightBumper, dpadUp, dpadDown, dpadRight, dpadLeft;
    public FloatingButton lX, lY, rX, rY, lT, rT;

    public ControllerHandler(Gamepad gp) {
        this.gp = gp;

        x = new Button();
        y = new Button();
        a = new Button();
        b = new Button();
        leftBumper = new Button();
        rightBumper = new Button();
        dpadUp = new Button();
        dpadDown = new Button();
        dpadRight = new Button();
        dpadLeft = new Button();

        lX = new FloatingButton();
        lY = new FloatingButton();
        rX = new FloatingButton();
        rY = new FloatingButton();
        lT = new FloatingButton();
        rT = new FloatingButton();
    }

    public void update() {
        x.handleUpdate(gp.x);
        y.handleUpdate(gp.y);
        a.handleUpdate(gp.a);
        b.handleUpdate(gp.b);
        leftBumper.handleUpdate(gp.left_bumper);
        rightBumper.handleUpdate(gp.right_bumper);
        dpadUp.handleUpdate(gp.dpad_up);
        dpadDown.handleUpdate(gp.dpad_down);
        dpadLeft.handleUpdate(gp.dpad_left);
        dpadRight.handleUpdate(gp.dpad_right);

        lX.handleUpdate(gp.left_stick_x);
        lY.handleUpdate(gp.left_stick_y);
        rX.handleUpdate(gp.right_stick_x);
        rY.handleUpdate(gp.right_stick_y);
        lT.handleUpdate(gp.left_trigger);
        rT.handleUpdate(gp.right_trigger);
    }
}
