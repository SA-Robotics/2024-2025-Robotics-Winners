package org.firstinspires.ftc.teamcode.util.arm;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

public class ArmController {
    public static final double CLAW_CLOSED_POSITION = 0, CLAW_OPENED_POSITION = 0.4;
    public static final double SLIDE_MOVE_POWER = 1, ARM_ROTATE_POWER = 1;
    public static final int ARM_TICKS_PER_ROTATION = 5281;

    private ControllerHandler ch;
    private DcMotor leftSlideMotor, rightSlideMotor;
    private DcMotor leftArmMotor, rightArmMotor;
    private Servo leftClawServo, rightClawServo;
    private boolean isClawOpen;

    public ArmController(ControllerHandler ch, DcMotor leftSlideMotor, DcMotor rightSlideMotor, DcMotor leftArmMotor, DcMotor rightArmMotor, Servo leftClawServo, Servo rightClawServo) {
        this.ch = ch;
        this.leftSlideMotor = leftSlideMotor;
        this.rightSlideMotor = rightSlideMotor;
        this.leftArmMotor = leftArmMotor;
        this.rightArmMotor = rightArmMotor;
        this.leftClawServo = leftClawServo;
        this.rightClawServo = rightClawServo;

        // Linear slide motors init
        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Arm rotation motors init
        leftArmMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Claw servos init
        leftClawServo.setPosition(CLAW_OPENED_POSITION);
        rightClawServo.setPosition(CLAW_OPENED_POSITION);
        isClawOpen = true;
    }

    /**
     * Opens the claw and marks the clas as open
     */
    public void openClaw() {
        leftClawServo.setPosition(CLAW_OPENED_POSITION);
        rightClawServo.setPosition(CLAW_OPENED_POSITION);
        isClawOpen = true;
    }

    /**
     * Closes the claw and marks claw as closed
     */
    public void closeClaw() {
        leftClawServo.setPosition(CLAW_CLOSED_POSITION);
        rightClawServo.setPosition(CLAW_CLOSED_POSITION);
        isClawOpen = false;
    }

    /**
     * @return If the claw is currently open determined by the software
     */
    public boolean isClawOpen() {
        return isClawOpen;
    }

    /**
     * Set the position of the linear slides according to a manually input value
     * @param ticks new position of linear slide
     */
    public void setSlidePosition(int ticks) {
        leftSlideMotor.setTargetPosition(ticks);
        rightSlideMotor.setTargetPosition(ticks);

        leftSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftSlideMotor.setPower(SLIDE_MOVE_POWER);
        rightSlideMotor.setPower(SLIDE_MOVE_POWER);
    }

    /**
     * Set the position of the linear slides according to a certain position under ArmPosition.java
     * @param position new position of linear slide
     */
    public void setSlidePosition(ArmPosition position) {
        leftSlideMotor.setTargetPosition(position.getTicks());
        rightSlideMotor.setTargetPosition(position.getTicks());

        leftSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftSlideMotor.setPower(SLIDE_MOVE_POWER);
        rightSlideMotor.setPower(SLIDE_MOVE_POWER);
    }

    /**
     * Set the position of the arm according to a manually input value
     * @param ticks new position of arm
     */
    public void setArmRotation(int ticks) {
        leftArmMotor.setTargetPosition(ticks);
        rightArmMotor.setTargetPosition(ticks);

        leftArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftArmMotor.setPower(ARM_ROTATE_POWER);
        rightArmMotor.setPower(ARM_ROTATE_POWER);
    }

    /**
     * Set the position of the arm according to a certain position under ArmPosition.java
     * @param position new position of arm
     */
    public void setArmRotation(ArmPosition position) {
        leftArmMotor.setTargetPosition(position.getTicks());
        rightArmMotor.setTargetPosition(position.getTicks());

        leftArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftArmMotor.setPower(ARM_ROTATE_POWER);
        rightArmMotor.setPower(ARM_ROTATE_POWER);
    }

    /**
     * Set the position of the arm according to a certain position according to a new angle
     * @param degrees degrees to set the arm to
     */
    public void setArmAngle(double degrees) {
        int position = (int)(degrees * ARM_TICKS_PER_ROTATION / 360);
        leftArmMotor.setTargetPosition(position);
        rightArmMotor.setTargetPosition(position);

        leftArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftArmMotor.setPower(ARM_ROTATE_POWER);
        rightArmMotor.setPower(ARM_ROTATE_POWER);
    }
}
