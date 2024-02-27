package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.DriveTrain;
import org.firstinspires.ftc.teamcode.visionportal.VisionPortalCamera;

@Autonomous(name = "Example Autonomous", group = "Example")
public class ExampleAutonomous extends LinearOpMode {

    private DriveTrain dt;
    private VisionPortalCamera camera;

    @Override
    public void runOpMode() throws InterruptedException {
        ////////////////////
        // INITIALIZATION //
        ////////////////////

        DcMotor fR = hardwareMap.get(DcMotor.class, "FrontRight");
        DcMotor fL = hardwareMap.get(DcMotor.class, "FrontLeft");
        DcMotor bR = hardwareMap.get(DcMotor.class, "BackRight");
        DcMotor bL = hardwareMap.get(DcMotor.class, "BackLeft");
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

        dt.driveToPosition(0.5, 1000);
        dt.strafeToPosition(0.5, 1000);
        dt.driveToPosition(0.5, -1000);
        dt.strafeToPosition(0.5, -1000);

        dt.turnToPosition(1, 1000);

        // Autonomous will end automatically unless while(opModeIsActive)
    }
}
