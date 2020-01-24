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