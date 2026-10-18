package org.firstinspires.ftc.teamcode;

import java.util.List;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor.Builder;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.VisionPortal;
@TeleOp(name="Test Code", group="Linear OpMode")
public class Main extends LinearOpMode {
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor shootwheel;
    Servo artifactstopper;
    ColorSensor color1;
    DistanceSensor distance1;
    double shootPower;
    //IMU imu;
    double maxDrivePower;
    double horizontalInput = 0;
    double verticalInput = 0;
    int id = 0;
    double range2, yaw2;
    boolean isFinding;
    boolean isShooting;
    org.firstinspires.ftc.vision.VisionPortal.Builder visionPortalBuilder;
    List<AprilTagDetection> aprilTagDetections;
    VisionPortal ThisVisionPortal;
    AprilTagProcessor ApriltagProcessor;
    Builder AprilTagProcessorBuilder;
    //FTCKeyboard keyboard;

    public void initializeVisionPortal(){
        visionPortalBuilder = new VisionPortal.Builder();
        visionPortalBuilder.setCamera((hardwareMap.get(WebcamName.class, "webcam")));
        AprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        ApriltagProcessor = (AprilTagProcessorBuilder.build());
        visionPortalBuilder.addProcessor(ApriltagProcessor);
        ThisVisionPortal = (visionPortalBuilder.build());

    }
    public void findTag() {
        isFinding = true;
        motorLeft.setPower(0);
        motorRight.setPower(0);
        double lateralOffset = 40;
        id = 23;
        motorLeft.setPower(-maxDrivePower);
        motorRight.setPower(maxDrivePower);
        int i;
        for (i = 0; ((lateralOffset < -18 || lateralOffset > 18) || id != 20) && i < 180; i++) {
            aprilTagDetections= (ApriltagProcessor.getDetections());
            for (AprilTagDetection detection : aprilTagDetections) {
                lateralOffset = detection.ftcPose.x;
                yaw2 = detection.ftcPose.yaw;

                range2 = detection.ftcPose.range;
                id = detection.id;

            }

            telemetry.update();
            sleep(20);
        }
        motorLeft.setPower(0);
        motorRight.setPower(0);
        isFinding = false;
        if (i < 180) {
            if (range2 < 60) {
                shootPower = range2/(range2+18);
            } else if (range2 < 100) {
                shootPower = range2/(range2+15);
            } else if (range2 < 130) {
                shootPower = range2/(range2+10);
            } else {
                shootPower = 1;
            }
            //shootPower = range2/(range2 + 15);
            shootPower = Math.min(Math.max(0, shootPower), 1);
            shoot();
        }
    }
    public void displayVisionPortalData(){
        aprilTagDetections = (ApriltagProcessor.getDetections());
        for (AprilTagDetection detections : aprilTagDetections) {
            telemetry.addData("ID", (detections.id));
            telemetry.addData("Range", (detections.ftcPose.range));
            telemetry.addData("Yaw", (detections.ftcPose.yaw));
            telemetry.addData("Lateral Offset", (detections.ftcPose.x));
        }
        telemetry.update();
    }
    @Override
    public void runOpMode() {
        //keyboard = new FTCKeyboard();
        motorLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        motorRight = hardwareMap.get(DcMotor.class, "driveRight");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        //imu = hardwareMap.get(IMU.class, "imu");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        artifactstopper.setPosition(0.2);
        maxDrivePower = 1;
        shootPower = 0.8;
        initializeVisionPortal();
        waitForStart();


        while (opModeIsActive()) {
            horizontalInput = gamepad1.left_stick_x;
            verticalInput = gamepad1.right_stick_y;
            maxDrivePower = 0.5;

            if (gamepad1.a && !isShooting) {
                shoot();
            } else if (gamepad1.b && !isFinding) {
                findTag();
            }
            motorLeft.setPower((maxDrivePower*verticalInput + maxDrivePower*horizontalInput));
            motorRight.setPower((maxDrivePower*verticalInput -maxDrivePower*horizontalInput));
            displayVisionPortalData();
        }
    }

    public void shoot() {
        // Don't move while shooting
        motorLeft.setPower(0);
        motorRight.setPower(0);
        isShooting = true;
        // Let one artifact come through
        artifactstopper.setPosition(0);
        shootwheel.setPower(shootPower);
        sleep(250);
        // Stop the next artifact
        artifactstopper.setPosition(0.2);
        sleep(200);
        shootwheel.setPower(0);
        sleep(1500);
        // Allow for a new shot to be triggered
        isShooting = false;
    }


}