package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.SubSystems.*;
public class OI{
    private Drive drive;
    private Climb climb;
    private Intake intake;
    private Shooter shooter;
    public OI(){
        //drive = Drive.get_Instance();
        climb = Climb.get_Instance();
        //intake = Intake.get_Instance();
        shooter = Shooter.get_Instance();
    }
    //Driver control
    public void driver_Control(Joystick driver_Control){
        //drive.move(driver_Control.getRawAxis(1), driver_Control.getRawAxis(5));
        shooter.setShooterSpeed(driver_Control.getRawAxis(1));
        //shooter.shooterEncoder();
        //intake.setIntakeSpeed(driver_Control.getRawAxis(1));
    }
    //Operator control
    public void operator_Control(Joystick operator_Control){

    }
}