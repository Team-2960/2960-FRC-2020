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

  private int isIndexEnabled = 0;

  private int balls; // not very important

  /** 
   * @return Index
   */
  public static Index get_Instance(){
    if(index == null){
      index = new Index();
    } 
    return index;
  }
  private Index() {
    mLeftIndex = new CANSparkMax(Constants.mLeftIndex, MotorType.kBrushless);
    mRightIndex = new CANSparkMax(Constants.mRightIndex, MotorType.kBrushless);
    photoeye = new CANDigitalInput(mRightIndex,  CANDigitalInput.LimitSwitch.kForward,  CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);

  }
  /**
   * Gets the photo eye and prints it
   */
  public void setSpeed(double speed){
    mLeftIndex.set(speed);
    mRightIndex.set(speed);
  }
  public void startIndexIn(){
      if(photoeye.get())
        setSpeed(0.5);
      else
        setSpeed(0);
  }
  public void startIndexOut(){
    if(!photoeye.get())
      setSpeed(-0.5);
    else
      setSpeed(0);
}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run 
    if(isIndexEnabled == 1){
      startIndexIn();
    }else if(isIndexEnabled == -1){
      startIndexOut();
    }else{
      setSpeed(0);
    }
  }

  public void enableIndex(int dirction){
    isIndexEnabled = dirction;
  }
  public void disableIndex(){
    isIndexEnabled = 0;
  }
}
