package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.PIDController;
import org.firstinspires.ftc.teamcode.util.arm.ArmController;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

@TeleOp(name = "Competition Teleop", group = "Competition")
public class CompetitionTeleop extends OpMode {
    private static final double EPSILON = 0.01;
    private boolean precisionArmMode;
    private DriveTrain dt;
    private ArmController ac;
    private ControllerHandler ch;
    private DcMotor lSlideMotor, rSlideMotor, lArmMotor, rArmMotor;
    private PIDController slidePID;

    @Override
    public void init() {
        Hardware.init(hardwareMap);

        DcMotor fR = Hardware.DT_FRONT_RIGHT_MOTOR.get();
        DcMotor fL = Hardware.DT_FRONT_LEFT_MOTOR.get();
        DcMotor bR = Hardware.DT_BACK_RIGHT_MOTOR.get();
        DcMotor bL = Hardware.DT_BACK_LEFT_MOTOR.get();
        dt = new DriveTrain(fR, fL, bR, bL, new DcMotor[] {fR, fL});

        lSlideMotor = Hardware.SLIDE_L_MOTOR.get();
        rSlideMotor = Hardware.SLIDE_R_MOTOR.get();
        lArmMotor = Hardware.ARM_L_MOTOR.get();
        rArmMotor = Hardware.ARM_R_MOTOR.get();
        Servo lClawServo = Hardware.CLAW_L_SERVO.get();
        Servo rClawServo = Hardware.CLAW_R_SERVO.get();
        ac = new ArmController(lSlideMotor, rSlideMotor, lArmMotor, rArmMotor, lClawServo, rClawServo);

        ch = new ControllerHandler(gamepad1);

        lArmMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        slidePID = new PIDController(0.7, 0.02, 1.3, lSlideMotor, rSlideMotor);

        new Thread(() -> {
            long prevTime = 0;
            while(true) {
                // Sleep for 3 ms if time since last update is < the PID's time step
                if(System.currentTimeMillis() - prevTime < PIDController.TIME_STEP - 2) {
                    try {
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    prevTime = System.currentTimeMillis();

                    slidePID.update();
                }
            }
        }, "PID Thread").start();
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

        /////////////////
        // PID UPDATES //
        /////////////////

        if(slidePID.isEnabled()) {
            ac.setSlidePower(slidePID.k);
        }

        //////////////////
        // CLAW CONTROL //
        //////////////////

        if(ch.a.onPress()) {
            if(ac.isClawOpen())
                ac.closeClaw();
            else
                ac.openClaw();
        }

        /////////////////
        // ARM CONTROL //
        /////////////////

        if(ch.y.onPress()) {
            precisionArmMode = !precisionArmMode;
        }

        // Arm controls
        ac.setArmPower((ch.lT.getValue() - ch.rT.getValue()) * (precisionArmMode ? 0.2 : 1));

        // Slide controls
        if(Math.abs(ch.rY.getValue()) >= EPSILON) {
            slidePID.disable();
            ac.setSlidePower(-ch.rY.getValue());
        } else if(ch.dpadUp.onPress()) {
            slidePID.setSetPoint(2450);
        } else if(ch.dpadDown.onPress()) {
            slidePID.setSetPoint(0);
        } else if(!slidePID.isEnabled()) {
            ac.setSlidePower(0);
        }

        ///////////////
        // Telemetry //
        ///////////////

        telemetry.addData("Precision Mode (Y)", precisionArmMode);
        telemetry.addData("Drive Train Power (RB | LB)", dt.driveTrainPowerPercent + "%");
        telemetry.addData("LX", ch.lX.getValue());
        telemetry.addData("LY", ch.lY.getValue());
        telemetry.addData("RX", ch.rX.getValue());
    }
}