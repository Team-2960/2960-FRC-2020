/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
  static Drive drive;

  static Drive get_Instance(){
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
