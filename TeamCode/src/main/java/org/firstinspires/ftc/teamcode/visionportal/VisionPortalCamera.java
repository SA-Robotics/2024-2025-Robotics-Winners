package org.firstinspires.ftc.teamcode.visionportal;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class VisionPortalCamera {
    private AprilTagProcessor atProcessor;
    private TfodProcessor tfodProcessor;
    private VisionPortal vp;
    private WebcamName camera;

    public VisionPortalCamera(WebcamName camera) {
        atProcessor = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();

        tfodProcessor = new TfodProcessor.Builder()
                .setIsModelQuantized(true)
                .setUseObjectTracker(true)
                .build();

        vp = new VisionPortal.Builder()
                .addProcessors(atProcessor, tfodProcessor)
                .setCamera(camera)
                .setCameraResolution(new Size(640, 480))
                .build();
    }

    public List<Recognition> getTFODRecognitions() {
        return tfodProcessor.getRecognitions();
    }

    public List<AprilTagDetection> getATDetections() {
        return atProcessor.getDetections();
    }
}
