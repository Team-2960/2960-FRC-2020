package frc.robot.SubSystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase{
    private static Shooter shooter;

    private TalonFX mLeftShooter;
    private TalonFX mRightShooter;

    public static Shooter get_Instance(){
      if(shooter == null){
        shooter = new Shooter();
      } 
      return shooter;
    }
    
    private Shooter(){
      mLeftShooter = new TalonFX(Constants.mLeftShooter);
      mRightShooter = new TalonFX(Constants.mRightShooter);

    
      mRightShooter.setInverted(true);

      mRightShooter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
      
      mRightShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, 30);
      mRightShooter.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 30);

      mRightShooter.configNominalOutputForward(0, 30);
      mRightShooter.configNominalOutputReverse(0, 30);
      mRightShooter.configPeakOutputForward(1, 30);
      mRightShooter.configPeakOutputReverse(-1, 30);

      mRightShooter.selectProfileSlot(0, 0);
		  mRightShooter.config_kP(0, 0.055, 30);
		  mRightShooter.config_kI(0, 0.0002, 30);
      mRightShooter.config_kD(0, 0.00, 30);
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
		  mLeftShooter.config_kP(0, 0.055, 30);
		  mLeftShooter.config_kI(0, 0.0002, 30);
      mLeftShooter.config_kD(0, 0.00, 30);
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
    public void smart(){
      SmartDashboard.putNumber("encoder1", mRightShooter.getSelectedSensorVelocity());
      SmartDashboard.putNumber("encoder2", mLeftShooter.getSelectedSensorVelocity());
    }
    @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}