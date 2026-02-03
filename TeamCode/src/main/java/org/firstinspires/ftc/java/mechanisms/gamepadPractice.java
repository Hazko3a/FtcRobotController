package org.firstinspires.ftc.java.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class gamepadPractice extends OpMode {

    //nothing within initialise as this would only create a function to occur once
@Override
    public void init() {

}

@Override
            public void loop() {
    // runs 50x* a second
    double speedForward = -gamepad1.left_stick_y / 2.0;
    double diffXSticks = gamepad1.left_stick_x - gamepad1.right_stick_x;


    //creates placeholders for the inputs to link to
    telemetry.addData("x LStick",gamepad1.left_stick_x);
    telemetry.addData("y LStick",speedForward);
    telemetry.addData("x RStick", gamepad1.right_stick_x);
    telemetry.addData("y RStick", gamepad1.right_stick_y);
    telemetry.addData("diff x", diffXSticks);

    telemetry.addData("a button", gamepad1.a);
    telemetry.addData("b button", gamepad1.b);

    telemetry.addData("motor 0", DcMotorSimple.Direction.FORWARD);
    
}

}
