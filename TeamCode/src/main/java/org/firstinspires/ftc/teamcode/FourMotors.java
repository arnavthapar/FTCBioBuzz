package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Four Motors", group="Linear OpMode")
public class FourMotors extends LinearOpMode {
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
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        intake = hardwareMap.get(DcMotor.class, "inttake");
        waitForStart();

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