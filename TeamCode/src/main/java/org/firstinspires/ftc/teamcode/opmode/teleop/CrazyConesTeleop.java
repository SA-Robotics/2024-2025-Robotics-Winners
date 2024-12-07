package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.PIDController;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

@TeleOp(name = "Crazy Cones Teleop", group = "Competition")
public class CrazyConesTeleop extends OpMode {

    private final double EPSILON = 0.01;
    private DriveTrain dt;
    private ArmController ac;
    private PIDController slidePID;
    private ControllerHandler ch;
    private DcMotor lSlideMotor, rSlideMotor, lArmMotor, rArmMotor;

    @Override
    public void init() {
        Hardware.init(hardwareMap);

        ch = new ControllerHandler(gamepad1);

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

    private boolean precisionArmMode;

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
            slidePID.setSetPoint(1000);
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
    }
}
