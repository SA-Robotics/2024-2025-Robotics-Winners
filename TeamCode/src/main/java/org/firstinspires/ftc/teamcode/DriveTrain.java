package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.controller.ControllerHandler;

public class DriveTrain {
    public final double DRIVE_TRAIN_MOTOR_TICKS = 0;
    private DcMotor fR;
    private DcMotor fL;
    private DcMotor bR;
    private DcMotor bL;
    private int driveTrainPowerPercent = 60;

    public DriveTrain(DcMotor frontRight, DcMotor frontLeft, DcMotor backRight, DcMotor backLeft, DcMotor[] reversed) {
        fR = frontRight;
        fL = frontLeft;
        bR = backRight;
        bL = backLeft;

        for(DcMotor motor : reversed) {
            motor.setDirection(DcMotorSimple.Direction.REVERSE);
        }
    }

    /**
     * Initializes the drive train to be used for an autonomous class
     */
    public void autonomousInit() {
        setDriveTrainMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Waits until all motors are finished then continues program flow
     */
    public void awaitMotors() {
        while(fR.isBusy() || fL.isBusy() || bR.isBusy() || bL.isBusy()) {}
    }

    /**
     * Sets the mode for all drivetrain motors
     * @param mode Mode to set
     */
    public void setDriveTrainMode(DcMotor.RunMode mode) {
        fR.setMode(mode);
        fL.setMode(mode);
        bR.setMode(mode);
        bL.setMode(mode);
    }

    /**
     * Sets the zero power behavior for all drivetrain motors
     * @param behavior Behavior to set
     */
    public void setDriveTrainZeroPower(DcMotor.ZeroPowerBehavior behavior) {
        fR.setZeroPowerBehavior(behavior);
        fL.setZeroPowerBehavior(behavior);
        bR.setZeroPowerBehavior(behavior);
        bL.setZeroPowerBehavior(behavior);
    }

    /**
     * Sets the power for all drivetrain motors
     * @param power Power to set
     */
    public void setDriveTrainPower(double power) {
        fR.setPower(power);
        fL.setPower(power);
        bR.setPower(power);
        bL.setPower(power);
    }

    /**
     * Drives the robot given a distance to move
     * @param power Motor power
     * @param distance Distance to move
     */
    public void driveToPosition(double power, int distance) {
        // Set new position
        fR.setTargetPosition(fR.getCurrentPosition() + distance);
        fL.setTargetPosition(fL.getCurrentPosition() + distance);
        bR.setTargetPosition(bR.getCurrentPosition() + distance);
        bL.setTargetPosition(bL.getCurrentPosition() + distance);

        // Runs to position with designated power
        setDriveTrainMode(DcMotor.RunMode.RUN_TO_POSITION);
        setDriveTrainPower(power);

        // Wait for movement to finish
        awaitMotors();
    }

    /**
     * Drives given an amount of degrees to turn the wheels
     * @param power Motor power
     * @param degrees Degrees to rotate
     */
    public void driveForDegrees(double power, double degrees, boolean wait) {
        int pos = (int)(DRIVE_TRAIN_MOTOR_TICKS * (degrees / 360));

        setDriveTrainMode(DcMotor.RunMode.RUN_USING_ENCODER); // Set motors to use encoders

        // Set new position
        fR.setTargetPosition(fR.getTargetPosition() + pos);
        fL.setTargetPosition(fL.getTargetPosition() + pos);
        bR.setTargetPosition(bR.getTargetPosition() + pos);
        bL.setTargetPosition(bL.getTargetPosition() + pos);

        // Run to position with designated power
        setDriveTrainMode(DcMotor.RunMode.RUN_TO_POSITION);
        setDriveTrainPower(power);

        // Wait for movement to finish
        awaitMotors();
    }

    /**
     * Turns to robot to a given motor position
     * @param power Motor power
     * @param distance Distance to move motors
     */
    public void turnToPosition(double power, int distance) {
        // Set new position
        fR.setTargetPosition(fR.getCurrentPosition() - distance);
        fL.setTargetPosition(fL.getCurrentPosition() - distance);
        bR.setTargetPosition(bR.getCurrentPosition() + distance);
        bL.setTargetPosition(bL.getCurrentPosition() + distance);

        // Run to position with designated power
        setDriveTrainMode(DcMotor.RunMode.RUN_TO_POSITION);
        setDriveTrainPower(power);

        // Wait for movement to finish
        awaitMotors();
    }

    /**
     * Strafes the robot given a distance to move
     * @param power Motor power
     * @param distance Distance to move motors
     */
    public void strafeToPosition(double power, int distance) {
        // Set new position
        fR.setTargetPosition(fR.getCurrentPosition() - distance);
        fL.setTargetPosition(fL.getCurrentPosition() + distance);
        bR.setTargetPosition(bR.getCurrentPosition() + distance);
        bL.setTargetPosition(bL.getCurrentPosition() - distance);

        // Run to position with designated power
        setDriveTrainMode(DcMotor.RunMode.RUN_TO_POSITION);
        setDriveTrainPower(power);

        // Wait for movement to finish
        awaitMotors();
    }

    /**
     * Controls the robot based on the orientation of the robot on the field
     * @param ch Primary gamepad
     * @param imu Imu to read position from
     * @param isFieldCentric
     * 45deg 98m
     * 98 = vcos(45)t
     */
    public void update(ControllerHandler ch, IMU imu, boolean isFieldCentric) {
        double scale = driveTrainPowerPercent / 100d;

        // Get current from gamepad
        double lX = ch.lX.getValue() * scale;
        double lY = -ch.lY.getValue() * scale;
        double rX = ch.rX.getValue() * scale;

        // Increase drive train power on left bumper
        if(ch.leftBumper.onPress())
            driveTrainPowerPercent += 10;

        // Decrease drive train power on right bumper
        if(ch.rightBumper.onPress())
            driveTrainPowerPercent -= 10;

        double yaw = 0;

        // Set yaw to current yaw of robot
        if(isFieldCentric && imu != null)
            yaw = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Calculate power based on current angle of robot
        double calcX = lX * Math.sin(yaw) + lY * Math.cos(yaw);
        double calcY = lX * Math.cos(yaw) - lY * Math.sin(yaw);

        // Apply power
        fR.setPower(calcY - calcX + rX);
        fL.setPower(calcY + calcX + rX);
        bR.setPower(calcY + calcX - rX);
        bL.setPower(calcY - calcX - rX);
    }
}
