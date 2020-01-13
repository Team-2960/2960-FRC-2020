/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  private static Intake intake;
  private CANSparkMax mIntake;
  public void setIntakeSpeed(double speed){
    mIntake.set(speed);
  }
  public static Intake get_Instance(){
    if(intake == null){
      intake = new Intake();
    } 
    return intake;
  }
  private Intake() {
    mIntake = new CANSparkMax(Constants.mIntake, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
