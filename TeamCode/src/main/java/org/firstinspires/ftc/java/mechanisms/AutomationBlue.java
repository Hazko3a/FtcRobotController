package org.firstinspires.ftc.java.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Automation Section", group="Robot")
public class AutomationBlue extends LinearOpMode {

    /* Declare OpMode members. */
    public DcMotor  frontLeft   = null;
    public DcMotor  backLeft    = null;
    public DcMotor  frontRight  = null;
    public DcMotor  backRight   = null;

    public DcMotor collectionWheel = null;
    public DcMotor flyWheel = null;
    public Servo ballStopper = null;

    private ElapsedTime runtime = new ElapsedTime();

    // Encoder constants
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 8.5 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;

    @Override
    public void runOpMode() {

        // Initialize Drive Motors (Matching DriveTrain names)
        frontLeft  = hardwareMap.get(DcMotor.class, "Motor 2");
        backLeft   = hardwareMap.get(DcMotor.class, "Motor 3");
        frontRight = hardwareMap.get(DcMotor.class, "Motor 0");
        backRight  = hardwareMap.get(DcMotor.class, "Motor 1");

        // Initialize Mechanisms
        collectionWheel = hardwareMap.get(DcMotor.class, "collection wheel");
        flyWheel = hardwareMap.get(DcMotor.class, "Flywheel");
        ballStopper = hardwareMap.get(Servo.class, "ballStopper");

        // Set Directions (Matching DriveTrain)
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Use BRAKE mode to prevent drifting
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step 1: Move Backwards 61 cm (~24 inches)
        encoderDrive(DRIVE_SPEED, -24, -24, 5.0);

        // Step 2: Spin Flywheel with 2 Sec timeout for ramp up
        flyWheel.setPower(1.0);
        sleep(2000);

        // Step 3: Start Collection Wheel and keep flywheel going for 3.5 Sec timeout
        collectionWheel.setPower(-1.0);
        sleep(3500);

        // Stop flywheel and collection
        flyWheel.setPower(0);
        collectionWheel.setPower(0);
        sleep(200);

        // Step 4: Turn 45 degrees by only moving the left side
        // Distance depends on track width; -11.5 inches is an estimate for 45 degrees on a 15" track.
        encoderDrive(DRIVE_SPEED, -11.5, 0, 3.0);

        // Step 5: Strafe left (Add strafe code here if needed)

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }


    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newFrontLeftTarget;
        int newBackLeftTarget;
        int newFrontRightTarget;
        int newBackRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget  = frontLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newBackLeftTarget   = backLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newBackRightTarget  = backRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);
            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            // Turn On RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Reset the timeout time and start motion.
            runtime.reset();
            frontLeft.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));

            // For pivot turns where one side is 0, only check the busy status of the side that is moving
            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                   (frontLeft.isBusy() || frontRight.isBusy())) {

                // Display status for the driver.
                telemetry.addData("Status", "Driving Encoders");
                telemetry.addData("Actual",  "%7d :%7d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // pause after move
        }
    }
}
