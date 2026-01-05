package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "intake test")
public class intaketest1 extends OpMode {

    Servo s1;
    Servo s2;

    @Override
    public void init() {
        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class, "s2");

        // startpositie (pas zelf aan)
        s1.setPosition(0);
        s2.setPosition(0.5);
    }

    @Override
    public void loop() {

        if (gamepad2.dpad_up) {
            s1.setPosition(0.5);
            s2.setPosition(0); // tegenovergesteld
        }

        if (gamepad2.dpad_down) {
            s1.setPosition(0);
            s2.setPosition(0.5); // tegenovergesteld
        }
    }
}
