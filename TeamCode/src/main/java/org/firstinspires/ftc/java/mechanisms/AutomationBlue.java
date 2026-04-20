package org.firstinspires.ftc.java.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    public DcMotor flyWheel1 = null;
    public DcMotor flyWheel2 = null;
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
        flyWheel1 = hardwareMap.get(DcMotor.class, "Flywheel 1");
        flyWheel2 = hardwareMap.get(DcMotor.class, "Flywheel 2");
        ballStopper = hardwareMap.get(Servo.class, "ballStopper");

        // Set Directions (Matching DriveTrain)
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        collectionWheel.setDirection(DcMotor.Direction.FORWARD);
        
        // Flywheel Directions (Matching DriveTrain)
        flyWheel1.setDirection(DcMotor.Direction.FORWARD);
        flyWheel2.setDirection(DcMotor.Direction.REVERSE);

        // Flywheel behavior (Explicitly reset and set mode)
        flyWheel1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flyWheel2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flyWheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Use BRAKE mode for drive motors
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset encoders
        resetEncoders();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step 1: Lower ball stopper, turn on flywheels, and Move Backwards 61 cm (~24 inches)
        ballStopper.setPosition(0.7);
        setFlywheelPower(0.6);
        sleep(4000);
        encoderDrive(DRIVE_SPEED, -24, -24, 1.2);
        sleep(1200);
        
        // Step 2: collection wheel on and ballStopper move into upwards position
        collectionWheel.setPower(-1.0);
        ballStopper.setPosition(0.35);
        sleep(3600);
        
        // Step 3: ballStopper down and turn left -12
        ballStopper.setPosition(0.7);
        encoderDrive(DRIVE_SPEED, -12, 0, 1.2);

        // Step 4: Strafe left by 61 cm (~24 inches)
        strafeDrive(DRIVE_SPEED, -24, 3.0);
        
        // Step 5: Move forward 61 cm (~24 inches) at reduced speed (0.5)
        encoderDrive(0.4, 24, 24, 3.0);

        // Step 6: backwards 61cm and strafe right 61cm SIMULTANEOUSLY (Diagonal move)
        mecanumDrive(DRIVE_SPEED, 0, -48, -48, 0, 3.5);
        
        // Step 7: turn right 45 degrees
        encoderDrive(DRIVE_SPEED, 12,0, 1.2);
        
        // Step 8: ballStopper up
        ballStopper.setPosition(0.35);
        sleep(3600);

        // Step 9: turn left 45 degrees and ballStopper down
        encoderDrive(DRIVE_SPEED,0, 12, 1.2);
        ballStopper.setPosition(0.7);
        sleep(500);

        // Step 10: strafe left 122cm (~48 inches)
        strafeDrive(DRIVE_SPEED, -48, 3.0);

        // Step 11: Move forward 61cm at reduced speed (0.5) (~24 inches)
        encoderDrive(0.5, 24, 24, 3.0);

        // Step 12: backwards 61cm and strafe right 122cm (~48 inches) SIMULTANEOUSLY (Diagonal move)
        mecanumDrive(DRIVE_SPEED, 24, -72, -72, 24, 4.5);

        // Step 14: turn right 45 degrees
        encoderDrive(DRIVE_SPEED, 0,-12, 1.2);

        //Step 15: ballStopper up
        ballStopper.setPosition(0.35);
        setFlywheelPower(0.0);
        sleep(3600);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    private void setFlywheelPower(double power) {
        flyWheel1.setPower(power);
        flyWheel2.setPower(power);
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        mecanumDrive(speed, leftInches, leftInches, rightInches, rightInches, timeoutS);
    }

    public void strafeDrive(double speed, double inches, double timeoutS) {
        // Strafe Right (positive inches): FL+, BL-, FR-, BR+
        mecanumDrive(speed, inches, -inches, -inches, inches, timeoutS);
    }

    public void mecanumDrive(double speed,
                             double flInches, double blInches,
                             double frInches, double brInches,
                             double timeoutS) {
        int newFrontLeftTarget;
        int newBackLeftTarget;
        int newFrontRightTarget;
        int newBackRightTarget;

        if (opModeIsActive()) {

            newFrontLeftTarget  = frontLeft.getCurrentPosition() + (int)(flInches * COUNTS_PER_INCH);
            newBackLeftTarget   = backLeft.getCurrentPosition() + (int)(blInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRight.getCurrentPosition() + (int)(frInches * COUNTS_PER_INCH);
            newBackRightTarget  = backRight.getCurrentPosition() + (int)(brInches * COUNTS_PER_INCH);

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
                telemetry.addData("Status", "Mecanum Drive");
                telemetry.addData("FW1 Power", "%.2f", flyWheel1.getPower());
                telemetry.addData("FW2 Power", "%.2f", flyWheel2.getPower());
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

    private void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        resetRunMode();
    }

    private void resetRunMode() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
