package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import java.lang.Math;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase{
    private static Shooter shooter;
    private boolean isPivotEnabled = false;
    private double pTargetAngle;
    //shooter motor
    private TalonFX mLeftShooter;
    private TalonFX mRightShooter;
    //Angle motor
    private CANSparkMax mLeftangle;
    private CANSparkMax mRightangle;
    //angle PID controler
    private PIDController aPidController;

    private CANEncoder EArm;

    private SpeedControllerGroup mAngle;
  
    private Encoder eArm;
    //pid value will move to constants later.
    double kp = 0.033,
           ki = 0.000045,
           kd = 0.01;

    
    public static Shooter get_Instance(){
      if(shooter == null){
        shooter = new Shooter();
      } 
      return shooter;
    }
    
    private Shooter(){
      //init code
      //mLeftShooter = new TalonFX(Constants.mLeftShooter);
      //mRightShooter = new TalonFX(Constants.mRightShooter);

      mLeftangle = new CANSparkMax(11, MotorType.kBrushless);
      mRightangle = new CANSparkMax(12, MotorType.kBrushless);

      mAngle = new SpeedControllerGroup(mLeftangle, mRightangle);
      //encoder 
      eArm = new Encoder(4, 5, true, Encoder.EncodingType.k4X);
    
      //Arm PID
      eArm.reset();
      eArm.setMaxPeriod(.1);
      eArm.setMinRate(10);
      eArm.setSamplesToAverage(7);
      eArm.setDistancePerPulse(360.0/1024.0);
      aPidController = new PIDController(0.00015, 0.0, 0.0);

      /* //invert right shooter motor
      mRightShooter.setInverted(true);

      //set the rigth motor pid
      mRightShooter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
      
      mRightShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
      mRightShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);
      //out put range
      mRightShooter.configNominalOutputForward(0, 30);
      mRightShooter.configNominalOutputReverse(0, 30);
      mRightShooter.configPeakOutputForward(1, 30);
      mRightShooter.configPeakOutputReverse(-1, 30);
      //set PID value
      mRightShooter.selectProfileSlot(0, 0);
		  mRightShooter.config_kP(0, kp, 30);
		  mRightShooter.config_kI(0, ki, 30);
      mRightShooter.config_kD(0, kd, 30);
      mRightShooter.config_kF(0, 1023.0/22968.0, 30);
      
      //get sensor value
      mLeftShooter.setSelectedSensorPosition(0, 0, 30);
      
      mLeftShooter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
      
      mLeftShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
      mLeftShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);

      mLeftShooter.configNominalOutputForward(0, 30);
      mLeftShooter.configNominalOutputReverse(0, 30);
      mLeftShooter.configPeakOutputForward(1, 30);
      mLeftShooter.configPeakOutputReverse(-1, 30);

      mLeftShooter.selectProfileSlot(0, 0);
		  mLeftShooter.config_kP(0, kp, 30);
		  mLeftShooter.config_kI(0, ki, 30);
      mLeftShooter.config_kD(0, kd,30);
      mLeftShooter.config_kF(0, 1023.0/22968.0, 30);
      
      
      mLeftShooter.setSelectedSensorPosition(0, 0, 30); */
      
      EArm = new CANEncoder(mRightangle);
      
    }
    //set the shooter speed
    public void setShooterSpeed(double speed){
      mRightShooter.set(ControlMode.PercentOutput, speed);
    }
    //setting the pid speed
    public void setPIDShooterSpeed(double speed){
      mRightShooter.set(ControlMode.Velocity, speed);
      mLeftShooter.set(ControlMode.Velocity, speed);
    }
    public void SetSpeed(double speed){
      mLeftangle.set(speed);
      mRightangle.set(-speed);
    }
    //set arm speed
    public void SetPivotPIDRate(double rate){
      /*       MathUtil.clamp(pid.calculate(encoder.getDistance(), setpoint), -0.5, 0.5);
 */   double speed = MathUtil.clamp(aPidController.calculate(EArm.getVelocity(), rate), -0.25, 0.75);
      SetSpeed(speed);
    }
    //set target Pivot Angle
    public void setPTargetAngle(double target){
      pTargetAngle = target;
    }

    //set Pivot Angle
    private void setPAngle(double angle){
      double errer = eArm.getDistance() - angle;
      double dirction;
      
      SetPivotPIDRate(MathUtil.clamp(errer * 12, -600, 1000));
      SmartDashboard.putNumber("speed", MathUtil.clamp(errer * 12, -600, 1000) );
    }
    
    @Override
  public void periodic() {
    // This method will be called once per scheduler run

    //enable pivot PID
    if(isPivotEnabled){
      setPAngle(pTargetAngle);
    }
    else{
      pTargetAngle = 0;
    }
    SmartDashboard.putNumber("Encoder ", eArm.getDistance());
    SmartDashboard.putNumber("Encoder rate ", EArm.getVelocity());
    SmartDashboard.putNumber("mSpeed ", mLeftangle.get());
    SmartDashboard.putNumber("Errer ", eArm.getDistance() - -40);
  }

  //enable the pivot pid
  public void EnablePivotPID(){
    isPivotEnabled = true;
  }
  //disable the pivot pid
  public void DisablePivotPID(){
    isPivotEnabled = false;
    pTargetAngle = 0;
  }
}