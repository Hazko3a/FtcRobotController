package org.firstinspires.ftc.java.mechanisms;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp

public class DriveTrain extends OpMode {

    /* Declare OpMode members. */
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    public DcMotor collectionWheel = null;

    @Override
    public void init() {
        // Define and Initialize Motors
        leftDrive  = hardwareMap.get(DcMotor.class, "Motor 0");
        rightDrive = hardwareMap.get(DcMotor.class, "Motor 1");

        // for collection of balls on the robot
        collectionWheel = hardwareMap.get(DcMotor.class, "Hex Motor 2");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left and right sticks forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        collectionWheel.setDirection(DcMotor.Direction.FORWARD);

        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Robot Ready.  Press START.");    //
    }

    @Override
    public void loop() {
        double left;
        double right;

        //booleans read true-false in comparison to joysticks using double which read values of numbers.



        // Run wheels in tank mode (note: The joystick goes negative when pushed forward, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;


        leftDrive.setPower(left);
        rightDrive.setPower(right);


    }



}