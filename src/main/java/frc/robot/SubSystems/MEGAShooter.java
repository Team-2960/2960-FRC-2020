/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.SubSystems.*;
import frc.robot.Constants;
import frc.robot.Camera.Camera;
import edu.wpi.first.wpilibj.Timer;



public class MEGAShooter extends SubsystemBase {
  private static MEGAShooter megaShooter;
  private Intake intake;
  private Shooter shooter;
  private Pivot pivot;
  private Index index;
  private Camera camera;
  private boolean shoot = false;
  public double speed = -6000;
  public Timer timer = new Timer();

  /** 
   * @return megaShooter
   */
  public static MEGAShooter get_Instance(){
    if(megaShooter == null){
      megaShooter = new MEGAShooter();
    } 
    return megaShooter;
  }

/**
 * MEGAShooter costructor
 */
  private MEGAShooter() {
    camera = Camera.get_Instance();
    intake = Intake.get_Instance();
    shooter = Shooter.get_Instance();
    pivot = Pivot.get_Instance();
    index = Index.get_Instance();
  

  }
  
  /** 
   * @param angle
   * @param speed
   */
  public void setOffset(double angle, double speed){
    shooter.setSpeedOffset(speed);
    pivot.pivotAngleOffset(angle);
  }
  public void disableManual(){
    shooter.setSpeedOffset(0);
    pivot.pivotAngleOffset(0);
  }
  public void outakeEnable(){
    intake.setSpeed(-1);
    index.enableIndex(-1);  
    shootAlways(-6000);
  }
  //Driver Controls for the intake and shooter speeds
  public void intakeEnableDr(){
    index.enableIndex(1);
    intake.setSpeed(1);
    shooter.setPIDShooterSpeed(4000);
  }
  public void intakeOutEnableDr(){
    intake.setSpeed(-1);
  }
  //Operator control on the position for the intake and pivot positions
  public void intakeEnableOp(){
    boolean pivotOutOfIntake = false;
    if(Constants.pivotOutOfReach < pivot.getPivotPos()){    
      intake.setPosition(0);
      pivotOutOfIntake = true;
    }
    else{
      timer.start();
      if(timer.get() > 1){
        pivot.setPTargetAngle(pivot.frontOrBack());
        pivotOutOfIntake = true;
      }
      
    }
    if(pivotOutOfIntake){
      pivot.setPTargetAngle(Constants.intakePivotAngle);      
    }
  }
  //Operator Outake with pivot and index and shooter
  public void outakeEnableOp(){
    boolean pivotOutOfIntake = false;
    if(Constants.pivotOutOfReach < pivot.getPivotPos()){    
      intake.setPosition(1);
      pivotOutOfIntake = true;
    }
    else{
      timer.start();
      if(timer.get() > 1){
        pivot.setPTargetAngle(pivot.frontOrBack());
        pivotOutOfIntake = true;
      }
      
    }
    if(pivotOutOfIntake){
      intake.setPosition(1);
      index.setSpeed(-1, -0.85);
      shooter.setShooterSpeed(-0.2, -0.2);
    }
  }
  public void intakeDisable(){
    intake.setSpeed(0);
    shooter.setShooterSpeed(0, 0);
    if(Constants.pivotOutOfReach > pivot.getPivotPos()){
      intake.setPosition(1);
    }
    pivot.setPTargetAngle(pivot.frontOrBack());
    index.disableIndex();
  }
  public void shoot(){
    shooter.gotoRate(Constants.pivotTable[2][pivot.pivotTablePos]);
    if(shooter.readyToShoot()){
      index.enableIndex(-1);
    }
  }
  public void shootAlways(double rate){
    shooter.gotoRate(rate);
    if(shooter.readyToShoot()){
      shoot = true;
    }
    else{
      shoot = false;
    }
    if(shoot){
      index.enableIndex(-1);
    /*    if(index.lostBalls){
        shoot = false;*/
      } 
    }
    else{
      index.disableIndex();
    }

  }
  public void fullSpeedOutake(){
    shooter.gotoRate(9000);
    if(shooter.isAboveThreshold()){
      index.setSpeed(-1, -1);
    }
    else{
      if(!index.getPhotoeyeIndex()){
        index.setSpeed(-0.8, -0.8);
      }
      else{
        index.setSpeed(0, 0);
      }
    }
  } 
  public void alwaysOnShoot(){
    shooter.gotoRate(9000);
    if(shooter.readyToShoot()){
      index.setSpeed(-1, -1);
    }
  }
  public void disableShoot(){
    index.disableIndex();
    shooter.gotoRate(0);
  }
  public void SmartDashBoard(){
    speed = SmartDashboard.getNumber("Speed", speed);
    SmartDashboard.putNumber("Speed", speed);
    
  }
  
  @Override
  public void periodic() {
    SmartDashBoard();
    // This method will be called once per scheduler run

  }
}
