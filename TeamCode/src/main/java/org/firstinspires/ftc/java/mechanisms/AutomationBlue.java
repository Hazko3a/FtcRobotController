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

        // Step 1: Lower ball stopper, turn on flywheel, and Move Backwards 61 cm (~24 inches)
        ballStopper.setPosition(0.35);
        flyWheel.setPower(1.0);
        encoderDrive(DRIVE_SPEED, -24, -24, 1.2);
        
        // Step 2: collection wheel on and ballStopper move into upwards position
        collectionWheel.setPower(-1.0);
        ballStopper.setPosition(0.7);
        sleep(3600);
        
        // Step 3: ballStopper down and turn left -12
        ballStopper.setPosition(0.35);
        encoderDrive(DRIVE_SPEED, -12, 0, 1.2);

        // Step 4: Strafe left by 61 cm (~24 inches)
        strafeDrive(DRIVE_SPEED, -24, 3.0);
        
        // Step 5: Move forward 61 cm (~24 inches) at reduced speed (0.5)
        encoderDrive(0.4, 24, 24, 3.0);
        
        // Step 6: backwards 61cm (~24 inches) and strafe right 61cm (~24 inches)
        encoderDrive(DRIVE_SPEED, -24, -24, 3.5);
        strafeDrive(DRIVE_SPEED, 24, 3.5);
        
        // Step 7: turn right 45 degrees
        encoderDrive(DRIVE_SPEED, 12,0, 1.2);
        
        // Step 8: ballStopper up
        ballStopper.setPosition(0.7);
        sleep(3600);

        // Step 9: turn left 45 degrees and ballStopper down
        encoderDrive(DRIVE_SPEED,0, 12, 1.2);
        ballStopper.setPosition(0.35);
        sleep(500);

        // Step 10: strafe left 122cm (~48 inches)
        strafeDrive(DRIVE_SPEED, -48, 3.0);

        // Step 11: Move forward 61cm at reduced speed (0.5) (~24 inches)
        encoderDrive(0.5, 24, 24, 3.0);

        // Step 12: backwards 61cm (~24 inches) and strafe right 61cm (~24 inches)
        encoderDrive(DRIVE_SPEED, -24, -24, 4.5);
        strafeDrive(DRIVE_SPEED, 48, 4.5);

        // Step 14: turn right 45 degrees
        encoderDrive(DRIVE_SPEED, 0,-12, 1.2);

        //Step 15: ballStopper up
        ballStopper.setPosition(0.7);
        sleep(3600);








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

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontLeft.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                   (frontLeft.isBusy() || frontRight.isBusy())) {
                telemetry.addData("Status", "Driving Encoders");
                telemetry.update();
            }

            stopMotors();
            resetRunMode();
            sleep(250);
        }
    }

    public void strafeDrive(double speed, double inches, double timeoutS) {
        int newFrontLeftTarget;
        int newBackLeftTarget;
        int newFrontRightTarget;
        int newBackRightTarget;

        if (opModeIsActive()) {
            newFrontLeftTarget  = frontLeft.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
            newBackLeftTarget   = backLeft.getCurrentPosition()  - (int)(inches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRight.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
            newBackRightTarget  = backRight.getCurrentPosition()  + (int)(inches * COUNTS_PER_INCH);

            frontLeft.setTargetPosition(newFrontLeftTarget);
            backLeft.setTargetPosition(newBackLeftTarget);
            frontRight.setTargetPosition(newFrontRightTarget);
            backRight.setTargetPosition(newBackRightTarget);

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontLeft.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                   (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())) {
                telemetry.addData("Status", "Strafing");
                telemetry.update();
            }

            stopMotors();
            resetRunMode();
            sleep(250);
        }
    }

    private void stopMotors() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    private void resetRunMode() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
