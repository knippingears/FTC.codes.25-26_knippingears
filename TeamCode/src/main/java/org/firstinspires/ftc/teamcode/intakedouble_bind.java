package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "intakedouublebind")
public class intakedouble_bind extends OpMode {

    Servo s1;
    Servo s2;

    // Posities (pas eventueel aan)
    double DOWN_S1 = 0.0;
    double DOWN_S2 = 0.5;

    double HALF_UP_S1 = 0.25;
    double HALF_UP_S2 = 0.25;

    double UP_S1 = 0.5;
    double UP_S2 = 0.0;

    @Override
    public void init() {
        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class, "s2");

        // startpositie
        s1.setPosition(DOWN_S1);
        s2.setPosition(DOWN_S2);
    }

    @Override
    public void loop() {

        // Volledig omhoog → RB (knop boven rechter trigger)
        if (gamepad1.right_bumper) {
            s1.setPosition(UP_S1);
            s2.setPosition(UP_S2);
        }

        // Half omhoog → LB (knop boven linker trigger)
        else if (gamepad1.left_bumper) {
            s1.setPosition(HALF_UP_S1);
            s2.setPosition(HALF_UP_S2);
        }

        // Omlaag → rechter trigger
        else if (gamepad1.right_trigger > 0.5) {
            s1.setPosition(DOWN_S1);
            s2.setPosition(DOWN_S2);
        }
    }
}

