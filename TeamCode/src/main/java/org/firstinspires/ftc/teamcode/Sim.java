package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Simulation Code", group="Linear OpMode")
public class Sim extends LinearOpMode {
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor shootwheel;
    Servo artifactstopper;
    ColorSensor color1;
    DistanceSensor distance1;
    double shootPower;
    BNO055IMU imu;
    double maxDrivePower;
    int horizontalInput = 0;
    int verticalInput = 0;
    boolean isShooting, isFinding = false;
    double yaw2, range2;

    var myVisionPortalBuilder,  myAprilTagDetections, myVisionPortal, myAprilTagDetection, myApriltagProcessor, myAprilTagProcessorBuilder;
    FTCKeyboard keyboard;

    public void initializeVisionPortal(){
        myVisionPortalBuilder = new VisionPortal.Builder();

        myVisionPortalBuilder.setCamera((hardwareMap.get(WebcamName.class, "webcam")));
        myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        myApriltagProcessor = (myAprilTagProcessorBuilder.build());
        myVisionPortalBuilder.addProcessor(myApriltagProcessor);
        myVisionPortal = (myVisionPortalBuilder.build());

    }
    public void findTag() {
        isFinding = true;
        motorLeft.setPower(0);
        motorRight.setPower(0);
        double lateralOffset = 40;
        int i = 0;
        int id = 23;
        motorLeft.setPower(-maxDrivePower);
        motorRight.setPower(maxDrivePower);
        while (((lateralOffset < -18 || lateralOffset > 18) || id != 20) && i < 180) {
            myAprilTagDetections = (myApriltagProcessor.getDetections());
            for (String detection : myAprilTagDetections) {
                lateralOffset = detection.ftcPose.x;
                yaw2 = detection.ftcPose.yaw;

                range2 = detection.ftcPose.range;
                id = detection.id;

            }

            telemetry.update();
            sleep(20);
            i++;
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
        myAprilTagDetections = (myApriltagProcessor.getDetections());
        for (String myAprilTagDetection2 : myAprilTagDetections) {
            myAprilTagDetection = myAprilTagDetection2;
            telemetry.addData("ID", (myAprilTagDetection.id));
            telemetry.addData("Range", (myAprilTagDetection.ftcPose.range));
            telemetry.addData("Yaw", (myAprilTagDetection.ftcPose.yaw));
            telemetry.addData("Lateral Offset", (myAprilTagDetection.ftcPose.x));
        }
        telemetry.update();
    }
    @Override
    public void runOpMode() {
        keyboard = new FTCKeyboard();
        motorLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        motorRight = hardwareMap.get(DcMotor.class, "driveRight");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        artifactstopper.setPosition(0.2);
        maxDrivePower = 1;
        shootPower = 0.8;
        initializeVisionPortal();
        waitForStart();


        while (opModeIsActive()) {
            horizontalInput = keyboard.isPressed(108) - keyboard.isPressed(106);
            verticalInput = keyboard.isPressed(105) - keyboard.isPressed(107);
            maxDrivePower = 0.5;

            if (keyboard.isPressed(112) && !isShooting) {
                shoot();
            } else if (keyboard.isPressed(120) && !isFinding) {
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

    // Put loop blocks here
    // Put initialization blocks here

}