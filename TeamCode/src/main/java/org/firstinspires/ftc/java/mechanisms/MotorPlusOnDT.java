package org.firstinspires.ftc.java.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorPlusOnDT{
    private DcMotor leftSideMotor, rightSideMotor;

    public void init (HardwareMap hwMap) {
        leftSideMotor = hwMap.get(DcMotor.class,"Motor 0");
        rightSideMotor = hwMap.get(DcMotor.class, "Motor 1");

        // using run using encoder to have motors achieving the same velocity
        leftSideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightSideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //forward , reverse is used as one of the motors will be opposing the other,
        //and as such requires alternate rotation inputs
        leftSideMotor.setDirection(DcMotor.Direction.FORWARD);
        rightSideMotor.setDirection(DcMotor.Direction.REVERSE);
    }
    public void setMotorSpeed (double speed) {
        leftSideMotor.setPower(speed);
        rightSideMotor.setPower(speed);
    }
    public void drive(double throttle, double spin)
    {
        //throttle = up or down, spin = left or right
        double leftPower = throttle + spin;
        double rightPower = throttle - spin;
        double largest = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        //done to equalize outputs on each motor
        if (largest > 1.0) {
            leftPower /= largest;
            rightPower /= largest;
        }

        leftSideMotor.setPower(leftPower);
        rightSideMotor.setPower(rightPower);
        }

}
