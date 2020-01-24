/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpiutil.math.MathUtil;


public class Drive extends SubsystemBase {
  private static Drive drive;
  //motors
  private CANSparkMax mLeftMaster;
  private CANSparkMax mLeftFollow1;
  private CANSparkMax mLeftFollow2;
  private CANSparkMax mRightMaster;
  private CANSparkMax mRightMfollow1;
  private CANSparkMax mRightMfollow2;
  private PIDController drivePidController;
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    //init code
    mLeftMaster = new CANSparkMax(Constants.mLeftMaster, MotorType.kBrushless);
    mLeftFollow1 = new CANSparkMax(Constants.mLeftFollow1, MotorType.kBrushless);
    mLeftFollow2 = new CANSparkMax(Constants.mLeftFollow2, MotorType.kBrushless);

    mRightMaster = new CANSparkMax(Constants.mRightMaster, MotorType.kBrushless);
    mRightMfollow1 = new CANSparkMax(Constants.mRightMfollow1, MotorType.kBrushless);
    mRightMfollow2 = new CANSparkMax(Constants.mRightMfollow2, MotorType.kBrushless);

    //set master moter and follower motor
    mLeftFollow1.follow(mLeftMaster);
    mLeftFollow2.follow(mLeftMaster);
    mRightMfollow1.follow(mRightMaster);
    mRightMfollow2.follow(mRightMaster);


    drivePidController = new PIDController(0.00001, 0, 0);
  }
  public double rate(double previousRate, double currentRate){
    double rate;
    rate = currentRate - previousRate;
    return rate;
  }
  //set motor speed
  public void setDrivePID(double rate, double setpoint){
    double speed = MathUtil.clamp(drivePidController.calculate(rate, setpoint), -0.25, 0.5);
  }
  public void move(double left, double right){
    mLeftMaster.set(left);
    mRightMaster.set(-right);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
