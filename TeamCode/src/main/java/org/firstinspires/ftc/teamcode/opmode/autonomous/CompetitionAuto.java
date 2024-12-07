package org.firstinspires.ftc.teamcode.opmode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.DriveTrain;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.ArmController;
import org.firstinspires.ftc.teamcode.util.autonomousconfig.Alliance;
import org.firstinspires.ftc.teamcode.util.autonomousconfig.StartPosition;
import org.firstinspires.ftc.teamcode.util.controller.ControllerHandler;

@Autonomous(name = "Competition Autonomous", group = "Competition")
public class CompetitionAuto extends LinearOpMode {
    private ControllerHandler ch;
    private DriveTrain dt;
    private ArmController ac;
    private Alliance alliance = Alliance.RED;
    private StartPosition startPosition = StartPosition.BASKET_SIDE;
    private int startDelay;

    @Override
    public void runOpMode() throws InterruptedException {
        ////////////////////
        // INITIALIZATION //
        ////////////////////

        telemetry.addData("Status", "INITIALIZING...");
        telemetry.update();

        Hardware.init(hardwareMap);
        ch = new ControllerHandler(gamepad1);

        DcMotor fR = Hardware.DT_FRONT_RIGHT_MOTOR.get();
        DcMotor fL = Hardware.DT_FRONT_LEFT_MOTOR.get();
        DcMotor bR = Hardware.DT_BACK_RIGHT_MOTOR.get();
        DcMotor bL = Hardware.DT_BACK_LEFT_MOTOR.get();
        dt = new DriveTrain(fR, fL, bR, bL, new DcMotor[] {bL, fL});
        dt.autonomousInit();

        DcMotor slideLeftMotor = Hardware.SLIDE_L_MOTOR.get();
        DcMotor slideRightMotor = Hardware.SLIDE_R_MOTOR.get();
        DcMotor armLeftMotor = Hardware.ARM_L_MOTOR.get();
        DcMotor armRightMotor = Hardware.ARM_R_MOTOR.get();
        Servo clawLeftServo = Hardware.CLAW_L_SERVO.get();
        Servo clawRightServo = Hardware.CLAW_R_SERVO.get();
        ac = new ArmController(slideLeftMotor, slideRightMotor, armLeftMotor, armRightMotor, clawLeftServo, clawRightServo);
        ac.autonomousInit();

        ///////////////////////
        // WAITING FOR START //
        ///////////////////////
        while(!opModeIsActive()) {
            ch.update();

            if(ch.x.onPress()) {
                alliance = alliance == Alliance.RED ? Alliance.BLUE : Alliance.RED;
            }

            if(ch.b.onPress()) {
                startPosition = startPosition == StartPosition.BASKET_SIDE ? StartPosition.OBSERVATION_SIDE : StartPosition.BASKET_SIDE;
            }

            if(ch.dpadUp.onPress() && startDelay + 500 <= 5000) {
                startDelay += 500;
            } else if(ch.dpadDown.onPress() && startDelay - 500 >= 0) {
                startDelay -= 500;
            }

            telemetry.addData("Alliance (x)", alliance);
            telemetry.addData("Start Position (b)", startPosition);
            telemetry.addData("Start Delay (dUp | dDown)", startDelay / 1000d);
            telemetry.addData("Status", "INITIALIZED: Waiting for start...");
            telemetry.update();

            sleep(2);
        }

        ////////////////
        // AUTONOMOUS //
        ////////////////
        telemetry.addData("Status", "Running " + alliance + " " + startPosition);
        telemetry.addData("Task", "Waiting for " + (startDelay / 1000d) + "s");
        telemetry.update();

        sleep(startDelay);


        if(alliance == Alliance.RED) {
            if(startPosition == StartPosition.BASKET_SIDE) {
                redBasketAuto();
            } else {
                redObservationAuto();
            }
        } else {
            if(startPosition == StartPosition.BASKET_SIDE) {
                blueBasketAuto();
            } else {
                blueObservationAuto();
            }
        }
    }

