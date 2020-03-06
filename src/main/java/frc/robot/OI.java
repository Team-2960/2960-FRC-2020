package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.SubSystems.*;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Camera.Camera;
public class OI extends SubsystemBase{
    private Climb climb;
    private Intake intake;
    private Drive drive;
    private Shooter shooter;
    public Pivot pivot;
    private Index index;
    private Camera camera;
    private MEGAShooter mShooter;
    //Joysticks
    private Joystick driver_Control;
    private Joystick driver_Control_Left;
    private Joystick operator_Control;
    private Joystick backUp_Control;


    public OI(){
        //Init Classes
        camera = Camera.get_Instance();
        drive = Drive.get_Instance();
        climb = Climb.get_Instance();
        intake = Intake.get_Instance();
        shooter = Shooter.get_Instance();
        pivot = Pivot.get_Instance();
        index = Index.get_Instance();
        mShooter = MEGAShooter.get_Instance();
        //joysticks
        driver_Control = new Joystick(Constants.driver_Control_Left);
        
        operator_Control = new Joystick(Constants.operator_Control);
        backUp_Control = new Joystick(Constants.backUp_Control);
    }
    /**
     * Commands for the joysticks
     * @param driver_Control joystick 1
     * @param joystick2 joystick 2
     */
    public void Controller(){ 
        if(driver_Control.getRawButton(4)){
            pivot.DisablePivotPID();
        }




        if(isManualControl()){ //this may change
            pivot.SetPivotSpeed(backUp_Control.getRawAxis(1));
        }
        else{

        }
        //Driver Control
        drive.setSpeed(driver_Control.getRawAxis(5), driver_Control.getRawAxis(1)); //drive

        if(dintakeIn()){ //in
            mShooter.intakeEnableDr();
        }else if(oIndexOut()){
            index.setSpeed(-1, -1);
        }else if(dintakeOut()){//may be shoooter
            intake.setSpeed(-1); //out
            shooter.setShooterSpeed(-0.2, -0.2); 
        }else if(oShortPreset() && dShootOut()){
            mShooter.dShortShoot();
        }
        else{
            intake.setSpeed(0);
            index.disableIndex();
            shooter.setShooterSpeed(0, 0);
        }/*else{
            mShooter.intakeDisable(); //why this bring the intake up/down 
            //isn't this should be stop intake speed?
        

        if(dShootOut()){
            mShooter.shoot();
        }

        //winch*/
        if(oWinching()) { //winch in
            climb.setSpeed(-0.5);
        }else if(dBackWinching()) { // winch out
            climb.setSpeed(0.2);
        }else{
            climb.setSpeed(0); //off
        }/*
        */
        if(dTrenchHeight() || oIntakeOut()){
            //sets intake down ready to intake
            mShooter.intakePosition();
        }
        else if(oShortPreset()){
            mShooter.ShortShoot();
        }else if(oIntakeIn()) {
            mShooter.toNeuturalPosition();
        }
        

        
        
        /*
        
    
        if (oIndexOut()) { 
            shooter.setShooterSpeed(-0.2, -0.2);
            index.setSpeed(-1, -1);
        }

        // todo back and front
        if (oShortPreset()) {
            pivot.setPTargetAngle(Constants.shortPreset[1]);
        } else if (oLongPreset()) {
            pivot.setPTargetAngle(Constants.longPreset[1]);
        }
        if(oShoot()){
            mShooter.shoot();
        }
        //pivot front or back
        if(isPivotFront()){
            pivot.setpivotDirection(true);
        }else{
            pivot.setpivotDirection(false);
        }
        //climbing Stuff
        if(oClimbExtended()) // Extend the climber
            climb.setPosition(0);
        else if(oClimbRetracted()) // Retract the climber
            climb.setPosition(1);
 */












        /* //Operater control
        if(isManualControl()){
            pivot.SetPivotSpeed(backUp_Control.getRawAxis(1));
            //shooter.setShooterSpeed(backUp_Control.getRawAxis(1), backUp_Control.getRawAxis(5));
        }

        //mShooter.setOffset(operator_Control.getRawAxis(1), operator_Control.getRawAxis(1));
        
        //dirction of pivot
        if(isPivotFront()){
            pivot.setpivotDirection(true);
        }else{
            pivot.setpivotDirection(false);
        }
        
        //pivot and intake stuff
        if(dintakeIn()) { // intake up
            mShooter.intakeDisable();
        }

        // todo back and front
        if (oShortPreset()) {
            pivot.setPTargetAngle(Constants.shortPreset[1]);
        } else if (oLongPreset()) {
            pivot.setPTargetAngle(Constants.longPreset[1]);
        }

        if (dPivotAlign()) {
            pivot.isCameraTrackingEnabled(true);
        }

        
         if(isFeederStation()){ mShooter.pivotToPosition(Constants.feederPreset[1]); }
         
        if (dintakeIn() || dTrenchHeight()) {
            mShooter.intakePosition();
            intake.setPosition(1);
        }
        // index and shooter and intake stuff
        if (oIndexOut()) {
            shooter.setShooterSpeed(0.2, 0.2);
            index.setSpeed(-1, -1);
        } else if ((dintakeIn()) && oFeederStation()) {
            mShooter.intakeFeederEnableDr();
        } else if (dintakeIn()) {
            mShooter.intakeEnableDr();
        } else if (dintakeOut()) {// may need to run the shooter
            intake.setSpeed(-1);
        } else if (dShootOut() && oShortPreset()) { //why driver need press shoot button
            mShooter.shootAlways(Constants.shortPreset[0]);
        } else if (dShootOut() && oLongPreset()) {  //why driver need press shoot button
            mShooter.shootAlways(Constants.longPreset[0]);
        }
        //climbing Stuff
        if(oClimbExtended()) // Extend the climber
            climb.setPosition(0);
        else if(oClimbRetracted()) // Retract the climber
            climb.setPosition(1);

        //driver control
        //Winch
        
        if(dShootOut()){

        }
       
        if(dAlignDrive()) {
            drive.adjustToTarget();
        }else{
            drive.setSpeed(driver_Control.getRawAxis(1), driver_Control.getRawAxis(5));
        }

        if(dintakeIn()) {
            mShooter.intakeEnableDr();
        } 
        */
    }
    
