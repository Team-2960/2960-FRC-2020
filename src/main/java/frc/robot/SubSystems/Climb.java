/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climb extends SubsystemBase {
  private static Climb climb;
  private TalonFX mClimb;
  
  /** 
   * @return Climb
   */
  public static Climb get_Instance(){
    if(climb == null){
      climb = new Climb();
    } 
    return climb;
  }

  private Climb() {
    mClimb = new TalonFX(Constants.mClimb);
  }

  
  /** 
   * @param speed
   */
  public void setSpeed(double speed){
    mClimb.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
