package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Camera.Camera;
import frc.robot.OI;
import frc.robot.Constants;
import frc.robot.SubSystems.*;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
 
  private Joystick driver_Control;
  private Joystick operator_Control;
  private Joystick joystick2;
  private OI oi;
  private LEDs leds;
  @Override
  public void robotInit() {
    oi = new OI();
    leds = new LEDs();
    //init Driver and Oerator Joystick
    driver_Control = new Joystick(Constants.driver_Control);
    joystick2 = new Joystick(1);
    operator_Control = new Joystick(Constants.operator_Control);
  }
  
  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    
    

  
  }

  @Override
  public void teleopPeriodic() {

    oi.update();
    oi.driver_Control(driver_Control, joystick2);
    oi.operator_Control(operator_Control);
    oi.smartDashboradUpdate();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
