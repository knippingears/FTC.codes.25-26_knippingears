package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "autonomous far", group = "Auto")
public class autonomous_far extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor launcherLeft, launcherRight;

    Servo s1, s2, s3, s4;

    // ===== SERVO POSITIES =====
    double DOWN_S1 = 0;
    double DOWN_S2 = 0.495;

    double HALF_UP_S1 = 0.25;
    double HALF_UP_S2 = 0.21;

    double UP_S1 = 0.46;
    double UP_S2 = 0.0;

    double S3_REST = 0.52;
    double S3_ACTIVE = 1.0;

    // ===== DRIVE CONSTANTS =====
    static final double WHEEL_DIAMETER_M = 0.075;
    static final double TICKS_PER_REV = 140;

    // afstand tussen linker en rechter wielen (schatting)
    static final double TRACK_WIDTH_M = 0.38;

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
        s4 = hardwareMap.get(Servo.class, "s4");

        // Init posities
        s1.setPosition(HALF_UP_S1);
        s2.setPosition(HALF_UP_S2);
        s3.setPosition(S3_REST);
        s4.setPosition(0.3);

        resetDriveEncoders();

        waitForStart();

        // ===== RIJ-PROCES =====

        driveDistance(1.0, 0.5);      // 1 meter vooruit
        turnDegrees(90, 0.4);         // 90° rechts
        driveDistance(2.0, 0.5);      // 2 meter vooruit
        turnDegrees(45, 0.4);         // 45° rechts

        // ===== SCHIET-PROCES =====

        launcherLeft.setPower(0.5);
        launcherRight.setPower(0.5);

        for (int i = 0; i < 3; i++) {

            sleep(5000); // launcher op snelheid

            s3.setPosition(S3_ACTIVE);
            sleep(400);
            s3.setPosition(S3_REST);
            sleep(2000);

            // Na bal 1 en 2 intake omhoog en terug
            if (i == 0 || i == 1) {
                s1.setPosition(UP_S1);
                s2.setPosition(UP_S2);
                sleep(500);
                s1.setPosition(HALF_UP_S1);
                s2.setPosition(HALF_UP_S2);
            }
        }

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
        int ticks = (int)((meters / (Math.PI * WHEEL_DIAMETER_M)) * TICKS_PER_REV);

        setTargetAll(ticks, ticks, ticks, ticks);
        runToPosition(power);
    }

    void turnDegrees(double degrees, double power) {
        double turnCircumference = Math.PI * TRACK_WIDTH_M;
        double distancePerWheel = (degrees / 360.0) * turnCircumference;

        int ticks = (int)((distancePerWheel / (Math.PI * WHEEL_DIAMETER_M)) * TICKS_PER_REV);

        setTargetAll(ticks, -ticks, ticks, -ticks);
        runToPosition(power);
    }

    void setTargetAll(int fl, int fr, int bl, int br) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + fl);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + fr);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + bl);
        backRight.setTargetPosition(backRight.getCurrentPosition() + br);
    }

    void runToPosition(double power) {
        DcMotor[] motors = {frontLeft, frontRight, backLeft, backRight};
        for (DcMotor m : motors) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m.setPower(Math.abs(power));
        }

        while (opModeIsActive() &&
                (frontLeft.isBusy() || frontRight.isBusy()
                        || backLeft.isBusy() || backRight.isBusy())) {
            idle();
        }

        for (DcMotor m : motors) {
            m.setPower(0);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
