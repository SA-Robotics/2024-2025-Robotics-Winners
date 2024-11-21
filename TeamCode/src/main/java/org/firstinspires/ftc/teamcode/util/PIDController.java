package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PIDController {
    public static final int MAX_ERROR_ENTRIES = 50;
    public static final int TIME_STEP = 50; // ms
    private boolean enabled;
    private List<Double> errorList = new ArrayList<>();
    private double kP, kI, kD;
    private DcMotor[] dcMotors;
    private boolean inOperation;

    public double k;
    public int setPoint;

    public PIDController(double kP, double kI, double kD, DcMotor... dcMotors) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        this.dcMotors = dcMotors;
    }

    public void update() {
        if(!enabled)
            return;

        inOperation = true;

        double avgPos = 0;
        for(DcMotor motor : dcMotors) {
            avgPos += motor.getCurrentPosition();
        }
        avgPos /= dcMotors.length;

        // Proportional
        double error = setPoint - avgPos;

        errorList.add(error);
        if(errorList.size() > MAX_ERROR_ENTRIES) {
            errorList.remove(0);
        }

        // Integral estimate through a left rienman sum
        double sum = 0;
        for(Double e : errorList) {
            sum += e * TIME_STEP;
        }
        sum /= 1000;

        // Derivative estimate by slop of prev point and current point
        double derivative = 0;
        if(errorList.size() > 2) {
            derivative = (error - errorList.get(errorList.size() - 2)) / TIME_STEP / 1000;
        }

        k = (kP * error + kI * sum + kD * derivative) / 100d;
        // Dividing by 100 helps to limit the k to a motor power

        inOperation = false;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;

        // Make sure PID is not using list before clearing
        if(!inOperation) {
            errorList.clear();
        } else {
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                errorList.clear();
            }).start();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setSetPoint(int setPoint) {
        this.setPoint = setPoint;

        if(!enabled)
            enable();
    }
}
