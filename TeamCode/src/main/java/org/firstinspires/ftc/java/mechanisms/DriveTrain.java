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
    public DcMotor  frontLeft   = null;
    public DcMotor  backLeft    = null;
    public DcMotor  frontRight  = null;
    public DcMotor  backRight   = null;
    
    public DcMotor collectionWheel = null;
    public DcMotor flyWheel = null;
    public Servo ballStopper = null;

    public CRServo liftMechanism1 = null;
    public CRServo liftMechanism2 = null;

    // Control variables
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
        // Initialize Drive Motors
        frontLeft  = hardwareMap.get(DcMotor.class, "Motor 2 nbgyfhnm ");
        backLeft   = hardwareMap.get(DcMotor.class, "Motor 3");
        frontRight = hardwareMap.get(DcMotor.class, "Motor 0");
        backRight  = hardwareMap.get(DcMotor.class, "Motor 1");

        // Initialize Mechanisms
        collectionWheel = hardwareMap.get(DcMotor.class, "collection wheel");
        flyWheel = hardwareMap.get(DcMotor.class, "Flywheel");
        ballStopper = hardwareMap.get(Servo.class, "ballStopper");
        liftMechanism1 = hardwareMap.get(CRServo.class, "liftMechanism1");
        liftMechanism2 = hardwareMap.get(CRServo.class, "liftMechanism2");

        // Set Directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        collectionWheel.setDirection(DcMotor.Direction.REVERSE);
        flyWheel.setDirection(DcMotor.Direction.FORWARD);
        liftMechanism1.setDirection(DcMotor.Direction.FORWARD);
        liftMechanism2.setDirection(DcMotor.Direction.REVERSE);

        // Initial States
        ballStopper.setPosition(0.7);
        setLiftPower(0.0);

        telemetry.addData(">", "Robot Ready. Press START.");
    }

    private void setLiftPower(double power) {
        liftMechanism1.setPower(power);
        liftMechanism2.setPower(power);
    }

    @Override
    public void loop() {
        // TANK DRIVE LOGIC
        // Left stick Y controls the entire left side
        // Right stick Y controls the entire right side
        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;

        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);

        // Collection Wheel Toggle (Button B)
        if (gamepad1.b && !bPressed) {
            collectionWheelOn = !collectionWheelOn;
        }
        bPressed = gamepad1.b;
        collectionWheel.setPower(collectionWheelOn ? 1.0 : 0.0);

        // Flywheel Toggle (Button A)
        if (gamepad1.a && !aPressed) {
            flyWheelOn = !flyWheelOn;
        }
        aPressed = gamepad1.a;
        flyWheel.setPower(flyWheelOn ? 1.0 : 0.0);

        // Ball Stopper Toggle (Button Y)
        if (gamepad1.y && !yPressed) {
            ballStopperOn = !ballStopperOn;
        }
        yPressed = gamepad1.y;
        ballStopper.setPosition(ballStopperOn ? 0.35 : 0.7);

        // Lift Mechanism Control (Button X)
        if (gamepad1.x && !xPressed) {
            liftMechanismOn = !liftMechanismOn;
            if (liftMechanismOn) {
                liftTimer.reset();
                liftForward = !liftForward;
            }
        }
        xPressed = gamepad1.x;

        if (liftMechanismOn && liftTimer.seconds() < 1.8) {
            setLiftPower(liftForward ? 1.0 : -1.0);
        } else {
            setLiftPower(0.0);
            liftMechanismOn = false;
        }

        // Telemetry
        telemetry.addData("Drive", "Tank Mode (Dual Stick)");
        telemetry.addData("Left Power", "%.2f", leftPower);
        telemetry.addData("Right Power", "%.2f", rightPower);
        telemetry.addData("Flywheel", flyWheelOn ? "ON" : "OFF");
        telemetry.update();
    }
}
