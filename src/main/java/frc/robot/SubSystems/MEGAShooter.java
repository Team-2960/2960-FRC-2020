package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.SubSystems.*;
import frc.robot.Constants;
import frc.robot.Camera.Camera;



public class MEGAShooter extends SubsystemBase {
  private static MEGAShooter megaShooter;
  private Intake intake;
  private Shooter shooter;
  private Pivot pivot;
  private Index index;
  private Camera camera;
  private boolean shoot = false;
  public double speed = -6000;
  public boolean setMotorToIntake = false;
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
    //camera = Camera.get_Instance();
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
    speed = (speed - 0.50) * 2;
    angle = (angle - 0.50) * 2;
    shooter.setSpeedOffset(speed);
/*     pivot.pivotAngleOffset(angle);
 */  }
  public void disableManual(){
    shooter.setSpeedOffset(0);
    /* pivot.pivotAngleOffset(0); */
  }
  public void outakeEnable(){
    intake.setSpeed(-1);
    index.enableIndex(-1);  
    shootAlways(-6000);
  }
  //Driver Controls for the intake and shooter speeds
  public void intakeEnableDr(){
    //intake in
    index.enableIndex(1);
    intake.setSpeed(1);
    shooter.gotoRate(4000);
  }

  public void intakeFeederEnableDr(){
    index.enableIndex(1);
    shooter.gotoRate(Constants.feederPreset[0]);
  }

  //new function to review
  public boolean pivotToBumper(){
    if(pivot.getPivotPos() < 280 - Constants.angleTolerance){
      pivot.setPTargetAngle(280);     // TODO: Move PID stop position to constants
      return false;
    }else if(pivot.getPivotPos() < 319 - Constants.angleTolerance){ // TODO: Move hardstop position to constants
      pivot.DisablePivotPID();
      pivot.SetPivotSpeed(0.2);
      return false;
    }else{
      pivot.DisablePivotPID();
      pivot.SetPivotSpeed(0);
      return true;
    }
  }


  public void intakeOutEnableDr(){
    intake.setSpeed(-1);
  }
  //Operator control on the position for the intake and pivot positions
  public void intakePosition(){
    if(intake.isIntakeOut()){
      if(intake.getTime() > 0.5){   // TODO: Move Intake Delay to constants        
        if(pivotToBumper()){ //new edit review
          pivot.DisablePivotPID();
        }
      }
    }else{
      intake.setPosition(1);
    }
  }
  //sets to neutural position
  public void intakeUp(){
    if(pivot.pivotTarget() > 260){
      toNeuturalPosition();
    }
    if(pivot.getPivotPos() < Constants.pivotOutOfReach){
      intake.setPosition(0);
    }
  }
  public void pivotToPosition(double position){
    if(Constants.pivotOutOfReach > position){
    intake.setSpeed(0);
    shooter.setShooterSpeed(0, 0);
    if(Constants.pivotOutOfReach > pivot.getPivotPos()){
      intake.setPosition(1);
    }
    pivot.setPTargetAngle(position);
    index.disableIndex();
  }
  }
  public void intakeDisable(){
    intake.setSpeed(0);
    //shooter.setShooterSpeed(0, 0);
    if(Constants.pivotOutOfReach > pivot.getPivotPos()){
      intake.setPosition(1);
    }
    pivot.setPTargetAngle(pivot.frontOrBack());
    //index.disableIndex();
  }
  public void shoot(){
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

  public void ShortShoot(){
    pivot.setPTargetAngle(Constants.shortPreset[1]);
    shooter.gotoRate(Constants.shortPreset[0]);
    if(!pivot.atPivotTarget() && !shooter.readyToShoot()){
      index.setSpeed(0, 0);
    }
  }
  /* public void wheelOfFortunePreset(){
    pivot.setPTargetAngle(Constants.wheelOfFortunePreset[1]);
  } */
  public void longShoot(){
    pivot.setPTargetAngle(Constants.autonPreset[1]);
    shooter.gotoRate(Constants.autonPreset[0]);
    if(!pivot.atPivotTarget() && !shooter.readyToShoot()){
      index.setSpeed(0, 0);
    }
  }
  public void toNeuturalPosition(){
    pivot.setPTargetAngle(Constants.neuturalPosFront);
  }
  public void dShortShoot(){
    shootAlways(Constants.shortPreset[0]);
  }
  public void dLongShoot(){
    shootAlways(Constants.autonPreset[0]);
  }
  @Override
  public void periodic() {
    //Timer time = new Timer();
    //time.start();
    // This method will be called once per scheduler run
   // SmartDashBoard();
    //SmartDashboard.putNumber("megaTimer",  time.get());
  }
}