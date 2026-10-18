package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp(name="Robot OpMode", group="Linear OpMode")
public class Example extends LinearOpMode {

    DcMotor driveLeft, driveRight, shootwheel;
    Servo artifactstopper;

    VisionPortal myVisionPortal;
    AprilTagProcessor myAprilTagProcessor;
    List<AprilTagDetection> myAprilTagDetections;

    double shootPower = 0.8;
    boolean isShooting = false;
    double yaw, range;

    public void initializeVisionPortal() {
        myAprilTagProcessor = new AprilTagProcessor.Builder().build();

        myVisionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .addProcessor(myAprilTagProcessor)
                .build();
    }

    public void displayVisionPortalData() {
        // Real Java expects the exact list type
        myAprilTagDetections = myAprilTagProcessor.getDetections();

        for (AprilTagDetection detection : myAprilTagDetections) {
            if (detection.ftcPose != null) {
                yaw = detection.ftcPose.yaw;
                range = detection.ftcPose.range;

                telemetry.addData("ID", detection.id);
                telemetry.addData("Range", range);
                telemetry.addData("Yaw", yaw);
            }
        }
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");

        artifactstopper.setPosition(0.2);
        initializeVisionPortal();

        waitForStart();

        while (opModeIsActive()) {
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            driveLeft.setPower(drive + turn);
            driveRight.setPower(drive - turn);

            if (gamepad1.a && !isShooting) {
                //shoot();
            }

            displayVisionPortalData();
        }
    }
}