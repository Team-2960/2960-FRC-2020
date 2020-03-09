package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.SubSystems.*;
import frc.robot.Constants;
import frc.robot.Camera.Camera;



public class MEGAShooter extends SubsystemBase {
  private static MEGAShooter megaShooter;

  public enum ShooterMode{short_mode, long_mode, camera_mode, intake_mode, idle_mode, manual_mode};
  public enum IntakeStatus{intake_In, intake_Out, intake_Off};

  private ShooterMode shooterMode = ShooterMode.idle_mode;
  private IntakeStatus intakeStatus = IntakeStatus.intake_Off;



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
    camera = Camera.get_Instance();
    intake = Intake.get_Instance();
    shooter = Shooter.get_Instance();
    pivot = Pivot.get_Instance();
    index = Index.get_Instance();
  }
  
  /**
   * Sets the mode for the shooter to be in
   * @param shooterMode
   */
  public void setShooterMode(ShooterMode shooterMode){
    this.shooterMode = shooterMode;
  }

  /** 
   * allows for the offset for shooter and the pivot to be used
   * @param angle the value from the joystick that gets passed
   * @param speed the value from the joystick that gets passed 
   */
  public void setOffset(double angle, double speed){
    speed = (speed - 0.50) * 2;// makes the value between -1 and 1
    angle = (angle - 0.50) * 2;// makes the value between -1 and 1
    shooter.setSpeedOffset(speed);//sets the shooter offset 
/*     pivot.pivotAngleOffset(angle);//sets the pivto offset
 */  }




 
 /**
  * disables the manual control
  */
  public void disableManual(){
    shooter.setSpeedOffset(0);//sets the shooter speed to zero
    /* pivot.pivotAngleOffset(0); *///resets the pivot offset
  }



  /**
   * 
   */
  public void intakeEnableDr(){
    //intake in
    index.enableIndex(1);
    intake.setSpeed(1);
    shooter.gotoRate(4000);
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
   /**
   * uses the to bumper to tell the pivot when to move and when to not
   */
  public void intakePosition(){
    if(intake.isIntakeOut()){//uses the intake out function to tell when it is out
      if(intake.getTime() > 0.5){   // TODO: Move Intake Delay to constants        
        if(pivotToBumper()){ 
          pivot.DisablePivotPID();// if the pivot is down at the bumper then it stops the pvot PID
        }
      }
    }else{
      intake.setPosition(1);//if the pivot is not at the bumper then it is putting the intake down
    }
  }
  
  
  /**
   * if the intake is up then it will set it to neutural
   */
  public void intakeUp(){
    if(pivot.pivotTarget() > 260){//if the pivot is in the way then the pivot will go to the neutural position
      toNeuturalPosition();
    }
    if(pivot.getPivotPos() < Constants.pivotOutOfReach){//if the pivot is out of the way then the intake will coem up
      intake.setPosition(0);
    }
  }




  //not correct
  /**
   * uses the ready to shoot function to tell when the shooter can shoot with acurracy
   * @param rate th rate
   */
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




  /**
   * sets the target position and the target rate that the pivot should be going to when shooting from the short shoot position
   */
  public void ShortShoot(){
    pivot.setPTargetAngle(Constants.shortPreset[1]);
    shooter.gotoRate(Constants.shortPreset[0]);
  }



  /**
   * sets the target position and the target rate that the pivot should be going to when shooting from the long shoot position
   */
  public void longShoot(){
    pivot.setPTargetAngle(Constants.autonPreset[1]);
    shooter.gotoRate(Constants.autonPreset[0]);
  }



  /**
   * sets the target position to the neurural position
   */
  public void toNeuturalPosition(){
    pivot.setPTargetAngle(Constants.neuturalPosFront);
  }



  /**
   * shoots the balls at the short shoot
   */
  public void dShortShoot(){
    shootAlways(Constants.shortPreset[0]);
  }
  /**
   * searches through the setpoints for camera tracking
   */
  public ShooterSetpoint cameraSearch(){
    double distance = camera.getTargetDistance();
    int minErrIndex = 0;
    double minErr = Double.MAX_VALUE;
    if(camera.getTargetDistance() == null){
      return Constants.idle_Mode;
      break;
    }
    for(int i = 0; i < Constants.cameraTable.length(); i++){
      double Err = Math.abs(distance - Constants.cameraTable[i].distance);
      if(Err < minErr){
        minErrIndex = i;
      }
    }
    
    return Constants.cameraTable[minErrIndex];

  }



  /**
   * shoots the balls at the long shoot
   */
  public void dLongShoot(){
    shootAlways(Constants.autonPreset[0]);
  }
  /************************/
  /*        Modes         */
  /************************/
  /**
   * sets the mode to idle mode
   */
  public void idle_Mode(){
    shooter.set_Setpoint(Constants.idle_Setpoint);
    pivot.set_Setpoint(Constants.idle_Setpoint);
  }
  /**
   * sets the mode to short mode
   */
  public void short_Mode(){
    shooter.set_Setpoint(Constants.short_Setpoint);
    pivot.set_Setpoint(Constants.short_Setpoint);
  }
  /**
   * sets the mode to long mode
   */
  private void long_Mode(){
    shooter.set_Setpoint(Constants.long_Setpoint);
    pivot.set_Setpoint(Constants.long_Setpoint);
    
  }
  /**
   * sets the mode to intake mode
   */
  private void intake_Mode(){
    //clear setpoints
    shooter.set_Setpoint(null);
    pivot.set_Setpoint(null);// TODO: if the pivot is null disable the PID
    
    //get the intake to the bumper
    intakePosition();
    
    //set intakae mode based on driver
    if(intakeStatus == IntakeStatus.intake_In){
      // TODO: intake in
    }else if(intakeStatus == IntakeStatus.intake_Out){
      // TODO: intake_out
    }else{
      // TODO: intake off
    }
  }
  /**
   * sets the mode to camera tracking mode
   */
  private void camera_Mode(){
    shooter.set_Setpoint(cameraSearch());
    pivot.set_Setpoint(cameraSearch());
  }
  /**
   * Sets the mode to maual mode
   */
  private void manual_Mode(){
    shooter.set_Setpoint(null);
    pivot.set_Setpoint(null);
  }
  @Override
  public void periodic() {
    if(shooterMode == ShooterMode.idle_mode){
      idle_Mode();
    }
    else if(shooterMode == ShooterMode.intake_mode){
      intake_Mode();
      if()// TODO: make the modes for intaking the balls
    }
    else if(shooterMode == ShooterMode.camera_mode){
      camera_Mode();
    }
    else if(shooterMode == ShooterMode.short_mode){
      short_Mode();
    }
    else if(shooterMode == ShooterMode.long_mode){
      long_Mode();
    }
    else if(shooterMode == ShooterMode.manual_mode){
      manual_Mode();
    }
    //Timer time = new Timer();
    //time.start();
    // This method will be called once per scheduler run
   // SmartDashBoard();
    //SmartDashboard.putNumber("megaTimer",  time.get());
  }
}