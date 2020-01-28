package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.SubSystems.*;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Camera.Camera;
public class OI{
    //private Drive drive;
    private Climb climb;
    private Intake intake;
    
    private Shooter shooter;
    private Camera camera;
    private double cameraTurn;
    public OI(){
        camera = new Camera(0);
        //drive = Drive.get_Instance();
        System.out.println("init gyro");
        //climb = Climb.get_Instance();
        //intake = Intake.get_Instance();
        shooter = Shooter.get_Instance();
        SmartDashboard.putNumber("CenterX", camera.getImageResultsX());
        SmartDashboard.putNumber("CenterY", camera.getImageResultsY());
        SmartDashboard.putNumber("TargetRatio", camera.getImageResultsTargetRatio());
        SmartDashboard.putNumber("Constants.hsvThresholdHueMin", Constants.hsvThresholdHue[0]);
        SmartDashboard.putNumber("Constants.hsvThresholdHueMax", Constants.hsvThresholdHue[1]);
        SmartDashboard.putNumber("Constants.hsvThresholdSaturationMin", Constants.hsvThresholdSaturation[0]);
        SmartDashboard.putNumber("Constants.hsvThresholdSaturationMax", Constants.hsvThresholdSaturation[1]);
        SmartDashboard.putNumber("Constants.hsvThresholdValueMin", Constants.hsvThresholdValue[0]);
        SmartDashboard.putNumber("Constants.hsvThresholdValueMax", Constants.hsvThresholdValue[1]);
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
           drive.setDrivePID(20);
        }
        else if(joystick2.getRawButton(2)){
            drive.setDriveArcPID(-200 * joystick2.getRawAxis(0), -1 * driver_Control.getRawAxis(1));
        }
        else{
            drive.move((driver_Control.getRawAxis(1)), (joystick2.getRawAxis(1)));
        } */
        /*if(driver_Control.getRawAxis(1) == 0 && driver_Control.getRawAxis(5) == 0){
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
        }
        else{
            drive.move(0, 0);
        }
        }*/
    
    }
    //Operator control
    public void operator_Control(Joystick operator_Control){
        if(operator_Control.getRawButton(2)){
            shooter.EnablePivotPID();
        }else if(operator_Control.getRawButton(1)){
            shooter.DisablePivotPID();
        }
        
        shooter.setPTargetAngle(-40);

    }
    public void smartDashboradUpdate(){
        SmartDashboard.putNumber("CenterX", camera.getImageResultsX());
        SmartDashboard.putNumber("CenterY", camera.getImageResultsY());
        SmartDashboard.putNumber("TargetRatio", camera.getImageResultsTargetRatio());

        SmartDashboard.putNumber("Angle", camera.horizontalDistanceCalc(camera.getImageResultsX()));
        SmartDashboard.putNumber("Width", camera.getImageResultsWidth());
        SmartDashboard.putNumber("Height", camera.getImageResultsHeight());
        Constants.hsvThresholdHue[0] = SmartDashboard.getNumber("Constants.hsvThresholdHueMin", Constants.hsvThresholdHue[0]);
        Constants.hsvThresholdHue[1] = SmartDashboard.getNumber("Constants.hsvThresholdHueMax", Constants.hsvThresholdHue[1]);
        Constants.hsvThresholdSaturation[0] = SmartDashboard.getNumber("Constants.hsvThresholdSaturationMin", Constants.hsvThresholdSaturation[0]);
        Constants.hsvThresholdSaturation[1] = SmartDashboard.getNumber("Constants.hsvThresholdSaturationMax", Constants.hsvThresholdSaturation[1]);
        Constants.hsvThresholdValue[0] = SmartDashboard.getNumber("Constants.hsvThresholdValueMin", Constants.hsvThresholdValue[0]);
        Constants.hsvThresholdValue[1] = SmartDashboard.getNumber("Constants.hsvThresholdValueMax", Constants.hsvThresholdValue[1]);
        SmartDashboard.putNumber("Distance", camera.distanceCalc(camera.getImageResultsHeight()));
    }
    public void update(){
       // drive.periodic();
        //climb.periodic();
        shooter.periodic();
        //intake.periodic();
    }
}