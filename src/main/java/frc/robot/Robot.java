package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Camera.Camera;
import frc.robot.OI;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    driver_Control = new Joystick(Constants.driver_Control);
    operator_Control = new Joystick(Constants.operator_Control);

    camera = new Camera(0);
  }

  @Override
  public void teleopPeriodic() {
    oi.driver_Control(driver_Control);
    oi.operator_Control(operator_Control);
    SmartDashboard.putNumber("CenterX", camera.getImageResultsX());
    SmartDashboard.putNumber("CenterY", camera.getImageResultsY());
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
