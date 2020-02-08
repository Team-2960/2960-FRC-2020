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
    //Joysticks
    private Joystick driver_Control;
    private Joystick operator_Control;
    private Joystick joystick2;


    public OI(){
        //Init Classes
        camera = new Camera(0);
        //drive = Drive.get_Instance();
        //climb = Climb.get_Instance();
        //intake = Intake.get_Instance();
        //shooter = Shooter.get_Instance();
        //pivot = Pivot.get_Instance();
        //index = Index.get_Instance();

        //joysticks
        driver_Control = new Joystick(Constants.driver_Control);
        joystick2 = new Joystick(1);
        operator_Control = new Joystick(Constants.operator_Control);
    }
    //Driver control
    public void driver_Control(Joystick driver_Control, Joystick joystick2){
                if(driver_Control.getRawButton(1)){
                    //Drive forawrd speed, angle and targetDistance
                    drive.giveNums(0, 90, 0);
                    //enable function
                    drive.enableDrivePIDF();
                    //The method of checking 1 = distance, 2 = anlge, 3 = both
                    drive.PIDCheck = 2;
                }    
    /*             else if(driver_Control.getRawButton(2)){
                    drive.setDriveToAngle((camera.calcAngle(camera.getCenterX()) +  drive.navXAngle()), joystick2.getRawAxis(1));
                }
                else if(driver_Control.getRawButton(3)){
                    drive.setDriveToAngle(0, 0);
                } 
                else if(driver_Control.getRawButton(4)){
                    drive.setDrive(-0.5, 0, 75);
                }
                else if(driver_Control.getRawButton(5)){
                    drive.setDriveToAngle((camera.calcAngle(camera.getCenterX()) +  drive.navXAngle()), joystick2.getRawAxis(1));
                }*/
                else if(joystick2.getRawButton(2)){
                    drive.encoderReset();
                }
                else if(joystick2.getRawButton(1)){
                    drive.navXReset();
                }
                else{
                    drive.setSpeed((driver_Control.getRawAxis(1)), (joystick2.getRawAxis(1)));
                }          
    }
    //Operator control
    public void operator_Control(Joystick operator_Control){
        /* if(operator_Control.getRawButton(2)){
            pivot.EnablePivotPID();
            pivot.setPTargetAngle(-40);
        }else if(operator_Control.getRawButton(1)){
            pivot.DisablePivotPID();
        } */
        
        
        //pivot.SetPivotSpeed(operator_Control.getRawAxis(1));

    }
    public void SmartDashboard(){
        SmartDashboard.putNumber("calc angle", (camera.calcAngle(camera.getCenterX()) + drive.navXAngle()));
        SmartDashboard.putNumber("togo angle", (camera.calcAngle(camera.getCenterX())));
    }
    public void periodic(){
        if(DriverStation.getInstance().isOperatorControl()){
            operator_Control(operator_Control);
            driver_Control(driver_Control, joystick2);
        }
    }
}