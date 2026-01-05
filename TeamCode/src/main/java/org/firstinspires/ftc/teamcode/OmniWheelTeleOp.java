package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "OmniWheel TeleOp", group = "TeleOp")
public class OmniWheelTeleOp extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private DcMotor launcherLeft = null;
    private DcMotor launcherRight = null;

    @Override
    public void runOpMode() {
        // Map motors to configuration names
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Reverse the right side so positive power makes all wheels go forward
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        launcherLeft = hardwareMap.get(DcMotor.class, "launcherLeft");
        launcherRight = hardwareMap.get(DcMotor.class, "launcherRight");

        launcherLeft.setDirection(DcMotor.Direction.FORWARD);
        launcherRight.setDirection(DcMotor.Direction.REVERSE);

        launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        while (opModeIsActive()) {
            // Joystick inputs
            double y = -gamepad1.left_stick_y;   // forward/back
            double x = -gamepad1.left_stick_x;   // strafe (omgedraaid)
            double turn = -gamepad1.right_stick_x; // rotation (omgedraaid)

            double rightTrigger = gamepad2.right_trigger;
            double leftTrigger = gamepad2.left_trigger;

            if (rightTrigger > 0.1) {
                launcherLeft.setPower(1.0);
                launcherRight.setPower(1.0);
            } else if (leftTrigger > 0.1) {
                launcherLeft.setPower(0.0);
                launcherRight.setPower(0.0);
            }

            // Calculate wheel powers
            // Calculate wheel powers
            double frontLeftPower = y + (x * 1.15) + turn;   // slight boost to front left
            double backLeftPower = y - x + turn;
            double frontRightPower = y - (x * 1.10) - turn;   // slight reduction to front right
            double backRightPower = y + x - turn;


            // Normalize powers if any > 1.0
            double max = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(backLeftPower),
                            Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));
            if (max > 1.0) {
                frontLeftPower /= max;
                backLeftPower /= max;
                frontRightPower /= max;
                backRightPower /= max;
            }

            // Vermogen beperken tot 75%
            frontLeftPower *= 0.75;
            backLeftPower *= 0.75;
            frontRightPower *= 0.75;
            backRightPower *= 0.75;

            // Set powers
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // Telemetry for debugging
            telemetry.addData("Launcher Left Power", launcherLeft.getPower());
            telemetry.addData("Launcher Right Power", launcherRight.getPower());
            telemetry.addData("FL", frontLeftPower);
            telemetry.addData("FR", frontRightPower);
            telemetry.addData("BL", backLeftPower);
            telemetry.addData("BR", backRightPower);
            telemetry.update();



        }
    }
}