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
    public boolean winchback = false;


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
        //Operater control
        if(isManualControl()){
            pivot.SetPivotSpeed(backUp_Control.getRawAxis(1));
            shooter.setShooterSpeed(backUp_Control.getRawAxis(1), backUp_Control.getRawAxis(5));
        }

        mShooter.setOffset(operator_Control.getRawAxis(1), operator_Control.getRawAxis(1));
        
        if(isPivotFront()){
            pivot.setpivotDirection(true);
        }
        else{
            pivot.setpivotDirection(false);
        }
        
        //pivot and intake stuff
        if(isIntakeOut()){
            mShooter.intakeDisable();
            intake.setPosition(0);
        }
        
        if(isShortPreset()){
            pivot.setPTargetAngle(Constants.shortPreset[1]);
        }
        else if(isLongPreset()){
            pivot.setPTargetAngle(Constants.longPreset[1]);
        }
        if(targetPivotAlign()){
            pivot.isCameraTrackingEnabled(true);
        }
        if(isFeederStation()){
            mShooter.pivotToPosition(Constants.feederPreset[1]);
        }
        else if(isIntakeIn() || setPivotTrenchHeight()){
            mShooter.intakeEnableOp();
            intake.setPosition(1);
        }
        //index and shooter and intake stuff
        if(isIndexOut()){
            shooter.setShooterSpeed(0.2, 0.2);
        }
        else if(intakeInEnabled() && isFeederStation()){
            mShooter.intakeFeederEnableDr();
        }
        else if(intakeInEnabled()){
            mShooter.intakeEnableDr();
        }
        else if(intakeOutEnabled()){
            intake.setSpeed(-1);
        }
        else if(shootOutEnable() && isShortPreset()){
            mShooter.shootAlways(Constants.shortPreset[0]);
        }
        else if(shootOutEnable() && isLongPreset()){
            mShooter.shootAlways(Constants.longPreset[0]);
        }
        //climbing Stuff
        if(isClimbExtended())  //Extend the climber
         climb.setPosition(0);
        else if(isClimbRetracted())  //Retract the climber
            climb.setPosition(1);
        if(isWinching()){  //winch on
            winchback = false;
        }


        //driver control
        //Winch
        
        if(winchback){
            if(isBackWinching()){
                climb.setSpeed(0.2);
            }
            else{
                climb.setSpeed(0);
            }
        }
        else{
            winchback = false;
        }
        if(!winchback){
            if(isWinching()){  //winch on
                climb.setSpeed(-0.2);
        }
    }
        else{
            climb.setSpeed(0);  //winch off
            winchback = true;
        }
        if(targetAlignDrive()){
            drive.adjustToTarget();
        }
        else{
            drive.setSpeed(driver_Control.getRawAxis(1), driver_Control.getRawAxis(5));
        }
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
    private boolean intakeInEnabled(){
        return driver_Control.getRawButton(1);
    }
    
    /** 
     * @return boolean
     */
    private boolean intakeOutEnabled(){
        return driver_Control.getRawButton(2);
    }

    private boolean intakeDisable(){
        return driver_Control.getRawButton(17);
    }
    
    /** 
     * @return boolean
     */
    private boolean setPivotTrenchHeight(){
        return driver_Control.getRawButton(3);
    }
    
    /** 
     * @return boolean
     */
    private boolean shootOutEnable(){
        return driver_Control.getRawButton(4);
    }
    
    /** 
     * @return boolean
     */
    private boolean targetAlignDrive(){
        return driver_Control.getRawButton(5);
    }
    
    /** 
     * @return boolean
     */
    private boolean targetPivotAlign(){
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
    private boolean isClimbExtended(){
        return operator_Control.getRawButton(3);
    }
    
    /** 
     * @return boolean
     */
    //is climb retracted
    private boolean isClimbRetracted(){
        return operator_Control.getRawButton(2);
    }
    
    /** 
     * @return boolean
     */
    //is winch winching
    private boolean isWinching(){
        return operator_Control.getRawButton(4);
    }
    private boolean isBackWinching(){
        return driver_Control.getRawButton(11) && driver_Control.getRawButton(11);
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
    private boolean isCameraTracking(){
        return operator_Control.getRawButton(6);
    }
    
    /** 
     * @return boolean
     */
    //is the short preset pressed
    private boolean isShortPreset(){
        return operator_Control.getRawButton(7);
    }
    
    /** 
     * @return boolean
     */
    //is the long pressed
    private boolean isLongPreset(){
        return operator_Control.getRawButton(8);
    }
    
    /** 
     * @return boolean
     */
    //is the Intake in
    private boolean isIntakeOut(){
        return operator_Control.getRawButton(9);
    }
    
    /** 
     * @return boolean
     */
    //is the Intake out
    private boolean isIntakeIn(){
        return operator_Control.getRawButton(10);
    }
    private boolean isFeederStation(){
        return (!isIntakeIn() && (!isIndexOut()));
    }
    
    /** 
     * @return boolean
     */
    //Is the shoot pressed
    private boolean isShoot(){
        return operator_Control.getRawButton(11);
    }
    
    /** 
     * @return boolean
     */
    //is the indexer indexing out
    private boolean isIndexOut(){
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