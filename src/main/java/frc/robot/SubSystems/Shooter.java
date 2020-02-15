package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Shooter extends SubsystemBase{
    private static Shooter shooter;
    //shooter motor
    private TalonFX mLeftShooter;
    private TalonFX mRightShooter;

    //pid value will move to constants later.
    double kp = 0.033,
           ki = 0.000045,
           kd = 0.01;
    
    /** 
     * @return Shooter
     */
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
    }
    /**
     * Sets the Shooter Speed 
     * @param speed the speed you want the shooter to be set to
     */
    public void setShooterSpeed(double speed){
      mRightShooter.set(ControlMode.PercentOutput, speed);
    }
    /**
     * Sets the PID shooter speed 
     * @param speed the PID shooter speed
     */
    public void setPIDShooterSpeed(double speed){
      mRightShooter.set(ControlMode.Velocity, speed);
      mLeftShooter.set(ControlMode.Velocity, speed);
    }
}