package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.SubSystems.*;
import frc.robot.Constants;
import frc.robot.Camera.Camera;



public class MEGAShooter extends SubsystemBase {
  private static MEGAShooter megaShooter;

  public enum ShooterMode{short_mode, long_mode, camera_mode, intake_mode, idle_mode, manual_mode, auton_Mode};
  public enum IntakeStatus{intake_In, intake_Out, intake_Off};

  private ShooterMode shooterMode = ShooterMode.idle_mode;
  private IntakeStatus intakeStatus = IntakeStatus.intake_Off;

  private boolean isShooting = false;
  private boolean isIndexOut = false;



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
   * Sets the mode for the shooter to be in for OI
   * @param shooterMode
   */
  public void setShooterMode(ShooterMode shooterMode){
    this.shooterMode = shooterMode;
  }
  /**
   * sets the mdoe for the intake
   */
  public void setintakeStatus(IntakeStatus intakeStatus){
    this.intakeStatus = intakeStatus;
  }
  /**
   * sets the shooting mode for OI
   */
  public void setShoot(boolean isShooting){
    this.isShooting = isShooting;
  }

  /**
   * sets the index mode for OI
   */
  public void setIndex(boolean isIndexOut){
    this.isIndexOut = isIndexOut;
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

  public void disableIntakeDr(){
    //intake in
    index.enableIndex(1);
    intake.setSpeed(0);
    shooter.gotoRate(0);
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
    intake.setPosition(1);//if the pivot is not at the bumper then it is putting the intake down
    if(intake.isIntakeOut()){//uses the intake out function to tell when it is out      
      if(pivotToBumper()){ 
        pivot.DisablePivotPID();// if the pivot is down at the bumper then it stops the pvot PID
      }
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
  public void shootAlways(){
    if(shooter.readyToShoot()){
      index.enableIndex(-1);
    }
    else{
      index.disableIndex();
    }

  }








  /**
   * sets the target position to the neurural position
   */
  public void toNeuturalPosition(){
    pivot.setPTargetAngle(Constants.neuturalPosFront);
  }




  /**
   * searches through the setpoints for camera tracking
   */
/*   public Shooter_Setpoint cameraSearch(){
    double distance = camera.getTargetDistance();
    Shooter_Setpoint setpoint = Constants.camera_DefaultSetpoint;
    double minErr = Double.MAX_VALUE;

    if(camera.isTargetFound()){
      for(int i = 0; i < Constants.cameraTable.length(); i++){
        double Err = Math.abs(distance - Constants.cameraTable[i].distance);
        if(Err < minErr){
          setpoint = Constants.cameraTable[i];
        }
      }
    }
    
    return setpoint;

  } 
  /************************/
  /*        Modes         */
  /************************/
  /**
   * sets the mode to idle mode
   */
  private void idle_Mode(){
    shooter.set_Setpoint(Constants.idlePreset);
    pivot.set_Setpoint(Constants.idlePreset);
  }
  /**
   * sets the mode to short mode
   */
  private void short_Mode(){
    shooter.set_Setpoint(Constants.shortPreset);
    pivot.set_Setpoint(Constants.shortPreset);
  }
  /**
   * sets the mode to long mode
   */
  private void long_Mode(){
    shooter.set_Setpoint(Constants.longPreset);
    pivot.set_Setpoint(Constants.longPreset);
    
  }
  /**
   * sets the shooter to auton mode
   */
  private void auton_Mode(){
    shooter.set_Setpoint(Constants.autonPreset);
    pivot.set_Setpoint(Constants.autonPreset);
    
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
      intakeEnableDr();
    }else if(intakeStatus == IntakeStatus.intake_Out){
      intakeOutEnableDr();
    }else{
      disableIntakeDr();
    }
  }
  /**
   * sets the mode to camera tracking mode
   */
/*   private void camera_Mode(){
    shooter.set_Setpoint(cameraSearch());
    pivot.set_Setpoint(cameraSearch());
  } */
  /**
   * Sets the mode to maual mode
   */
  private void manual_Mode(){
    shooter.set_Setpoint(null);
    pivot.set_Setpoint(null);
  }
  @Override
  public void periodic() {
    switch(shooterMode){
      case auton_Mode:
        auton_Mode();
        break;
      case intake_mode:
        intake_Mode();
        break;
      case camera_mode:
        //camera_Mode();
        break;
      case short_mode:
        short_Mode();
        break;
      case long_mode:
        long_Mode();
        break;
      case manual_mode:
        manual_Mode();
        break;
      default:
        idle_Mode();
        break;
    }
    //Timer time = new Timer();
    //time.start();
    // This method will be called once per scheduler run
   // SmartDashBoard();
    //SmartDashboard.putNumber("megaTimer",  time.get());
  }
}