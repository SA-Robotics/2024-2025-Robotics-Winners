package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public enum Hardware {
    DT_FRONT_RIGHT_MOTOR(DcMotor.class, "FrontRight"),
    DT_BACK_RIGHT_MOTOR(DcMotor.class, "BackRight"),
    DT_FRONT_LEFT_MOTOR(DcMotor.class, "FrontLeft"),
    DT_BACK_LEFT_MOTOR(DcMotor.class, "BackLeft"),
    SLIDE_L_MOTOR(DcMotor.class, "SlideLeftMotor"),
    SLIDE_R_MOTOR(DcMotor.class, "SlideRightMotor"),
    ARM_L_MOTOR(DcMotor.class, "ArmLeftMotor"),
    ARM_R_MOTOR(DcMotor.class, "ArmRightMotor"),
    CLAW_L_SERVO(Servo.class, "ClawLeftServo"),
    CLAW_R_SERVO(Servo.class, "ClawRightServo"),
    CAMERA(WebcamName.class, "Webcam1");

    private String name;
    private Class clazz;
    private static HardwareMap hwMap;

    private Hardware(Class<?> clazz, String name) {
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * @return Name of hardware in configuration file
     */
    public String getConfigName() {
        return name;
    }

    /**
     * @return Type of hardware a type is
     */
    public Class getType() {
        return clazz;
    }

    /**
     * Initializes the hardware component with the configured name and type
     * @return Reference to class
     */
    public <T> T get() {
        return (T)hwMap.get(clazz, name);
    }

    public static void init(HardwareMap hwMap) {
        Hardware.hwMap = hwMap;
    }
}
