package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.arm.ArmController;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

@TeleOp(name = "Competiion Teleop", group = "Competition")
public class CompetitionTeleop extends OpMode {
    private DriveTrain dt;
    private ArmController at;
    private ControllerHandler ch;

    @Override
    public void init() {
        Hardware.init(hardwareMap);

        DcMotor fR = Hardware.DT_FRONT_RIGHT_MOTOR.get();
        DcMotor fL = Hardware.DT_FRONT_LEFT_MOTOR.get();
        DcMotor bR = Hardware.DT_BACK_RIGHT_MOTOR.get();
        DcMotor bL = Hardware.DT_BACK_LEFT_MOTOR.get();
        dt = new DriveTrain(fR, fL, bR, bL, new DcMotor[] {fR, fL});

        DcMotor lSlideMotor = Hardware.SLIDE_L_MOTOR.get();
        DcMotor rSlideMotor = Hardware.SLIDE_R_MOTOR.get();
        DcMotor lArmMotor = Hardware.ARM_L_MOTOR.get();
        DcMotor rArmMotor = Hardware.ARM_R_MOTOR.get();
        Servo lClawServo = Hardware.CLAW_L_SERVO.get();
        Servo rClawServo = Hardware.CLAW_R_SERVO.get();
        at = new ArmController(lSlideMotor, rSlideMotor, lArmMotor, rArmMotor, lClawServo, rClawServo);
        at.enablePID();

        ch = new ControllerHandler(gamepad1);
    }

    private String status = "AWAITING START";
    @Override
    public void init_loop() {
        telemetry.addData("Status", status);

        switch(status) {
            case "AWAITING START":
                status = "AWAITING START.";
                break;
            case "AWAITING START.":
                status = "AWAITING START..";
                break;
            case "AWAITING START..":
                status = "AWAITING START...";
                break;
            case "AWAITING START...":
                status = "AWAITING START";
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void start() {
        telemetry.addData("Status", "RUNNING...");
        telemetry.update();
    }

    @Override
    public void loop() {
        ch.update();
        dt.update(ch, null, false);
        at.update(ch);

        //////////////////
        // CLAW CONTROL //
        //////////////////

        if(ch.a.onPress()) {
            if(at.isClawOpen())
                at.closeClaw();
            else
                at.openClaw();
        }

        /////////////////
        // ARM CONTROL //
        /////////////////

        // Arm controls
        if(at.manualArmControl) {
            at.setArmPower(-ch.rY.getValue());
        } else {
            if(ch.dpadUp.onPress()) {
                at.setArmAngle(150);
            } else if(ch.dpadDown.onPress()) {
                at.setArmPower(0);
            }
        }

        // Slide controls
        if(at.manualSlideControl) {
            at.setSlidePower(ch.rT.getValue() - ch.lT.getValue());
        } else {
            if(ch.dpadUp.onPress()) {
                at.setSetPoint(500);
            } else if(ch.dpadDown.onPress()) {
                at.setSetPoint(0);
            }
        }

        ///////////////
        // Telemetry //
        ///////////////

        telemetry.addData("Drive Train Power", dt.driveTrainPowerPercent + "%");
        telemetry.addData("Arm Mode", at.manualArmControl ? "MANUAL" : "AUTO");
        telemetry.addData("Slide Mode", at.manualSlideControl ? "MANUAL" : "AUTO");
    }
}
