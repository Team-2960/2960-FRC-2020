/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive extends SubsystemBase {
  private static Drive drive;

  private CANSparkMax leftMotor;
  private CANSparkMax rightMotor;
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    leftMotor = new CANSparkMax(2, MotorType.kBrushless);
    rightMotor = new CANSparkMax(1, MotorType.kBrushless);
  }
  public void move(double right, double left){
    leftMotor.set( 1 * left);
    rightMotor.set( -1 * left);
    System.out.println(rightMotor.getEncoder());
    System.out.println(leftMotor.getEncoder());
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
