package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Two Motors", group="Linear OpMode")
public class TwoMotors extends LinearOpMode {
    DcMotor motorLeft;
    DcMotor motorRight;

    double maxDrivePower = 0.5;
    double horizontalInput = 0.0;
    double verticalInput = 0.0;

    @Override
    public void runOpMode() {
        motorLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        motorRight = hardwareMap.get(DcMotor.class, "driveRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();


        while (opModeIsActive()) {
            horizontalInput = gamepad1.left_stick_x;
            verticalInput = gamepad1.right_stick_y;

            motorLeft.setPower((maxDrivePower * verticalInput + maxDrivePower * horizontalInput));
            motorRight.setPower((maxDrivePower * verticalInput - maxDrivePower * horizontalInput));
        }
    }
}