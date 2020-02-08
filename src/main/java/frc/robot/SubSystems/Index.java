package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Index extends SubsystemBase {
  private static Index index;
  private CANDigitalInput photoeye;
  
  private CANSparkMax mRightIndex;
  private CANSparkMax mLeftIndex;
  public static Index get_Instance(){
    if(index == null){
      index = new Index();
    } 
    return index;
  }
  private Index() {
    mRightIndex = new CANSparkMax(11, MotorType.kBrushless);
    photoeye = new CANDigitalInput(mRightIndex,  CANDigitalInput.LimitSwitch.kForward,  CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);

  }
  public void setSpeed(double speed){
    System.out.println(photoeye.get());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run 
    System.out.println(photoeye.get());
  }
}
