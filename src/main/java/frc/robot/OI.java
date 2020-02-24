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
        //intake pos climb pos
        
        
        if(operator_Control.getRawButton(4)){
            pivot.setpivotDirection(true);
        }
        else{
            pivot.setpivotDirection(true);
        }
        if(/* button */){
            pivot.cameraTrackingEnabled = true;
        }else{
            pivot.cameraTrackingEnabled = false;
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

}