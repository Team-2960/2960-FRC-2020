package frc.robot;
public class Constants{
    //Joystick
    public final static int driver_Control = 0;
    public final static int operator_Control = 1;
    //Drive train motor
    public final static int mLeftMaster = 1;
    public final static int mLeftSlave1 = 2;
    public final static int mLeftSlave2 = 3;
    public final static int mRightMaster = 4;
    public final static int mRightSlave1 = 5;
    public final static int mRightSlave2 = 6;
    //Shooter motor
    public final static int mLeftShooter = 7;//change to 7
    public final static int mRightShooter = 8;//change to 8
    //Intake motor
    public final static int mIntake = 9;//change to 9
    //Climb motor
    //camera
    public final static int cWidth = 480;
    public final static int cHeight = 640;
    //values for grippipelines
    public static double[] hsvThresholdHue = {97, 55};
    public static double[] hsvThresholdSaturation = {79, 23};
    public static double[] hsvThresholdValue = {255, 216};
    public static double filterContoursMinArea = 70;
    
    
    
}