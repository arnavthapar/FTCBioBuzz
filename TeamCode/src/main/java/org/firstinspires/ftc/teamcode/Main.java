package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Main", group="Linear OpMode")
public class Main extends LinearOpMode {
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor intake;

    double maxDrivePower = 0.5;
    double horizontalInput = 0.0;
    double verticalInput = 0.0;

    @Override
    public void runOpMode() {

        motorLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        motorRight = hardwareMap.get(DcMotor.class, "driveRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        intake = hardwareMap.get(DcMotor.class, "intake");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();

        /*
         * Control Hub
         * 0: intake
         * 1: driveRight
         * 2: frontRight
         * Expansion Hub
         * 0: driveLeft
         * 1: frontLeft
         */

        while (opModeIsActive()) {
            horizontalInput = gamepad1.left_stick_y;
            verticalInput = -gamepad1.left_stick_x;

            if (gamepad1.b) {
                intake.setPower(maxDrivePower);
            } else if (gamepad1.a) {
                intake.setPower(-maxDrivePower);
            } else {
                intake.setPower(0);
            }

            frontLeft.setPower((maxDrivePower * verticalInput + maxDrivePower * horizontalInput));
            motorLeft.setPower((maxDrivePower * verticalInput + maxDrivePower * horizontalInput));
            frontRight.setPower((maxDrivePower * verticalInput - maxDrivePower * horizontalInput));
            motorRight.setPower((maxDrivePower * verticalInput - maxDrivePower * horizontalInput));

        }
    }
}