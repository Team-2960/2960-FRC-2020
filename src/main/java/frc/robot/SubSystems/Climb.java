/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climb extends SubsystemBase {
  private static Climb climb;

  public static Climb get_Instance(){
    if(climb == null){
      climb = new Climb();
    } 
    return climb;
  }

  private Climb() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
