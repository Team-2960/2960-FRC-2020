package frc.robot;
public class Constants{
    //Joystick
    public final static int driver_Control = 0;
    public final static int operator_Control = 3;
    //Drive train motor
    public final static int mLeftMaster = 1;
    public final static int mLeftFollow1 = 2;
    public final static int mLeftFollow2 = 3;
    public final static int mRightMaster = 4;
    public final static int mRightMfollow1 = 5;
    public final static int mRightMfollow2 = 6;
    //Intake motor
    //Climb motor

    //Shooter motor
    public final static int mLeftShooter = 7;
    public final static int mRightShooter = 8;
    
    //Pivot Motor
    public final static int mLeftPivot = 11;
    public final static int mRightPivot = 12;

    //Pivot PID and feedForward control
    public final static double Kp = 0.00018;
    public final static double Ki = 0.000025;
    public final static double Kd = 0.00001;
    
    public final static double Ks = 0.0;
    public final static double Kcos = 0.0;
    public final static double Kv = 0.0;
    

    //camera
    public final static int cWidth = 640;
    public final static int cHeight = 480;
    // values for grippipelines
    public static double[] hsvThresholdHue = {40, 98};
    public static double[] hsvThresholdSaturation = {99, 220};
    public static double[] hsvThresholdValue = {140, 255};
    //view angles
    public static double horizontalViewAngle = 61;
    public static double verticalViewAngle = 20.55;
    
    
}