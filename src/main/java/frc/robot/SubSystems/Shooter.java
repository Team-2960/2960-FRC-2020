package frc.robot.SubSystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
public class Shooter extends SubsystemBase{
    private static Shooter shooter;
    private CANSparkMax mLeftShooter;
    private CANSparkMax mRightShooter;
    public void setShooterSpeed(double speed){
      mLeftShooter.set(speed);
      mRightShooter.set(speed);
    }
    public void shooterEncoder(){
      //System.out.println(mLeftShooter.getSelectedSensorVelocity()/3.60501567398119122257);
      //System.out.println(RightEncoder.getVelocity());
    }
    public static Shooter get_Instance(){
      if(shooter == null){
        shooter = new Shooter();
      } 
      return shooter;
    }
    
    private Shooter(){
      mLeftShooter = new CANSparkMax(Constants.mLeftShooter, MotorType.kBrushless);
      mRightShooter = new CANSparkMax(Constants.mRightShooter, MotorType.kBrushless);
      //RightEncoder = new CANCoder(Constants.mRightShooter);
    }
    @Override
  public void periodic() {


    // This method will be called once per scheduler run
  }
}