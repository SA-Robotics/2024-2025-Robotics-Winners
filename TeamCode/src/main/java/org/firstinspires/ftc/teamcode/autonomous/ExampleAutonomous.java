package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.util.DriveTrain;
import org.firstinspires.ftc.teamcode.util.config.Hardware;
import org.firstinspires.ftc.teamcode.util.visionportal.VisionPortalCamera;

@Autonomous(name = "Example Autonomous", group = "Example")
public class ExampleAutonomous extends LinearOpMode {

    private DriveTrain dt;
    private VisionPortalCamera camera;

    @Override
    public void runOpMode() throws InterruptedException {
        ////////////////////
        // INITIALIZATION //
        ////////////////////
        Hardware.init(hardwareMap);

        DcMotor fR = Hardware.DT_FRONT_RIGHT_MOTOR.get();
        DcMotor fL = Hardware.DT_FRONT_LEFT_MOTOR.get();
        DcMotor bR = Hardware.DT_BACK_RIGHT_MOTOR.get();
        DcMotor bL = Hardware.DT_BACK_LEFT_MOTOR.get();
        dt = new DriveTrain(fR, fL, bR, bL, new DcMotor[] {fL, fR});
        dt.autonomousInit();

        camera = new VisionPortalCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        String status = "Awaiting start";
        while(opModeInInit()) { // Loops until the autonomous is started
            telemetry.addData("Status", status);

            switch(status) {
                case "Awaiting start":
                    status = "Awaiting start.";
                    break;
                case "Awaiting start.":
                    status = "Awaiting start..";
                    break;
                case "Awaiting start..":
                    status = "Awaiting start...";
                    break;
                case "Awaiting start...":
                    status = "Awaiting start";
                    break;
            }

            telemetry.update();
            Thread.sleep(1000);
        }

        // Waits for the autonomous to be started (will carry on if already started)
        waitForStart();

        ///////////
        // START //
        ///////////

        telemetry.addData("Status", "Moving forward");
        telemetry.update();
        dt.driveToPosition(0.5, 1000);

        telemetry.addData("Status", "Moving right");
        telemetry.update();
        dt.strafeToPosition(0.5, 1000);

        telemetry.addData("Status", "Moving back");
        telemetry.update();
        dt.driveToPosition(0.5, -1000);

        telemetry.addData("Status", "Moving left");
        telemetry.update();
        dt.strafeToPosition(0.5, -1000);

        telemetry.addData("Status", "Spinning");
        telemetry.update();
        dt.turnToPosition(1, 1000);

        while(opModeIsActive()) {
            telemetry.addData("Camera", camera.getTFODRecognitions().size() + " recognition(s)");
            telemetry.update();
        }
    }
}
