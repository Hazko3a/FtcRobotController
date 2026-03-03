package org.firstinspires.ftc.java.mechanisms;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp

public class DriveTrain extends OpMode {

    /* Declare OpMode members. */
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    public DcMotor collectionWheel = null;

    public DcMotor flyWheel = null;

    public Servo ballStopper = null;

    public CRServo liftMechanism1 = null;
    public CRServo liftMechanism2 = null;




    //boolean for the collection wheel
    private boolean collectionWheelOn = false;
    private boolean bPressed = false;


    private boolean flyWheelOn = false;
    private boolean aPressed = false;


    private boolean ballStopperOn = false;
    private boolean yPressed = false;


    private boolean liftMechanismOn = false;
    private boolean liftForward = true;
    private boolean xPressed = false;
    private final ElapsedTime liftTimer = new ElapsedTime();


    @Override
    public void init() {
        // Define and Initialize Motors
        leftDrive  = hardwareMap.get(DcMotor.class, "Motor 0");
        rightDrive = hardwareMap.get(DcMotor.class, "Motor 1");

        // for collection of balls on the robot
        collectionWheel = hardwareMap.get(DcMotor.class, "Hex Motor 2");

        //for the flywheel
        flyWheel = hardwareMap.get(DcMotor.class, "Flywheel Motor 3");

        //for the ball stopper
        ballStopper = hardwareMap.get(Servo.class, "ballStopper");

        //for the lift mechanism
        liftMechanism1 = hardwareMap.get(CRServo.class, "liftMechanism1");
        liftMechanism2 = hardwareMap.get(CRServo.class, "liftMechanism2");

        /* 
           MATCHING LOGIC: 
           If servos are mounted on opposite sides (mirror-imaged), 
           one MUST be reversed so they "match" and move the lift together.
        */
        liftMechanism1.setDirection(DcMotor.Direction.FORWARD);
        liftMechanism2.setDirection(DcMotor.Direction.REVERSE);


        

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left and right sticks forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);


        collectionWheel.setDirection(DcMotor.Direction.REVERSE);

        flyWheel.setDirection(DcMotor.Direction.FORWARD);

        ballStopper.setPosition(0.7);



        setLiftPower(0.0);



        // Send telemetry message to signify robot waiting;
        telemetry.addData(">", "Robot Ready.  Press START.");    //
    }

    // Helper method to ensure both servos always get the same command
    private void setLiftPower(double power) {
        liftMechanism1.setPower(power);
        liftMechanism2.setPower(power);
    }

    @Override
    public void loop() {
        double left;
        double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forward, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;

        leftDrive.setPower(left);
        rightDrive.setPower(right);


        // a few different codes here to help compact the coding

        //collection wheel code
        if (gamepad1.b && !bPressed) {
            collectionWheelOn = !collectionWheelOn;
        }
        bPressed = gamepad1.b;

        if (collectionWheelOn) {
            collectionWheel.setPower(1.0);
        } else {
            collectionWheel.setPower(0.0);
        }

        //flywheel code
        if (gamepad1.a && !aPressed) {
            flyWheelOn = !flyWheelOn;
        }
        aPressed = gamepad1.a;

        if (flyWheelOn) {
            flyWheel.setPower(1.0);
        } else {
            flyWheel.setPower(0.0);
        }

        //ball stopper code
        if (gamepad1.y && !yPressed) {
            ballStopperOn = !ballStopperOn;
        }
        yPressed = gamepad1.y;

        if (ballStopperOn) {
            ballStopper.setPosition(0.2);
        } else {
            ballStopper.setPosition(0.7);
        }

        //lift mechanism code
        if (gamepad1.x && !xPressed) {
            liftMechanismOn = !liftMechanismOn;
            if (liftMechanismOn) {
                liftTimer.reset();
                liftForward = !liftForward; // Toggles direction each time it starts
            }
        }
        xPressed = gamepad1.x;

        double LIFT_DURATION = 1.8;
        if (liftMechanismOn && liftTimer.seconds() < LIFT_DURATION) {

            double LIFT_SPEED = 1.0;
            setLiftPower(liftForward ? LIFT_SPEED : -LIFT_SPEED);
        } else {
            setLiftPower(0.0);
            liftMechanismOn = false;
        }


        //shows value of flywheel
        telemetry.addData("gamepad1.a", gamepad1.a);
        telemetry.addData("aPressed", aPressed);
        telemetry.addData("flyWheelOn", flyWheelOn);
        //shows value of ball stopper
        telemetry.addData("gamepad1.y", gamepad1.y);
        telemetry.addData("yPressed", yPressed);
        telemetry.addData("ballStopperOn", ballStopperOn);
        telemetry.addData("ballStopper Position", ballStopper.getPosition());
        //shows value of lift mechanism
        telemetry.addData("gamepad1.x", gamepad1.x);
        telemetry.addData("xPressed", xPressed);
        telemetry.addData("liftMechanismOn", liftMechanismOn);
        telemetry.addData("lift1 Power", liftMechanism1.getPower());
        telemetry.addData("lift2 Power", liftMechanism2.getPower());
        //shows value of collection wheel
        telemetry.addData("gamepad1.b", gamepad1.b);
        telemetry.addData("bPressed", bPressed);
        telemetry.addData("collectionWheelOn", collectionWheelOn);

        telemetry.update();
    }

}
