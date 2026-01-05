package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleOP (goede)", group = "TeleOp")
public class MecanumTeleOp extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private DcMotor launcherLeft = null;
    private DcMotor launcherRight = null;
    private ElapsedTime time = new ElapsedTime();

    //helper vars
    double deltaTime = 0;
    double prevTime = 0;


    @Override
    public void runOpMode() {
        // Map motors to configuration names
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        // Reverse the right side so positive power makes the robot go forward
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
            //get deltaTime
            deltaTime = time.seconds() - prevTime;
            prevTime = time.seconds();

            // Joystick inputs
            double y    =  gamepad1.left_stick_y;    // forward/back
            double x    = -gamepad1.right_stick_x;    // strafe
            double turn = -gamepad1.left_stick_x;   // rotation

            double rightTrigger = gamepad2.right_trigger;
            double leftTrigger = gamepad2.left_trigger;

            if (rightTrigger > 0.1) {
                launcherLeft.setPower(rightTrigger);
                launcherRight.setPower(rightTrigger);
            } else if (leftTrigger > 0.1) {
                launcherLeft.setPower(0.0);
                launcherRight.setPower(0.0);
            }
            double launcherLeftSpeed = launcherLeft.getCurrentPosition() / deltaTime;
            double launcherRightSpeed = launcherLeft.getCurrentPosition() / deltaTime;
            telemetry.addData("MotorSpeed Left: ", launcherLeftSpeed);
            telemetry.addData("MotorSpeed Right: ", launcherRightSpeed);


            // Mecanum drive formulas
            double frontLeftPower  = y + x + turn;
            double frontRightPower = y - x - turn;
            double backLeftPower   = y - x + turn;
            double backRightPower  = y + x - turn;

            // Normalize if any power is outside -1..1
            double max = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower),
                            Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            // Scale to 75% power
            frontLeftPower  *= 0.75;
            frontRightPower *= 0.75;
            backLeftPower   *= 0.75;
            backRightPower  *= 0.75;


            // Apply to motors
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);




            // Telemetry for debugging
            telemetry.addData("FL", frontLeftPower);
            telemetry.addData("FR", frontRightPower);
            telemetry.addData("BL", backLeftPower);
            telemetry.addData("BR", backRightPower);
            telemetry.update();

            telemetry.addData("Launcher Left Power", launcherLeft.getPower());
            telemetry.addData("Launcher Right Power", launcherRight.getPower());
        }
    }
}
