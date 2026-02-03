package org.firstinspires.ftc.teamcode.robotinfo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class Gamepad extends OpMode {

@Override
    public void init() {

}

@Override
            public void loop() {
    // runs 50x* a second
    double speedForward = -gamepad1.left_stick_y / 2.0;
    double diffXsticks = gamepad1.left_stick_x - gamepad1.right_stick_x;
    double sumTriggers = gamepad1.left_trigger = gamepad1.right_trigger;

    telemetry.addData("x lstick",gamepad1.left_stick_x);
    telemetry.addData("y lstick",speedForward);
    telemetry.addData("x rstick", gamepad1.right_stick_x);
    telemetry.addData("y rstick" gamepad1.right_stick_y);
    telemetry.addData("diff x", diffXsticks);

    telemetry.addData("a button", gamepad1.a);
    telemetry.addData("b button", gamepad1.b);

    telemetry.addData("sum triggers", gamepad1.left_trigger + gamepad1.right_trigger);

}
}
