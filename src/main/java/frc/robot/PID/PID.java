/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.PID;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class PID extends SubsystemBase {
  private static PID pid;

  public static PID get_Instance(){
    if(pid == null){
      pid = new PID();
    } 
    return pid;
    
  }

  private PID() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
