package frc.robot.SubSystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
public class Shooter extends SubsystemBase{
    private static Shooter shooter;
    private TalonFX lshootMotor;
    private TalonFX rshootMotor;
    private CANCoder canCoderl;
    private CANCoder canCoderr;
    public void shoot(double right, double left){
      lshootMotor.set(ControlMode.PercentOutput, left);
      rshootMotor.set(ControlMode.PercentOutput, left);
    }
    public void shooterEncoder(){
      System.out.println(canCoderl.getVelocity());
      System.out.println(canCoderr.getVelocity());
    }
    public static Shooter get_Instance(){
      if(shooter == null){
        shooter = new Shooter();
      } 
      return shooter;
    }
    
    private Shooter(){
      lshootMotor = new TalonFX(3);
      rshootMotor = new TalonFX(4);
      canCoderl = new CANCoder(3);
      canCoderr = new CANCoder(4);
    }
    @Override
  public void periodic() {


    // This method will be called once per scheduler run
  }
}