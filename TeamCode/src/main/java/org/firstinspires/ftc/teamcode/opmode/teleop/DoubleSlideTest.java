package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.arm.ArmController;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

@TeleOp(name = "Double Slide Test", group = "Test")
public class DoubleSlideTest extends OpMode {
    private ArmController ac;
    private ControllerHandler ch;
    private DcMotor leftSlideMotor, rightSlideMotor, leftArmMotor, rightArmMotor;
    private Servo leftClawServo, rightClawServo;

    @Override
    public void init() {
        Hardware.init(hardwareMap);
        ch = new ControllerHandler(gamepad1);

        leftSlideMotor = Hardware.SLIDE_L_MOTOR.get();
        rightSlideMotor = Hardware.SLIDE_R_MOTOR.get();
        leftArmMotor = Hardware.ARM_L_MOTOR.get();
        rightArmMotor = Hardware.ARM_R_MOTOR.get();
        leftClawServo = Hardware.CLAW_L_SERVO.get();
        rightClawServo = Hardware.CLAW_R_SERVO.get();
        //ac = new ArmController(ch, leftSlideMotor, rightSlideMotor, leftArmMotor, rightArmMotor, leftClawServo, rightClawServo);
    }

    @Override
    public void loop() {
        ch.update();

//        if(ch.a.onPress()) {
//            if(ac.isClawOpen())
//                ac.closeClaw();
//            else
//                ac.openClaw();
//        }
//
//        leftArmMotor.setPower(ch.lY.getValue());
//        rightArmMotor.setPower(ch.lY.getValue());

//        if(ch.dpadUp.onPress()) {
//            ac.setSetPoint(550);
//        } else if(ch.dpadDown.onPress()) {
//            ac.setSetPoint(0);
//        }

//        telemetry.addData("Slide L Pos", leftSlideMotor.getCurrentPosition());
//        telemetry.addData("Slide R Pos", rightSlideMotor.getCurrentPosition());
//        telemetry.addData("Arm L Pos", leftArmMotor.getCurrentPosition());
//        telemetry.addData("Arm R Pos", rightArmMotor.getCurrentPosition());
    }
}
