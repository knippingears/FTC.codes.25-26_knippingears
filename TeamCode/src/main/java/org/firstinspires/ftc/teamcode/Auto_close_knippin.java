package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Auto Shoot 1.5m", group = "Auto")
public class AutoShootV1 extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor launcherLeft, launcherRight;

    Servo s1, s2, s3;

    // ===== SERVO POSITIES (uit TeleOp) =====
    double DOWN_S1 = 0;
    double DOWN_S2 = 0.495;

    double HALF_UP_S1 = 0.25;
    double HALF_UP_S2 = 0.21;

    double UP_S1 = 0.46;
    double UP_S2 = 0.0;

    double S3_REST = 0.52;
    double S3_ACTIVE = 1.0;

    // ===== DRIVE CONSTANTS =====
    static final double WHEEL_DIAMETER_M = 0.096;
    static final double TICKS_PER_REV = 537.6; // ⚠️ pas aan indien nodig
    static final double DISTANCE_M = 1.5;

    @Override
    public void runOpMode() {

        // Motors
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        launcherLeft  = hardwareMap.get(DcMotor.class, "launcherLeft");
        launcherRight = hardwareMap.get(DcMotor.class, "launcherRight");

        launcherRight.setDirection(DcMotor.Direction.REVERSE);

        // Servos
        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class, "s2");
        s3 = hardwareMap.get(Servo.class, "s3");

        // Intake start → HALF
        s1.setPosition(HALF_UP_S1);
        s2.setPosition(HALF_UP_S2);

        s3.setPosition(S3_REST);

        // Encoders reset
        resetDriveEncoders();

        waitForStart();

        // ===== 1. RIJD 1,5 METER ACHTERUIT =====
        driveDistance(-DISTANCE_M, 0.5);

        // ===== 2. START LAUNCHERS =====
        launcherLeft.setPower(0.5);
        launcherRight.setPower(0.5);

        // ===== 3. SCHIET 3 BALLEN =====
        for (int i = 0; i < 3; i++) {

            sleep(2000); // launcher op snelheid

            // Schiet
            s3.setPosition(S3_ACTIVE);
            sleep(400);
            s3.setPosition(S3_REST);
            sleep(400);

            // Na eerste bal → intake omhoog
            if (i == 0) {
                s1.setPosition(UP_S1);
                s2.setPosition(UP_S2);
                sleep(500);

                s1.setPosition(HALF_UP_S1);
                s2.setPosition(HALF_UP_S2);
            }
        }

        // Stop launcher
        launcherLeft.setPower(0);
        launcherRight.setPower(0);
    }

    // ===== HULPFUNCTIES =====

    void resetDriveEncoders() {
        DcMotor[] motors = {frontLeft, frontRight, backLeft, backRight};
        for (DcMotor m : motors) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    void driveDistance(double meters, double power) {
        int ticks = (int)(
                (meters / (Math.PI * WHEEL_DIAMETER_M)) * TICKS_PER_REV
        );

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + ticks);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + ticks);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + ticks);
        backRight.setTargetPosition(backRight.getCurrentPosition() + ticks);

        DcMotor[] motors = {frontLeft, frontRight, backLeft, backRight};
        for (DcMotor m : motors) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m.setPower(Math.abs(power));
        }

        while (opModeIsActive() &&
                (frontLeft.isBusy() || frontRight.isBusy() ||
                        backLeft.isBusy() || backRight.isBusy())) {
            idle();
        }

        for (DcMotor m : motors) {
            m.setPower(0);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
