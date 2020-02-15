/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.robot.SubSystems.*;
import frc.robot.Constants;
import frc.robot.Camera.Camera;

public class MEGAShooter extends SubsystemBase {
  private static MEGAShooter megaShooter;
  private Intake intake;
  private Drive drive;
  private Shooter shooter;
  public Pivot pivot;
  private Index index;
  private Camera camera;
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
    drive = Drive.get_Instance();
    intake = Intake.get_Instance();
    shooter = Shooter.get_Instance();
    pivot = Pivot.get_Instance();
    index = Index.get_Instance();

  }
  public void intakeEnable(){
      intake.setPosition(down);
      pivot.gotToAngle(intake);
      shooter.setPIDShooterSpeed(intake);
      intake.setSpeed(in);
      index.enableIndex(1);
  }
  public void intakeDisable(){
    intake.setSpeed(off);
    pivot.goToAngle(neutural);
    intake.setPosition(up);
    index.disableIndex();
  }
  public void shoot(boolean forward){
    shooter.setPIDShooterSpeed();
    if(shooter.checkAtRate()){
      index.enableIndex(-1);
    }
  }
  public void disableShoot(){
    index.disableIndex();
    shooter.setPIDShooterSpeed(0);
    pivot.goToAngle(neutural);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
