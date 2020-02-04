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
    public Shooter shooter;
    private Camera camera;
    private double cameraTurn;

    private Joystick driver_Control;
    private Joystick operator_Control;
    private Joystick joystick2;


    public OI(){
        camera = new Camera(0);
        //drive = Drive.get_Instance();
        System.out.println("init gyro");
        //climb = Climb.get_Instance();
        //intake = Intake.get_Instance();
        shooter = Shooter.get_Instance();
        //joysticks
        driver_Control = new Joystick(Constants.driver_Control);
        joystick2 = new Joystick(1);
        operator_Control = new Joystick(Constants.operator_Control);
    }
    //Driver control
    public void driver_Control(Joystick driver_Control, Joystick joystick2){
        /*         //drive.move(driver_Control.getRawAxis(1), driver_Control.getRawAxis(5));
                if(driver_Control.getRawButton(2)){
                    shooter.setShooterSpeed(driver_Control.getRawAxis(1));
                }else{
                    shooter.setShooterSpeed(0);
                }
                 */
                //driver_Control.setRumble(RumbleType.kLeftRumble, 1);
                //driver_Control.setRumble(RumbleType.kRightRumble, 1);
        /*         shooter.smart();
                if(driver_Control.getRawButton(2)){
                    shooter.setPIDShooterSpeed(1000);
                }
                else{
                    shooter.setPIDShooterSpeed(0);
                } */
                
                
                
        /* if(driver_Control.getRawButton(2)){
            drive.setDriveToAngle((camera.calcAngle(camera.getCenterX())), -0.2);
        } else if (joystick2.getRawButton(2)) {
            drive.setArcDriveRate(-200 * joystick2.getRawAxis(0), -1 * driver_Control.getRawAxis(1));
        } else if (driver_Control.getRawButton(1)) {
            drive.setDriveToAngle(90, 0);
        } else if (joystick2.getRawButton(1)){
            drive.setDriveRate(200);
        } else if (driver_Control.getRawButton(4)){
            drive.setDriveRate(0);
        } else if(driver_Control.getRawButton(3)){
            drive.navXReset();
        } else{
            drive.setSpeed((driver_Control.getRawAxis(1)), (joystick2.getRawAxis(1)));
        }  */
                /* if(driver_Control.getRawAxis(1) == 0 && driver_Control.getRawAxis(5) == 0){
                if(driver_Control.getRawButton(1)){
                    if(Math.abs(gyro.getAngle()) < 26.4243){
                        driver_Control.setRumble(RumbleType.kRightRumble, 1);
                        driver_Control.setRumble(RumbleType.kLeftRumble, 1);
                    }
                    else{
                            driver_Control.setRumble(RumbleType.kRightRumble, 0);
                            driver_Control.setRumble(RumbleType.kLeftRumble, 0);
                    }
                drive.move(-cameraTurn, cameraTurn);
                } */
        /*         else{
                    drive.move(0, 0);
                }
                } */
            
    }
    //Operator control
    public void operator_Control(Joystick operator_Control){
        if(operator_Control.getRawButton(2)){
            shooter.EnablePivotPID();
            shooter.setPTargetAngle(-40);
        }else if(operator_Control.getRawButton(1)){
            shooter.DisablePivotPID();
        }
        
        
        shooter.SetPivotSpeed(operator_Control.getRawAxis(1));

    }
    public void periodic(){
        if(DriverStation.getInstance().isOperatorControl()){
            operator_Control(operator_Control);
            driver_Control(driver_Control, joystick2);
        }
    }
}