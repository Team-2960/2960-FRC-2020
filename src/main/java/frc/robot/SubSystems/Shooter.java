package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import frc.robot.PID.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase{
    private static Shooter shooter;

    private TalonFX mLeftShooter;
    private TalonFX mRightShooter;

    private CANSparkMax mLeftangle;
    private CANSparkMax mRightangle;

    
    private PIDController aPidController;

    private SpeedControllerGroup mAngle;
  
    private Encoder eArm;
    
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
      mLeftShooter = new TalonFX(Constants.mLeftShooter);
      mRightShooter = new TalonFX(Constants.mRightShooter);

      mLeftangle = new CANSparkMax(11, MotorType.kBrushless);
      mRightangle = new CANSparkMax(12, MotorType.kBrushless);

      mAngle = new SpeedControllerGroup(mLeftangle, mRightangle);

      eArm = new Encoder(4, 5, true, Encoder.EncodingType.k4X);
    
      //Arm PID
      eArm.reset();
      eArm.setMaxPeriod(.1);
      eArm.setMinRate(10);
      eArm.setSamplesToAverage(7);
      eArm.setDistancePerPulse(360.0/1024.0);
      aPidController = new PIDController(0.004, 0.0001, 0.01);

      mRightShooter.setInverted(true);

      mRightShooter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
      
      mRightShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
      mRightShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);

      mRightShooter.configNominalOutputForward(0, 30);
      mRightShooter.configNominalOutputReverse(0, 30);
      mRightShooter.configPeakOutputForward(1, 30);
      mRightShooter.configPeakOutputReverse(-1, 30);

      mRightShooter.selectProfileSlot(0, 0);
		  mRightShooter.config_kP(0, kp, 30);
		  mRightShooter.config_kI(0, ki, 30);
      mRightShooter.config_kD(0, kd, 30);
      mRightShooter.config_kF(0, 1023.0/22968.0, 30);
      
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
      
      
      mLeftShooter.setSelectedSensorPosition(0, 0, 30);
    }

    public void setShooterSpeed(double speed){
      mRightShooter.set(ControlMode.PercentOutput, speed);
    }

    public void setPIDShooterSpeed(double speed){
      mRightShooter.set(ControlMode.Velocity, speed);
      mLeftShooter.set(ControlMode.Velocity, speed);
    }
    public void SetSpeed(double speed){
      mLeftangle.set(speed);
      mRightangle.set(-speed);
    }
    public void SetPIDSpeed(double setpoint){
      /*       MathUtil.clamp(pid.calculate(encoder.getDistance(), setpoint), -0.5, 0.5);
 */   double speed = MathUtil.clamp(aPidController.calculate(eArm.getRate(), setpoint), -0.25, 0.5);
      mLeftangle.set(speed);
      mRightangle.set(-speed);
    }
    public void smart(){
      SmartDashboard.putNumber("encoder1", mRightShooter.getSelectedSensorVelocity());
      SmartDashboard.putNumber("encoder2", mLeftShooter.getSelectedSensorVelocity());
    }
    @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}