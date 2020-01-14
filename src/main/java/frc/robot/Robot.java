package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Camera.Camera;
import frc.robot.OI;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LEDs;

public class Robot extends TimedRobot {
 
  private Camera camera;
  private Joystick driver_Control;
  private Joystick operator_Control;
  private OI oi;
  private LEDs leds;
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
    //oi = new OI();
    leds = new LEDs();
    //init Driver and Oerator Joystick
    driver_Control = new Joystick(Constants.driver_Control);
    operator_Control = new Joystick(Constants.operator_Control);
    //SmartDashboard.putNumber("CenterX", camera.getImageResultsX());
    //SmartDashboard.putNumber("CenterY", camera.getImageResultsY());
    SmartDashboard.putNumber("Constants.hsvThresholdHueMin", Constants.hsvThresholdHue[1]);
    SmartDashboard.putNumber("Constants.hsvThresholdHueMax", Constants.hsvThresholdHue[0]);
    SmartDashboard.putNumber("Constants.hsvThresholdSaturationMin", Constants.hsvThresholdSaturation[1]);
    SmartDashboard.putNumber("Constants.hsvThresholdSaturationMax", Constants.hsvThresholdSaturation[0]);
    SmartDashboard.putNumber("Constants.hsvThresholdValueMin", Constants.hsvThresholdValue[1]);
    SmartDashboard.putNumber("Constants.hsvThresholdValueMax", Constants.hsvThresholdValue[0]);
    

    camera = new Camera(0);
  }

  @Override
  public void teleopPeriodic() {
    //oi.driver_Control(driver_Control);
    //oi.operator_Control(operator_Control);
    cameraSetting();
  }
  public void cameraSetting(){
    Constants.hsvThresholdHue[0] = SmartDashboard.getNumber("HueMax", Constants.hsvThresholdHue[0]);
    Constants.hsvThresholdHue[1] = SmartDashboard.getNumber("HueMin", Constants.hsvThresholdHue[1]);
    Constants.hsvThresholdSaturation[0] = SmartDashboard.getNumber("SaturationMax", Constants.hsvThresholdSaturation[0]);
    Constants.hsvThresholdSaturation[1] = SmartDashboard.getNumber("SaturationMin", Constants.hsvThresholdSaturation[1]);
    Constants.hsvThresholdValue[0] = SmartDashboard.getNumber("ValueMax", Constants.hsvThresholdValue[0]);
    Constants.hsvThresholdValue[1] = SmartDashboard.getNumber("ValueMin", Constants.hsvThresholdValue[1]);
  }
  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
