package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

@TeleOp(name = "Basic Teleop", group = "Test")
public class BasicTeleop extends OpMode {

    private DcMotor fR, fL, bR, bL;

    @Override
    public void init() {
//        Hardware.init(hardwareMap);
//
//        fR = Hardware.DT_FRONT_RIGHT_MOTOR.get();
//        fL = Hardware.DT_FRONT_LEFT_MOTOR.get();
//        bR = Hardware.DT_BACK_RIGHT_MOTOR.get();
//        bL = Hardware.DT_BACK_LEFT_MOTOR.get();

        fR = hardwareMap.get(DcMotor.class, "FrontRight");
        fL = hardwareMap.get(DcMotor.class, "FrontLeft");
        bR = hardwareMap.get(DcMotor.class, "BackRight");
        bL = hardwareMap.get(DcMotor.class, "BackLeft");

        bR.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        fR.setPower(gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x);
        fL.setPower(gamepad1.left_stick_x + gamepad1.left_stick_y + gamepad1.right_stick_x);
        bR.setPower(gamepad1.left_stick_x + gamepad1.left_stick_y - gamepad1.right_stick_x);
        bL.setPower(gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x);
    }
}