    public void redBasketAuto() {
        telemetry.addData("Task", "Moving from wall...");
        telemetry.update();

        // Move from wall to submersible
        dt.driveToPosition(0.6, 627);

        ac.setArmAngle(-220);

        telemetry.addData("Task", "Moving to submersible...");
        telemetry.update();

        dt.strafeToPosition(0.4, 1505);
        dt.driveToPosition(0.4, 271);

        telemetry.addData("Task", "Hanging specimen...");
        telemetry.update();

        ac.setArmAngle(-200);
        sleep(200);

        // Hang specimen
        dt.driveToPosition(0.2, -100);
        ac.openClaw();

        telemetry.addData("Task", "Moving to extra block...");
        telemetry.update();

        dt.driveToPosition(0.4, -171);

        // Drive to observation zone
        dt.strafeToPosition(0.4, 2508);

        // Drive to block
        dt.driveToPosition(1, 2810);
        ac.setArmAngle(-280);
        dt.driveToPosition(0.4, 200);

        telemetry.addData("Task", "Picking up block...");
        telemetry.update();

        ac.closeClaw();
        sleep(200);
        ac.setArmAngle(-220);

        telemetry.addData("Task", "Parking...");
        telemetry.update();

        // Park in observation zone
        dt.driveToPosition(1, -3637);
    }

    public void redObservationAuto() {
        telemetry.addData("Task", "Moving from wall...");
        telemetry.update();

        dt.driveToPosition(0.4, 300);

        ac.setArmAngle(-220);

        telemetry.addData("Task", "Moving to submersible...");
        telemetry.update();

        // Drive to submersible
        dt.driveToPosition(0.4, 701);

        telemetry.addData("Task", "Hanging specimen...");
        telemetry.update();

        ac.setArmAngle(-200);
        sleep(200);

        // Hang specimen
        dt.driveToPosition(0.2, -100);

        ac.openClaw();

        telemetry.addData("Task", "Moving to extra block...");
        telemetry.update();

        // Backup
        dt.driveToPosition(0.6, -276);

        // Strafe down to observation zone
        dt.strafeToPosition(0.4, 2007);

        // Drive to block
        dt.driveToPosition(1, 2509);
        ac.setArmAngle(-280);
        dt.driveToPosition(0.4, 200);

        telemetry.addData("Task", "Picking up block...");
        telemetry.update();

        ac.closeClaw();
        sleep(200);
        ac.setArmAngle(-220);

        telemetry.addData("Task", "Parking...");
        telemetry.update();

        // Drive to observation zone
        dt.driveToPosition(1, -3336);
    }

    public void blueBasketAuto() {
        telemetry.addData("Task", "Moving from wall...");
        telemetry.update();

        // Move from wall to extend arm
        dt.driveToPosition(0.4, 627);

        ac.setArmAngle(-220);

        telemetry.addData("Task", "Moving to submersible...");
        telemetry.update();

        // Drive to submersible
        dt.strafeToPosition(0.4, 1345);
        dt.driveToPosition(0.4, 381);

        telemetry.addData("Task", "Hanging specimen...");
        telemetry.update();

        ac.setArmAngle(-200);
        sleep(200);

        // Hang specimen
        dt.driveToPosition(0.2, -100);
        ac.openClaw();

        telemetry.addData("Task", "Moving to extra block...");
        telemetry.update();

        dt.driveToPosition(0.4, -281);

        // Drive to observation zone
        dt.strafeToPosition(0.4, 2508);

        // Drive to block
        dt.driveToPosition(1, 2808);
        ac.setArmAngle(-280);
        dt.driveToPosition(0.4, 200);

        telemetry.addData("Task", "Picking up block...");
        telemetry.update();

        ac.closeClaw();
        sleep(200);
        ac.setArmAngle(-220);

        telemetry.addData("Task", "Parking...");
        telemetry.update();


        // Drive to observation zone
        dt.driveToPosition(1, -3637);
    }

    public void blueObservationAuto() {
        telemetry.addData("Task", "Moving from wall...");
        telemetry.update();

        // Move from wall to extend arm
        dt.driveToPosition(0.4, 300);

        ac.setArmAngle(-220);

        telemetry.addData("Task", "Moving to submersible...");
        telemetry.update();

        dt.driveToPosition(0.4, 808);

        telemetry.addData("Task", "Hanging specimen...");
        telemetry.update();

        // Hang specimen
        ac.setArmAngle(-200);
        sleep(200);

        dt.driveToPosition(0.2, -100);
        ac.openClaw();

        telemetry.addData("Task", "Moving to extra block...");
        telemetry.update();

        dt.driveToPosition(0.4, -281);

        // Drive to observation zone
        dt.strafeToPosition(0.4, 1856);

        // Drive to block
        dt.driveToPosition(1, 2810);
        ac.setArmAngle(-280);
        dt.driveToPosition(0.4, 200);

        telemetry.addData("Task", "Picking up block...");
        telemetry.update();

        ac.closeClaw();
        sleep(200);
        ac.setArmAngle(-220);

        telemetry.addData("Task", "Parking...");
        telemetry.update();

        // Drive to observation zone
        dt.driveToPosition(1, -3637);
    }
}
