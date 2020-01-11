/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Camera.Camera;
import frc.robot.OI;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
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
