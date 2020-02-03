/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;


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

  
  public double previousAngle = 0.0;
  public double currentAngle = 0.0;

  
  private AnalogGyro gyro;
  private AHRS navX;

  private DutyCycleEncoder absoluteEncoder;
  private Encoder leftEncoder;
  private DigitalInput photoeye;
  
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

    previousAngle = currentAngle;

    gyro = new AnalogGyro(0);
    navX = new AHRS(SPI.Port.kMXP);
    absoluteEncoder = new DutyCycleEncoder(2);
    leftEncoder = new Encoder(0, 1);

    gyro.calibrate();

    currentAngle = navX.getAngle();

    drivePidController = new PIDController(Constants.dKp, Constants.dKi, Constants.dKd);
    
  }

  public double rate(double previousRate, double currentRate){
    double rate;
    rate = currentRate - previousRate;
    return rate;
  }

  //rate drive
  public void setDriveRate(double rate){
    double speed = drivePidController.calculate(navX.getRawGyroZ(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed, speed);
  }
  public void setDriveToAngle(double angle, double forwardspeed){
    double error = angle - navX.getAngle();
    double absError = Math.abs(error);
    int negative;
    
    if(error < 0){
      negative = -1;
    }
    else{
      negative = 1;
    }
    if(absError > 20){
    double rate = 2.5 * error;
    setArcDriveRate(rate, forwardspeed);
    }
    else if(absError < 2){
      setArcDriveRate(0, forwardspeed);
    }
    else{
      double rate = 70;
      setArcDriveRate(negative * rate, forwardspeed);
    }
  }
  //Arc drive pid
  public void setArcDriveRate(double rate, double forwardSpeed){
    double speed = drivePidController.calculate(navX.getRawGyroZ(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed + forwardSpeed, speed + forwardSpeed);
  }

  //set lefe and right motor speed.
  public void setSpeed(double left, double right){
    mLeftMaster.set(left);
    mRightMaster.set(-right);
  }
  public void navXReset(){
    navX.reset();
  }
  @Override
  // This method will be called once per scheduler run
  public void periodic() {
    SmartDashboard.putNumber("NavX Config", navX.getActualUpdateRate());
    previousAngle = currentAngle;
    currentAngle = navX.getAngle();
    SmartDashboard.putNumber("rate", navX.getYaw());
    SmartDashboard.putNumber("gyroz", navX.getRawGyroZ());
    SmartDashboard.putNumber("Accelration x", navX.getRawAccelX());
    SmartDashboard.putNumber("Accelration y", navX.getRawAccelY());
    SmartDashboard.putNumber("Gyro Angle", navX.getAngle());

  }
}
