package frc.robot.SubSystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
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
    }
    public void setShooterSpeed(double speed){
      mLeftShooter.set(ControlMode.PercentOutput, speed);
      mRightShooter.set(ControlMode.PercentOutput, -speed);
    } 
    @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}