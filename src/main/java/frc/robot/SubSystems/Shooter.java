package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase{
    private static Shooter shooter;
    private boolean isPivotEnabled = false;
    private double pTargetPivot;
    //shooter motor
    private TalonFX mLeftShooter;
    private TalonFX mRightShooter;
    //Pivot motor
    private CANSparkMax mLeftPivot;
    private CANSparkMax mRightPivot;
    //Pivot PID controler
    private PIDController aPidController;
    private ArmFeedforward armfeedforward;

    private CANEncoder EArm;
  
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

      mLeftPivot = new CANSparkMax(Constants.mLeftPivot, MotorType.kBrushless);
      mRightPivot = new CANSparkMax(Constants.mRightPivot, MotorType.kBrushless);

      //encoder 
      eArm = new Encoder(4, 5, true, Encoder.EncodingType.k4X);
    
      //Arm PID
      eArm.reset();
      eArm.setMaxPeriod(.1);
      eArm.setMinRate(10);
      eArm.setSamplesToAverage(7);
      eArm.setDistancePerPulse(360.0/1024.0);
      aPidController = new PIDController(Constants.Kp, Constants.Ki, Constants.Kd);
      armfeedforward = new ArmFeedforward(Constants.Ks, Constants.Kcos, Constants.Kv, Constants.Ka);
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
      
      EArm = new CANEncoder(mRightPivot);
      
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
    public void SetPivotSpeed(double speed){
      mLeftPivot.set(speed);
      mRightPivot.set(speed);
    }
    public void SetPivotVoltage(double Voltage){
      mLeftPivot.setVoltage(Voltage);
      mRightPivot.setVoltage(Voltage);
    }
    //set Pivot speed
    public void SetPivotPIDRate(double rate){
      double pid_output = aPidController.calculate(EArm.getVelocity(), rate);
      double feedforward = armfeedforward.calculate(EArm.getPosition(), 0) / RobotController.getBatteryVoltage();
      double speed = pid_output + feedforward;
      SmartDashboard.putNumber("pidout", pid_output);
      SmartDashboard.putNumber("feed", feedforward);
      SetPivotSpeed(speed);
    }

    //set target Pivot Pivot
    public void setPTargetAngle(double target){
      pTargetPivot = target;
    }

    //set Pivot Pivot
    private void setPAngle(double Pivot){
      double error = eArm.getDistance() - Pivot;
      double set_speed = 40 * error;
      SmartDashboard.putNumber("speed set point", set_speed );
      set_speed = MathUtil.clamp(set_speed, -600, 1000);

      double minspeed = 0;
      if(eArm.getDistance() < -50){
        minspeed = -76;
      }
      else if(eArm.getDistance() < -20){
        minspeed = -450;
      }
      else{
        minspeed = 0;
      }
      
      SetPivotPIDRate(-set_speed + minspeed);

      SmartDashboard.putNumber("speed set point - Clamped", set_speed );
    }

    @Override
  public void periodic() {
    // This method will be called once per scheduler run
    System.out.println("hello");
    //enable pivot PID
    if(isPivotEnabled){
      setPAngle(pTargetPivot);
    }
    else{
      pTargetPivot = 0;
    }


    SmartDashboard.putNumber("Encoder ", eArm.getDistance());
    SmartDashboard.putNumber("Encoder rate ", EArm.getVelocity());
    SmartDashboard.putNumber("mSpeed ", mLeftPivot.get());
    SmartDashboard.putNumber("Error ", eArm.getDistance() - -40);
    
  }

  //enable the pivot pid
  public void EnablePivotPID(){
    isPivotEnabled = true;
  }
  //disable the pivot pid
  public void DisablePivotPID(){
    isPivotEnabled = false;
    pTargetPivot = 0;
    SetPivotSpeed(0);
  }
}