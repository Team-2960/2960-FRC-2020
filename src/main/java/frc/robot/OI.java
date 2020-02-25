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
    private Joystick driver_Control_Right;
    private Joystick driver_Control_Left;
    private Joystick operator_Control;


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
        driver_Control_Left = new Joystick(Constants.driver_Control_Right);
        driver_Control_Right = new Joystick(Constants.driver_Control_Left);
        
        operator_Control = new Joystick(Constants.operator_Control);
    }
    /**
     * Commands for the joysticks
     * @param driver_Control joystick 1
     * @param joystick2 joystick 2
     */
    public void driver_Control(Joystick driver_Control_Right, Joystick driver_Control_Left){ 
       

        if(/* button */)
            mShooter.intakeEnable();
        else if(/* button */)
            mShooter.intakeDisable();
        else

        if(/* button */)
            pivot.setPTargetAngle(/* tranch */);
        if(/* button */)
            pivot.setPTargetAngle(/* intake */);
        if(/* button */)
            mShooter.shoot();
        if(/* button */)
            drive.targetLineUp();
        if(/* button */)
            pivot.cameraTrackingEnabled = true;
        else{
            drive.setSpeed(/* joystick */, /* joystick */);
            pivot.cameraTrackingEnabled = false; 
        }
            
    }   

    /**
     * Operator control
     * @param operator_Control operator control joystick
     */
    public void operator_Control(Joystick operator_Control){
        //preset
        //Set Manual Control Offset
        if(isManualControl())
            mShooter.setOffset(operator_Control.getRawAxis(1), operator_Control.getRawAxis(0));
        else
            mShooter.disableManual();

        //piston for climber
        if(isClimbExtended())  //Extend the climber
            climb.setPosition(0);
        else if(isClimbRetracted())  //Retract the climber
            climb.setPosition(1);
        else
            climb.setPosition(2960);  //Set climber off
       
        //Winch
        if(isWinching())  //winch on
            climb.setSpeed(1);
        else
            climb.setSpeed(0);  //winch off
        
        //Is pivot front
        if(isPivotFront()){
            pivot.setpivotDirection(true);
        }
        else{
            pivot.setpivotDirection(false);
        }

        //Is camera tracking enabled
        if(isCameraTracking())
            pivot.isCameraTrackingEnabled(true);
        else 
            pivot.isCameraTrackingEnabled(false);

        //Shooting presets
        if(isLongPreset())
            pivot.setPTargetAngle(Constants.longPreset[0]);  //Long
        else if (isShortPreset()){
            pivot.setPTargetAngle(Constants.shortPreset[0]);  //Short
        }
        
        //Intake
        if (isIntakeOut())
            intake.setSpeed(-1);  //Intake out
        else if (isIntakeIn())
            intake.setSpeed(1);  //Intake in
        else 
            intake.setSpeed(0);  //Intake stop

        //Shoot
        if (isShoot())
            mShooter.shoot();
        else
            shooter.setShooterSpeed(0, 0);

        //Index
        if (isIndexOut())
            index.enableIndex(-1); 
        else
            index.disableIndex();


        
        
        //pivot.SetPivotSpeed(operator_Control.getRawAxis(1));

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
            operator_Control(operator_Control);
            driver_Control(driver_Control_Left, driver_Control_Right);
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