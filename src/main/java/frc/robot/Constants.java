package frc.robot;
public class Constants{
    /**
     * Joystick
     */
    public final static int driver_Control = 0;
    public final static int operator_Control = 3;
    /**
     * Drive train motor
     */
    public final static int mLeftMaster = 1;
    public final static int mLeftFollow1 = 2;
    public final static int mLeftFollow2 = 3;
    public final static int mRightMaster = 4;
    public final static int mRightMfollow1 = 5;
    public final static int mRightMfollow2 = 6;

    /**
     * Shooter motor
     */
    public final static int mLeftShooter = 7;
    public final static int mRightShooter = 8;
    
    /**
     * Pivot Motor
     */
    public final static int mLeftPivot = 9;
    public final static int mRightPivot = 10;

    /**
     * Intake motor
     */
    public final static int mIntake = 11;

    /**
     * Climb Motor
     */
    public final static int mClimb = 12;

    /**
     * Index Motor
     */
    public final static int mLeftIndex = 13;
    public final static int mRightIndex = 14;

    /**
     * Pivot PID and feedForward control
     */
    public final static double pKp = 0.0002;
    public final static double pKi = 0.00002;
    public final static double pKd = 0.00001;
    /**
     * The feedforward PID
     */
    public final static double pKs = 0.28;
    public final static double pKcos = -0.421;
    public final static double pKv = 0.0462;
    public final static double pKa = 0.001;
    /**
     * The PID for the rate on the drive train
    */
    public final static double dKp = 0.0008;
    public final static double dKi = 0.000007;
    public final static double dKd = 0.000025;
    

    /**
     * camera
     */
    public final static int cWidth = 640;
    public final static int cHeight = 480;
    /**
     * values for grippipelines
     */
    public static double[] hsvThresholdHue = {40, 98};
    public static double[] hsvThresholdSaturation = {99, 220};
    public static double[] hsvThresholdValue = {140, 255};
    /**
     * view angles
     */
    public static double horizontalViewAngle = 61;
    public static double verticalViewAngle = 20.55;


    


    /**
     * The degrees per pixel on the Microsoft Life Cam HD -3000
     */
    public static double deg_per_px = verticalViewAngle / cHeight;
    /**
     * The diameter of the drive train wheels
     */
    public static double wheelDiam = 6;//in.
    /**
     * the pulses per revolution on the drive train encoders
     */
    public static int pulsePerRev = 8192;
    /**
     * The Distance per pulses
     */
    public static double wheelcircumference = (wheelDiam * Math.PI);
    /**
     * The Tolerance for target distance
     */
    public static double distanceTolerance = 2;
    /**
     * The tolerance for the target angle
     */
    public static double angleTolerance = 1;
    
    
}