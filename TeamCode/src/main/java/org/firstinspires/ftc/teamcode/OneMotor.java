package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="One Motor", group="Linear OpMode")
public class OneMotor extends LinearOpMode {
    DcMotor motor;
    final double maxDrivePower = 0.5;
    double input = 0.0;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "drive");
        waitForStart();

        while (opModeIsActive()) {
            input = gamepad1.left_stick_x;
            motor.setPower((maxDrivePower * input));
        }
    }
}