package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Camera.Camera;
import frc.robot.OI;

public class Robot extends TimedRobot {
 
  private Camera camera;
  private Joystick driver_Control;
  private Joystick operator_Control;
  private OI oi;
  @Override
  public void robotInit() {
  }
  
  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    oi = new OI();
    //init Driver and Oerator Joystick
    driver_Control = new Joystick(0);
    operator_Control = new Joystick(1);

    camera = new Camera(0);
  }

  @Override
  public void teleopPeriodic() {
    oi.driver_Control(driver_Control);
    oi.operator_Control(operator_Control);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