    /**
     * put to smartdashboard
     */
    public void SmartDashboard(){
        SmartDashboard.putNumber("calc angle", (camera.calcAngle(camera.getCenterX()) + drive.getAngle()));
        SmartDashboard.putNumber("togo angle", (camera.calcAngle(camera.getCenterX())));
        SmartDashboard.putNumber("", (camera.calcAngle(camera.getCenterX())));

    }
    /**
     * Run every time
     */
    public void periodic(){
        if(DriverStation.getInstance().isOperatorControl()){
            Controller();
        }
    }

    











    /** 
     * @return boolean
     */
    // Driver Control Outline
    private boolean dintakeIn(){
        return driver_Control.getRawAxis(3) > 0.1;
    }
    
    /** 
     * @return boolean
     */
    private boolean dintakeOut(){
        return driver_Control.getRawAxis(2) > 0.1;
    }

    private boolean intakeDisable(){
        return driver_Control.getRawButton(17);
    }
    
    /** 
     * @return boolean
     */
    private boolean dTrenchHeight(){
        return driver_Control.getRawButton(5) && driver_Control.getRawButton(6);
    }
    
    /** 
     * @return boolean
     */
    private boolean dShootOut(){
        return driver_Control.getRawButton(1);
    }
    
    /** 
     * @return boolean
     */
    private boolean dAlignDrive(){
        return driver_Control.getRawButton(5);
    }
    
    /** 
     * @return boolean
     */
    private boolean dPivotAlign(){
        return driver_Control.getRawButton(6);
    }

    
    /** 
     * @return boolean
     */
    // Operator Control Outline
    //is the robot in manual control
    private boolean isManualControl(){
        return operator_Control.getRawButton(1);
    }
    
    /** 
     * @return boolean
     */
    //is climb extended
    private boolean oClimbExtended(){
        return operator_Control.getRawButton(3);
    }
    
    /** 
     * @return boolean
     */
    //is climb retracted
    private boolean oClimbRetracted(){
        return operator_Control.getRawButton(2);
    }
    
    /** 
     * @return boolean
     */
    //is winch winching
    private boolean oWinching(){
        return operator_Control.getRawButton(4);
    }
    private boolean dBackWinching(){
        return driver_Control.getRawButton(7) && driver_Control.getRawButton(8);
    }
    
    /** 
     * @return boolean
     */
    //is the robot facing front
    private boolean isPivotFront(){
        return operator_Control.getRawButton(5);
    }
    
    /** 
     * @return boolean
     */
    //is the pivot Camera tracking
    private boolean dCameraTracking(){
        return operator_Control.getRawButton(6);
    }
    
    /** 
     * @return boolean
     */
    //is the short preset pressed
    private boolean oShortPreset(){
        return operator_Control.getRawButton(7);
    }
    
    /** 
     * @return boolean
     */
    //is the long pressed
    private boolean oLongPreset(){
        return operator_Control.getRawButton(8);
    }
    
    /** 
     * @return boolean
     */
    //is the Intake in
    private boolean oIntakeOut(){
        return operator_Control.getRawButton(9);
    }
    
    /** 
     * @return boolean
     */
    //is the Intake out
    private boolean oIntakeIn(){
        return operator_Control.getRawButton(10);
    }
    private boolean oFeederStation(){
        return (!oIntakeIn() && (!oIndexOut()));
    }
    
    /** 
     * @return boolean
     */
    //Is the shoot pressed
    private boolean oShoot(){
        return operator_Control.getRawButton(11);
    }
    
    /** 
     * @return boolean
     */
    //is the indexer indexing out
    private boolean oIndexOut(){
        return operator_Control.getRawButton(12);
    }
}


















/* 
Driver:
    drive
    intake: in / out (balls)
    shoot



Operator:
    intake out:
        -intake down
        -pivot too there(need wait)

    intake in:
        -




















*/