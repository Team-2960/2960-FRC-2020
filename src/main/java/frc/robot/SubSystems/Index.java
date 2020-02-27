package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Index extends SubsystemBase {
  private static Index index;
  private CANDigitalInput photoeye;
  
  private CANSparkMax mRightIndex;
  private CANSparkMax mLeftIndex;

  private int isIndexEnabled = 0;

  private int balls; // not very important
  private boolean inBalls = true;
  private boolean outBalls = true;
  private boolean isAutoIndexEnabled = false;
  public boolean lostBalls = false;

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
    mLeftIndex = new CANSparkMax(Constants.mIndex1, MotorType.kBrushless);
    mRightIndex = new CANSparkMax(Constants.mIndex2, MotorType.kBrushless);
    photoeye = new CANDigitalInput(mLeftIndex,  CANDigitalInput.LimitSwitch.kForward,  CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);

  }
  /**
   * Gets the photo eye and prints it
   */
  public void setSpeed(double right, double left){
    mLeftIndex.set(left);
    mRightIndex.set(-right);
  }
  private void startIndexIn(){
      if(photoeye.get()){
        setSpeed(1, 0.45);
        inBalls = true;
      }
      else{
        setSpeed(0, 0);
        if(inBalls){
          balls++;
          inBalls = false;
        }
      }
  }
  private void startIndexOut(){
    
    if(!photoeye.get()){
      setSpeed(-1, -1);
      outBalls = true;
      lostBalls = false;
    }
    else{
      setSpeed(0, 0);
      if(outBalls){
        balls--;
        outBalls = false;
        lostBalls = true;
      }
    }
  }
public boolean getPhotoeyeIndex(){
  return photoeye.get();
}
  public void SmartDashboard(){
    SmartDashboard.putNumber("Balls in Robot", balls);
    SmartDashboard.putBoolean("indexPhoto", photoeye.get());
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run 
    if(isAutoIndexEnabled){
      if(isIndexEnabled == 1){
        startIndexIn();
      }else if(isIndexEnabled == -1){
        startIndexOut();
      }
    }
  }

  
  /** 
   * @param dirction
   */
  public void enableIndex(int dirction){
    isAutoIndexEnabled = true;
    isIndexEnabled = dirction;
  }
  
  public void disableIndex(){
    isAutoIndexEnabled = false;
    setSpeed(0, 0);
    isIndexEnabled = 0;
  }
}
