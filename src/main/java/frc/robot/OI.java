package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.SubSystems.*;
public class OI{
    private Drive drive;
    public OI(){
        drive = Drive.get_Instance();
    }
    //Driver control
    public void driver_Control(Joystick driver_Control){
        drive.move(driver_Control.getRawAxis(1), driver_Control.getRawAxis(5));

    }
    //Operator control
    public void operator_Control(Joystick operator_Control){

    }
}