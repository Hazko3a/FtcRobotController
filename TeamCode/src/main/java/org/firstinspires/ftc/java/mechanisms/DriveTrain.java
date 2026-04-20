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
    public DcMotor flyWheel1 = null;
    public DcMotor flyWheel2 = null;
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
        frontLeft  = hardwareMap.get(DcMotor.class, "Motor 2");
        backLeft   = hardwareMap.get(DcMotor.class, "Motor 3");
        frontRight = hardwareMap.get(DcMotor.class, "Motor 0");
        backRight  = hardwareMap.get(DcMotor.class, "Motor 1");


        // Initialize Mechanisms
        collectionWheel = hardwareMap.get(DcMotor.class, "collection wheel");
        flyWheel1 = hardwareMap.get(DcMotor.class, "Flywheel 1");
        flyWheel2 = hardwareMap.get(DcMotor.class, "Flywheel 2");
        ballStopper = hardwareMap.get(Servo.class, "ballStopper");
        liftMechanism1 = hardwareMap.get(CRServo.class, "liftMechanism1");
        liftMechanism2 = hardwareMap.get(CRServo.class, "liftMechanism2");

        // Set Directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        collectionWheel.setDirection(DcMotor.Direction.REVERSE);
        
        // Flywheel Directions
        flyWheel1.setDirection(DcMotor.Direction.FORWARD);
        flyWheel2.setDirection(DcMotor.Direction.REVERSE);
        
        // Set both flywheels to RUN_WITHOUT_ENCODER to ensure they respond to power directly
        flyWheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flyWheel2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set flywheels to FLOAT so they coast down naturally
        flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flyWheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
        // Set lift mechanism direction
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

    private void setFlywheelPower(double power) {
        flyWheel1.setPower(power);
        flyWheel2.setPower(power);
    }

    @Override
    public void loop() {
        // TANK DRIVE LOGIC
        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;

        // SIDE MOVEMENT LOGIC (STRAFING)
        double strafe = gamepad1.right_trigger - gamepad1.left_trigger;

        // Combine powers for Mecanum Tank Drive
        frontLeft.setPower(leftPower - strafe);
        backLeft.setPower(leftPower + strafe);
        frontRight.setPower(rightPower + strafe);
        backRight.setPower(rightPower - strafe);

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
        setFlywheelPower(flyWheelOn ? 1.0 : 0.0);

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

        // Telemetry for Debugging Flywheels
        telemetry.addData("FW1 Power", "%.2f", flyWheel1.getPower());
        telemetry.addData("FW2 Power", "%.2f", flyWheel2.getPower());
        telemetry.addData("Flywheel Status", flyWheelOn ? "ON" : "OFF");
        telemetry.update();
    }
}
