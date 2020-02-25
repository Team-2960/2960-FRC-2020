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
        if(isManualControl()){
            mShooter.setOffset(operator_Control.getRawAxis(1), operator_Control.getRawAxis(0));
        }
        else{
            mShooter.disableManual();
        }
        //Extend the climber
        if(isClimbExtended()){
            climb.setPosition(0);
        }
        //Retract the climber
        else if(isClimbRetracted()){
            climb.setPosition(1);
        }
        //Set climber off
        else{
            climb.setPosition(2960);
        }
        //winch on
        if(isWinching()){
            climb.setSpeed(0.7);
        }
        //winch off
        else{
            climb.setSpeed(0);
        }

        if(isRobotFront()){
            pivot.isPivotFront = true;
        }
        else{
            pivot.isPivotFront = false;
        }

        if(isRobotFront()){
            pivot.setpivotDirection(true);
        }
        else{
            pivot.setpivotDirection(false);
        }
        if(isCameraTracking()){
            pivot.cameraTrackingEnabled = true;
        }
        else{
            pivot.cameraTrackingEnabled = false;
        }
        if(isLongPreset()){
            pivot.setPTargetAngle(Constants.longPreset [0]);
        }
        else{

        }
        
        
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

    // Driver Control Outline
    private boolean intakeInEnabled(){
        return driver_Control.getRawButton(1);
    }
    private boolean intakeOutEnabled(){
        return driver_Control.getRawButton(2);
    }
    private boolean setPivotTrenchHeight(){
        return driver_Control.getRawButton(3);
    }
    private boolean shootOutEnable(){
        return driver_Control.getRawButton(4);
    }
    private boolean targetAlignDrive(){
        return driver_Control.getRawButton(5);
    }
    private boolean targetPivotAlign(){
        return driver_Control.getRawButton(6);
    }

    // Operator Control Outline
    //is the robot in manual control
    private boolean isManualControl(){
        return operator_Control.getRawButton(1);
    }
    //is climb extended
    private boolean isClimbExtended(){
        return operator_Control.getRawButton(3);
    }
    //is climb retracted
    private boolean isClimbRetracted(){
        return operator_Control.getRawButton(2);
    }
    //is winch winching
    private boolean isWinching(){
        return operator_Control.getRawButton(4);
    }
    //is the robot facing front
    private boolean isRobotFront(){
        return operator_Control.getRawButton(5);
    }
    //is the pivot Camera tracking
    private boolean isCameraTracking(){
        return operator_Control.getRawButton(6);
    }
    //is the short preset pressed
    private boolean isShortPreset(){
        return operator_Control.getRawButton(7);
    }
    //is the long pressed
    private boolean isLongPreset(){
        return operator_Control.getRawButton(8);
    }
    //is the Intake in
    private boolean isIntakeOut(){
        return operator_Control.getRawButton(9);
    }
    //is the Intake out
    private boolean isIntakeIn(){
        return operator_Control.getRawButton(10);
    }
    //Is the shoot pressed
    private boolean isShoot(){
        return operator_Control.getRawButton(11);
    }
    //is the indexer indexing out
    private boolean isIndexOut(){
        return operator_Control.getRawButton(12);
    }
}