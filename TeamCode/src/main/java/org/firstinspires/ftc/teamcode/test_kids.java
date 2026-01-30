package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Test kids", group = "TeleOp")
public class test_kids extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    private DcMotor launcherLeft = null;
    private DcMotor launcherRight = null;
    private ElapsedTime time = new ElapsedTime();

    //helper vars
    double deltaTime = 0;
    double prevTime = 0;

    Servo s1;
    Servo s2;

    // Servo posities
    double DOWN_S1 = 0;
    double DOWN_S2 = 0.495;

    double HALF_UP_S1 = 0.25;
    double HALF_UP_S2 = 0.21;

    double UP_S1 = 0.46;
    double UP_S2 = 0.0;

    Servo s3;

    // Servo 3 posities
    double S3_REST = 0.51;
    double S3_ACTIVE = 1.0;

    // Timer voor servo 3
    ElapsedTime s3Timer = new ElapsedTime();
    boolean s3IsActive = false;

    Servo s4;



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

        final double LAUNCHER_POWER = 0.5;
        launcherLeft.setDirection(DcMotor.Direction.FORWARD);
        launcherRight.setDirection(DcMotor.Direction.REVERSE);


        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class, "s2");

        // Startpositie
        s1.setPosition(DOWN_S1);
        s2.setPosition(DOWN_S2);

        s3 = hardwareMap.get(Servo.class, "s3");
        s3.setPosition(S3_REST);

        s4 = hardwareMap.get(Servo.class, "s4");
        s4.setPosition(0.2); // startpositie

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
                launcherLeft.setPower(LAUNCHER_POWER);
                launcherRight.setPower(LAUNCHER_POWER);
            } else if (leftTrigger > 0.1) {
                launcherLeft.setPower(0.0);
                launcherRight.setPower(0.0);
            }

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
            frontLeftPower  *= 0.4;
            frontRightPower *= 0.4;
            backLeftPower   *= 0.4;
            backRightPower  *= 0.4;


            // Apply to motors
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            // Volledig omhoog → RB (player 1)
            if (gamepad2.y) {
                s1.setPosition(UP_S1);
                s2.setPosition(UP_S2);
            }

            // Half omhoog → LB (player 1)
            else if (gamepad2.x) {
                s1.setPosition(HALF_UP_S1);
                s2.setPosition(HALF_UP_S2);
            }

            // Omlaag → rechter trigger (player 1)
            else if (gamepad2.a) {
                s1.setPosition(DOWN_S1);
                s2.setPosition(DOWN_S2);
            }

            // Servo 3 omhoog → RB (player 2)
            if (gamepad2.right_bumper && !s3IsActive) {
                s3.setPosition(S3_ACTIVE);
                s3Timer.reset();
                s3IsActive = true;
            }

            // Servo 3 terug → LB (player 2)
            if (gamepad2.left_bumper) {
                s3.setPosition(S3_REST);
                s3IsActive = false;
            }

            // Automatisch terug na 2 seconden
            if (s3IsActive && s3Timer.seconds() >= 2.0) {
                s3.setPosition(S3_REST);
                s3IsActive = false;
            }

            // Servo s4 – player 1 D-pad
            if (gamepad1.right_bumper) {
                s4.setPosition(0.45); // naar beneden
            }

            if (gamepad1.left_bumper) {
                s4.setPosition(0.2); // naar boven
            }

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

