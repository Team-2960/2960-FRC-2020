/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  private static Intake intake;
  private CANSparkMax mIntake;
  private DoubleSolenoid iSolenoid;
  
  /** 
   * @return Intake
   */
  public static Intake get_Instance(){
    if(intake == null){
      intake = new Intake();
    } 
    return intake;
  }
  private Intake() {
    iSolenoid = new DoubleSolenoid(0, 1);
    mIntake = new CANSparkMax(Constants.mIntake, MotorType.kBrushless);
  }

  
  /** 
   * @param speed
   */
  public void setSpeed(double speed){
    mIntake.set(speed);
  }
  public void setPosition(boolean setPosition){
    if(setPosition){
      iSolenoid.set(Value.kReverse); 
    } else{
      iSolenoid.set(Value.kForward);
  }
}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
