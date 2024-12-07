package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

import java.util.ArrayList;
import java.util.List;

public class ArmController {
    public static final double CLAW_CLOSED_POSITION = 0.58, CLAW_OPENED_POSITION = 0.43;
    public static final double SLIDE_MOVE_POWER = 1, ARM_ROTATE_POWER = 1;
    public static final int ARM_TICKS_PER_ROTATION = 5281;

    private DcMotor leftSlideMotor, rightSlideMotor;
    private DcMotor leftArmMotor, rightArmMotor;
    private Servo leftClawServo, rightClawServo;
    private boolean isClawOpen;

    public ArmController(@NonNull DcMotor leftSlideMotor, @NonNull DcMotor rightSlideMotor, @NonNull DcMotor leftArmMotor, @NonNull DcMotor rightArmMotor, @NonNull Servo leftClawServo, @NonNull Servo rightClawServo) {
        this.leftSlideMotor = leftSlideMotor;
        this.rightSlideMotor = rightSlideMotor;
        this.leftArmMotor = leftArmMotor;
        this.rightArmMotor = rightArmMotor;
        this.leftClawServo = leftClawServo;
        this.rightClawServo = rightClawServo;

        // Linear slide motors init
        this.leftSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Arm rotation motors init
        this.leftArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightArmMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Claw servos init
        this.leftClawServo.setPosition(CLAW_OPENED_POSITION);
        this.rightClawServo.setPosition(CLAW_CLOSED_POSITION);
        isClawOpen = true;
    }

    public void autonomousInit() {
        leftArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        closeClaw();
    }

    /**
     * Opens the claw and marks the clas as open
     */
    public void openClaw() {
        leftClawServo.setPosition(CLAW_OPENED_POSITION);
        rightClawServo.setPosition(CLAW_CLOSED_POSITION);
        isClawOpen = true;
    }

    /**
     * Closes the claw and marks claw as closed
     */
    public void closeClaw() {
        leftClawServo.setPosition(CLAW_CLOSED_POSITION);
        rightClawServo.setPosition(CLAW_OPENED_POSITION);
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
    public void setSlidePosition(SlidePosition position) {
        leftSlideMotor.setTargetPosition(position.getTicks());
        rightSlideMotor.setTargetPosition(position.getTicks());

        leftSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftSlideMotor.setPower(SLIDE_MOVE_POWER);
        rightSlideMotor.setPower(SLIDE_MOVE_POWER);
    }

    /**
     * Sets the arm's power according to a desired power
     * @param power Arm power
     */
    public void setSlidePower(double power) {
        leftSlideMotor.setPower(power);
        rightSlideMotor.setPower(power);
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

    /**
     * Sets the arm's power according to a desired power
     * @param power Arm power
     */
    public void setArmPower(double power) {
        leftArmMotor.setPower(power);
        rightArmMotor.setPower(power);
    }

    public enum SlidePosition {
        DOWN(0),
        UP(500);

        private int ticks;
        private SlidePosition(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }
    }

    public enum ArmPosition {
        DOWN(0),
        UP(1980);

        private int ticks;
        private ArmPosition(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }
    }
}