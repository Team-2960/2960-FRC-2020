/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Camera.Camera;
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
  private Joystick joy;
  private TalonSRX leftMotor;
  private TalonSRX rightMotor;
  private double joyright = 0;
  private double joyleft = 0;
  @Override
  public void robotInit() {
    joy = new Joystick(0);
    leftMotor = new TalonSRX(0);
    rightMotor = new TalonSRX(1);
  }
  public void move(double right, double left){
    leftMotor.set(ControlMode.PercentOutput, 1 * left);
    rightMotor.set(ControlMode.PercentOutput, -1 * right);
  }
  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    camera = new Camera(0);
  }

  @Override
  public void teleopPeriodic() {
    joyleft = joy.getRawAxis(1);
    joyright = joy.getRawAxis(5);
    move(joyleft, -joyleft);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
