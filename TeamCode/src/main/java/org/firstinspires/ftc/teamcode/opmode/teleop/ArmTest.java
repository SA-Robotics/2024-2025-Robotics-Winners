package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;
@TeleOp(name = "Arm Test", group = "Test")
public class ArmTest extends OpMode {
    private DcMotor slideLMotor, slideRMotor, armLMotor, armRMotor, fL, fR, bL, bR;
    private Servo clawLServo, clawRServo;
    private ControllerHandler ch;
    private ArmController ac;
    private DriveTrain dt;

    @Override
    public void init() {
        Hardware.init(hardwareMap);
        ch = new ControllerHandler(gamepad1);

        fL = Hardware.DT_FRONT_LEFT_MOTOR.get();
        fR = Hardware.DT_FRONT_RIGHT_MOTOR.get();
        bL = Hardware.DT_BACK_LEFT_MOTOR.get();
        bR = Hardware.DT_BACK_RIGHT_MOTOR.get();

        slideRMotor = Hardware.SLIDE_R_MOTOR.get();
        slideLMotor = Hardware.SLIDE_L_MOTOR.get();

        clawLServo = Hardware.CLAW_L_SERVO.get();
        clawRServo = Hardware.CLAW_R_SERVO.get();

        slideRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armLMotor = Hardware.ARM_L_MOTOR.get();
        armRMotor = Hardware.ARM_R_MOTOR.get();

        armLMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        armLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slideRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armLMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armRMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ac = new ArmController(slideLMotor, slideRMotor, armLMotor, armRMotor, clawLServo, clawRServo);
        dt = new DriveTrain(fR, fL, bR, bL, new DcMotor[] {fR, fL});
    }

    @Override
    public void loop() {
        ch.update();
        dt.update(ch, null, false);

        if(ch.a.onPress()) {
            if(ac.isClawOpen()) {
                ac.closeClaw();
            } else {
                ac.openClaw();
            }
        }

        armLMotor.setPower(ch.lT.getValue() - ch.rT.getValue());
        armRMotor.setPower(ch.lT.getValue() - ch.rT.getValue());
    }
}
