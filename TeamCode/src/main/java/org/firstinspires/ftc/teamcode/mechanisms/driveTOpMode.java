package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class driveTOpMode extends OpMode {


    // creating a new instance of a previously built MotorPlusONDT code
  MotorPlusOnDT drive = new MotorPlusOnDT();
    double throttle, spin;

    @Override
    public void init() {
        drive.init(hardwareMap);
    }


    //giving the function an input to source values from
    @Override
    public void loop() {
        //using "-" to counteract on inversion on the left stick y
      throttle  = -gamepad1.left_stick_y;
      spin = gamepad1.left_stick_x;

      drive.drive(throttle, spin);
    }

}
