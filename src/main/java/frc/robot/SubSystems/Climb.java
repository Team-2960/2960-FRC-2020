/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climb extends SubsystemBase {
  private static Climb climb;
  private TalonFX mClimb;
  private TalonFX mClimb2;
  private DoubleSolenoid sClimb;
  
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
    mClimb2 = new TalonFX(Constants.mClimb2);
    sClimb = new DoubleSolenoid(Constants.ClimbSolenoid1, Constants.ClimbSolenoid2);
  }

  
  /** 
   * @param speed
   */
  public void setSpeed(double speed){
    mClimb.set(ControlMode.PercentOutput, speed);
   mClimb2.set(ControlMode.PercentOutput, speed); 

  }
  
  /** 
   * @param state
   */
    public void setPosition(int state){
      if(state == 0)
        sClimb.set(Value.kForward);
      else if(state == 1)
        sClimb.set(Value.kReverse);
      else
        sClimb.set(Value.kOff);
    }
  private void SmartDashboard(){
    SmartDashboard.putNumber("winch motor", mClimb.getSupplyCurrent());
    SmartDashboard.putNumber("winch motor2", mClimb2.getSupplyCurrent());
  }
  @Override
  public void periodic() {
   SmartDashboard();
   // This method will be called once per scheduler run

  }
}
