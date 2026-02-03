package org.firstinspires.ftc.java.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class ifFunction extends OpMode {

    @Override
    public void init() {


    }

    @Override
    public void loop() {
        boolean aButton = gamepad1.a;

        if (aButton) {
            telemetry.addData("A Button", "Pressed");
    }
        else {
            telemetry.addData("A Button", "Not Pressed");
        }
        telemetry.addData("A Button State", aButton);

}

}
