package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Index extends SubsystemBase {
  private static Index index;
  private CANDigitalInput photoeye;
  
  private CANSparkMax mRightIndex;
  private CANSparkMax mLeftIndex;

  private int isIndexEnabled = 0;

  public int balls = 3; // not very important
  private boolean inBalls = true;
  private boolean outBalls = true;
  private boolean isAutoIndexEnabled = false;
  public boolean lostBalls = false;
  public boolean seeBalls = false;

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
    mRightIndex.getEncoder().setPosition(0);
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
        setSpeed(0.9, 0.75);// 1, 0.85
        inBalls = true;
      }
      else{
        setSpeed(0, 0);
        if(inBalls){
          
          inBalls = false;
        }
      }
  }
  private void startIndexOut(){
    if(seeBalls){
      if(!photoeye.get()){
       
       setSpeed(0,0);
       outBalls = false;
       seeBalls = false;
       lostBalls = true;
     }
   }
    if(!photoeye.get()){
      setSpeed(-1, -1);
      seeBalls = false;
      outBalls = true;
      lostBalls = false;
    }
    else{
      seeBalls = true;
      setSpeed(-1, -1);
      lostBalls = false;
    }
  
  
  }
  public boolean indexBeltsGoneDistance(double distance){
    return distance < getEncoderDistance();
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
    //Timer time = new Timer();
    //time.start();
    // This method will be called once per scheduler run
    // This method will be called once per scheduler run 
    //SmartDashboard();
    if(isAutoIndexEnabled){
      if(isIndexEnabled == 1){
        startIndexIn();
      }else if(isIndexEnabled == -1){
        startIndexOut();
      }
    }
    //SmartDashboard.putNumber("IndexTimer",  time.get());
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
  public double getEncoderDistance(){
    return mRightIndex.getEncoder().getPosition();
  }
}
