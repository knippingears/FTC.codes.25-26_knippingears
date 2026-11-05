package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TeleOP 2026", group = "TeleOp")
public class TeleOP2026 extends LinearOpMode {

    // Drive motors
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // Intake and shooter motors
    private DcMotor intake;
    private DcMotor shooterLeft, shooterRight;

    @Override
    public void runOpMode() {

        // Hardware configuration
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        intake = hardwareMap.get(DcMotor.class, "intake");

        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");

        // Motor directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            // =================================
            // PLAYER 1 - MECANUM DRIVE
            // =================================

            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x;
            double turn = -gamepad1.right_stick_x;

            // Mecanum drive formulas
            double frontLeftPower = y + x + turn;
            double frontRightPower = y - x - turn;
            double backLeftPower = y - x + turn;
            double backRightPower = y + x - turn;

            // Normalize motor powers
            double max = Math.max(
                    Math.abs(frontLeftPower),
                    Math.max(
                            Math.abs(frontRightPower),
                            Math.max(
                                    Math.abs(backLeftPower),
                                    Math.abs(backRightPower)
                            )
                    )
            );

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }

            // 75% drive power
            frontLeftPower *= 0.75;
            frontRightPower *= 0.75;
            backLeftPower *= 0.75;
            backRightPower *= 0.75;

            // Apply drive power
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);


            // =================================
            // PLAYER 1 - INTAKE
            // =================================

            if (gamepad1.a) {
                intake.setPower(1.0);
            } else {
                intake.setPower(0.0);
            }


            // =================================
            // PLAYER 2 - SHOOTER
            // =================================

            // Right trigger: Left shooter motor
            if (gamepad2.right_trigger > 0.1) {
                shooterLeft.setPower(1.0);
            } else {
                shooterLeft.setPower(0.0);
            }

            // Left trigger: Right shooter motor
            if (gamepad2.left_trigger > 0.1) {
                shooterRight.setPower(1.0);
            } else {
                shooterRight.setPower(0.0);
            }


            // =================================
            // TELEMETRY
            // =================================

            telemetry.addData("Front Left", frontLeftPower);
            telemetry.addData("Front Right", frontRightPower);
            telemetry.addData("Back Left", backLeftPower);
            telemetry.addData("Back Right", backRightPower);

            telemetry.addData("Intake", intake.getPower());
            telemetry.addData("Shooter Left", shooterLeft.getPower());
            telemetry.addData("Shooter Right", shooterRight.getPower());

            telemetry.update();
        }
    }
}