
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Intake Lift Sequence")
public class intaketest2 extends OpMode {

    Servo s1; // duw L-vorm
    Servo s2; // lift L-vorm

    int step = 0;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class, "s2");

        // startpositie
        s1.setPosition(0.0);
        s2.setPosition(0.5);
    }

    @Override
    public void loop() {

        // start sequence
        if (gamepad2.a && step == 0) {
            step = 1;
            timer.reset();
        }

        // STEP 1: duw bal
        if (step == 1) {
            s1.setPosition(0.5);
            if (timer.seconds() > 0.4) {
                step = 2;
                timer.reset();
            }
        }

        // STEP 2: til bal omhoog
        if (step == 2) {
            s2.setPosition(0.0);
            if (timer.seconds() > 0.4) {
                step = 3;
            }
        }

        telemetry.addData("Step", step);
        telemetry.update();
    }
}
