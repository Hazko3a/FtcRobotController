package org.firstinspires.ftc.java.mechanisms;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name="Drivetrain68", group="Linear OpMode")

public class Drivetrain68 extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    public DcMotor collectionWheel = null;

    public DcMotor flyWheel = null;

    public Servo ballStopper = null;

    public CRServo liftMechanism1 = null;
    public CRServo liftMechanism2 = null;

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
    public void runOpMode() {

      
        frontLeftDrive = hardwareMap.get(DcMotor.class, "Motor 11");
        backLeftDrive = hardwareMap.get(DcMotor.class, "Motor 00");
        frontRightDrive = hardwareMap.get(DcMotor.class, "Motor 1");
        backRightDrive = hardwareMap.get(DcMotor.class, "Motor 0");
        
        // for collection of balls on the robot
        collectionWheel = hardwareMap.get(DcMotor.class, "collection wheel");

        //for the flywheel
        flyWheel = hardwareMap.get(DcMotor.class, "Flywheel");

        //for the ball stopper
        ballStopper = hardwareMap.get(Servo.class, "ballStopper");

        //for the lift mechanism
        liftMechanism1 = hardwareMap.get(CRServo.class, "liftMechanism1");
        liftMechanism2 = hardwareMap.get(CRServo.class, "liftMechanism2");

        liftMechanism1.setDirection(DcMotor.Direction.FORWARD);
        liftMechanism2.setDirection(DcMotor.Direction.REVERSE);


        collectionWheel.setDirection(DcMotor.Direction.REVERSE);

        flyWheel.setDirection(DcMotor.Direction.FORWARD);

        ballStopper.setPosition(0.7);
        
        setLiftPower(0.0);

        
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;


            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double frontLeftPower  = axial + lateral + yaw;
            double frontRightPower = axial - lateral - yaw;
            double backLeftPower   = axial - lateral + yaw;
            double backRightPower  = axial + lateral - yaw;


            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }



            // Send calculated power to wheels
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);

            // collection wheel code
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
                ballStopper.setPosition(0.35);
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

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            
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

    private void setLiftPower(double power) {
        liftMechanism1.setPower(power);
        liftMechanism2.setPower(power);
    }
}
