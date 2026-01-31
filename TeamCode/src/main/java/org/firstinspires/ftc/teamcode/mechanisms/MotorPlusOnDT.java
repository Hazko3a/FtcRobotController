package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorPlusOnDT {
    private DcMotor leftsidemotor, rightsidemotor;

    public void init(HardwareMap hwMap) {
        leftsidemotor = hwMap.get(DcMotor.class,"Motor 0");
        rightsidemotor = hwMap.get(DcMotor.class, "Motor 1");

        // using run using encoder to have motors achieving the same velocity
        leftsidemotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightsidemotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //forward , reverse is used as one of the motors will be opposing the other,
        //and as such requires alternate rotation inputs
        leftsidemotor.setDirection(DcMotor.Direction.FORWARD);

        rightsidemotor.setDirection(DcMotor.Direction.REVERSE);
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

        leftsidemotor.setPower(leftPower);
        rightsidemotor.setPower(rightPower);
        }
    }
