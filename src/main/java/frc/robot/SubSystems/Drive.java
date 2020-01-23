/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BooleanDeserializer;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive extends SubsystemBase {
  private static Drive drive;

  private CANSparkMax leftMotorM;
  private CANSparkMax rightMotorM;
  private CANSparkMax leftMotor1;
  private CANSparkMax rightMotor1;
  private CANSparkMax leftMotor2;
  private CANSparkMax rightMotor2;
  private TalonSRX leftTalon;
  private TalonSRX rightTalon;
  private Boolean Talon = true;
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    if(!Talon){
    leftMotorM = new CANSparkMax(1, MotorType.kBrushless);
    leftMotor1 = new CANSparkMax(1, MotorType.kBrushless);
    leftMotor2 = new CANSparkMax(1, MotorType.kBrushless);
    rightMotorM = new CANSparkMax(4, MotorType.kBrushless);
    rightMotor1 = new CANSparkMax(4, MotorType.kBrushless);
    rightMotor2 = new CANSparkMax(4, MotorType.kBrushless);
    }
    else{
      leftTalon = new TalonSRX(1);
      rightTalon = new TalonSRX(0);
    }
  }
  public void move(double right, double left){
    if(Talon){
      leftTalon.set(ControlMode.PercentOutput, left);
      rightTalon.set(ControlMode.PercentOutput, -right);
    }
    else{
    leftMotorM.set( 1 * left);
    rightMotorM.set( -1 * right);
    leftMotor1.set( 1 * left);
    rightMotor1.set( -1 * right);
    leftMotor2.set( 1 * left);
    rightMotor2.set( -1 * right);
    }
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
