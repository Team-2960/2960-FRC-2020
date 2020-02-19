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
        if(driver_Control_Right.getRawButton(1)){
            //Climb set position extend
            climb.setPosition(0);
        }
        else if(driver_Control_Right.getRawButton(2)){
            //climb set position retract
            climb.setPosition(1);
        }
        else{
            climb.setPosition(3);
        }
        if(driver_Control_Right.getRawButton(3)){
            //index speed in
            index.setSpeed(0.4);
        }
        else if(driver_Control_Right.getRawButton(4)){
            //index speed out
            index.setSpeed(-0.4);
        }
        else{
            index.setSpeed(0);
        }
        if(driver_Control_Right.getRawButton(5)){
            //intake speed in
            intake.setSpeed(0.4);
        }
        else if(driver_Control_Right.getRawButton(6)){
            //intake speed out
            intake.setSpeed(-0.4);
        }
        else{
            intake.setSpeed(0);
        }

        if(driver_Control_Right.getRawButton(7)){
            //pivot speed
            pivot.SetPivotSpeed(0.4);
        }
        else if(driver_Control_Right.getRawButton(8)){
            //pivot speed
            pivot.SetPivotSpeed(-0.4);
        }
        else{
            pivot.SetPivotSpeed(0);
        }
        if(driver_Control_Right.getRawButton(9)){
            //shooter speed
            shooter.setShooterSpeed(0.4);
        }
        else if(driver_Control_Right.getRawButton(10)){
            //shooter speed
            shooter.setShooterSpeed(-0.4);
        }
        else{
            shooter.setShooterSpeed(0);
        }
        if(driver_Control_Right.getRawButton(11)){
            //intake position up
            intake.setPosition(0);
        }
        else if(driver_Control_Right.getRawButton(12)){
            //intake position down
            intake.setPosition(1);
        }
        else{
            intake.setPosition(3);
        }
            // drive
            drive.setSpeed(driver_Control_Right.getRawAxis(1), driver_Control_Left.getRawAxis(1));            
    }   

    /**
     * Operator control
     * @param operator_Control operator control joystick
     */
    public void operator_Control(Joystick operator_Control){
        
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

}