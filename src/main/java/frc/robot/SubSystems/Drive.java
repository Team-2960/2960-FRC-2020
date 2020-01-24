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
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANPIDController;

public class Drive extends SubsystemBase {
  private static Drive drive;

  private CANSparkMax mLeftMaster;
  private CANSparkMax mLeftFollow1;
  private CANSparkMax mLeftFollow2;
  private CANSparkMax mRightMaster;
  private CANSparkMax mRightMfollow1;
  private CANSparkMax mRightMfollow2;
  private TalonSRX leftTalon;
  private TalonSRX rightTalon;
  private Boolean Talon = false;
  private CANPIDController leftMotorPID;
  private CANPIDController rightMotorPID;
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    if(!Talon){
    mLeftMaster = new CANSparkMax(Constants.mLeftMaster, MotorType.kBrushless);
    mLeftFollow1 = new CANSparkMax(Constants.mLeftFollow1, MotorType.kBrushless);
    mLeftFollow2 = new CANSparkMax(Constants.mLeftFollow2, MotorType.kBrushless);

    mRightMaster = new CANSparkMax(Constants.mRightMaster, MotorType.kBrushless);
    mRightMfollow1 = new CANSparkMax(Constants.mRightMfollow1, MotorType.kBrushless);
    mRightMfollow2 = new CANSparkMax(Constants.mRightMfollow2, MotorType.kBrushless);

    mLeftFollow1.follow(mLeftMaster);
    mLeftFollow2.follow(mLeftMaster);
    mRightMfollow1.follow(mRightMaster);
    mRightMfollow2.follow(mRightMaster);
    
    leftMotorPID = new CANPIDController(mLeftMaster);
    rightMotorPID = new CANPIDController(mRightMaster);

    leftMotorPID.setOutputRange(-1, 1);
    rightMotorPID.setOutputRange(-1, 1);
    rightMotorPID.setP(0.00001);
    leftMotorPID.setP(0.00001);
    rightMotorPID.setI(0);
    leftMotorPID.setI(0);
    rightMotorPID.setD(0);
    leftMotorPID.setD(0);
    
    }
    else{
      leftTalon = new TalonSRX(1);
      rightTalon = new TalonSRX(0);
    }
  }
  public void setGyroPID(double gyro){
    leftMotorPID.setReference(gyro, ControlType.kVelocity);
    rightMotorPID.setReference(gyro, ControlType.kVelocity);
  }
  public void move(double left, double right){
    if(Talon){
      leftTalon.set(ControlMode.PercentOutput, left);
      rightTalon.set(ControlMode.PercentOutput, right);
    }
    else{
    mLeftMaster.set(left);
    mRightMaster.set(-right);
    }
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
