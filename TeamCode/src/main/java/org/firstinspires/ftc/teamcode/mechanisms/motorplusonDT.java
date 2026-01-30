package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class motorplusonDT {
    private DcMotor leftsidemotor, rightsidemotor;

    public void init(HardwareMap hwMap) {
        leftsidemotor = hwMap.get(DcMotor.class,"Motor 0");
        rightsidemotor = hwMap.get(DcMotor.class, "Motor 1");
        

    }
}
