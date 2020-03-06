package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.Camera.Camera;



public class Drive extends SubsystemBase {
  private static Drive drive;
  //motors
  private CANSparkMax mLeftMaster;
  private CANSparkMax mLeftFollow1;
  private CANSparkMax mLeftFollow2;
  
  private CANSparkMax mRightMaster;
  private CANSparkMax mRightMfollow1;
  private CANSparkMax mRightMfollow2;

  //PID Controller
  private PIDController drivePidController;

  //Gyro Sensor
  private AnalogGyro gyro;
  private AHRS navX;
  //Encoders
  private CANEncoder rightEncoder;
  private CANEncoder leftEncoder;

  public double TargetDistance;
  public double TargetAngle;

  public double forwardSpeed;
  public int PIDCheck = 0;
  public boolean isDrivePIDEnabled = false;
  private static Camera camera;
  public double cameraAngle = 0;
  
  
  /** 
   * @return Drive
   */
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    //init code
    camera = Camera.get_Instance();
    //init all the motors
    mLeftMaster = new CANSparkMax(Constants.mLeftMaster1, MotorType.kBrushless);
    mLeftFollow1 = new CANSparkMax(Constants.mLeftFollow2, MotorType.kBrushless);
    mLeftFollow2 = new CANSparkMax(Constants.mLeftFollow3, MotorType.kBrushless);

    mRightMaster = new CANSparkMax(Constants.mRightMaster1, MotorType.kBrushless);
    mRightMfollow1 = new CANSparkMax(Constants.mRightMfollow2, MotorType.kBrushless);
    mRightMfollow2 = new CANSparkMax(Constants.mRightMfollow3, MotorType.kBrushless);

    //set master moter and follower motor
    mLeftFollow1.follow(mLeftMaster);
    mLeftFollow2.follow(mLeftMaster);
    mRightMfollow1.follow(mRightMaster);
    mRightMfollow2.follow(mRightMaster);

    //init gyro
    gyro = new AnalogGyro(0);
    navX = new AHRS(SPI.Port.kMXP);
    gyro.calibrate();

    //init encoder
    rightEncoder = mRightMaster.getAlternateEncoder(AlternateEncoderType.kQuadrature, Constants.pulsePerRev);
    rightEncoder.setPositionConversionFactor(Constants.wheelcircumference);
    rightEncoder.setPosition(0);
    leftEncoder = mRightMaster.getAlternateEncoder(AlternateEncoderType.kQuadrature, Constants.pulsePerRev);
    leftEncoder.setPositionConversionFactor(Constants.wheelcircumference);
    leftEncoder.setPosition(0);

    //init drive PID
    drivePidController = new PIDController(Constants.dKp, Constants.dKi, Constants.dKd);
  }


  /**
   * Checks to see if we are at the target distance
   * @return true if we are at the target distance
   */
  public boolean checkDistance(){
    boolean isAtDistance;
    double error = Math.abs(encoderPosition() - TargetDistance);
    if(error < Constants.distanceTolerance){
      isAtDistance = true;
    }
    else{
      isAtDistance = false;
    }
    return isAtDistance;
  }
  /**
   * Checks to see if we are at the target distance
   * @return true if we are at the target angle
   */
  public boolean checkAngle(){
    boolean isAtAngle;
    double error = Math.abs(getAngle() - TargetAngle);
    if(error < Constants.angleTolerance){
      isAtAngle = true;
    }
    else{
      isAtAngle = false;
    }
    return isAtAngle;
  }
  public void targetLineUp(){
    setDriveToAngle(camera.calcAngle(camera.getCenterX()) +  navX.getAngle(), 0);
  }

  /**
   * Use the PID to set the rate we are going
   * @param rate is the target angle rate we want to be going
   */
  public void setDriveRate(double rate){
    double speed = drivePidController.calculate(navX.getRawGyroX(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed, speed);
  }
  /**
   * gives the PID the numbers that we want to be going
   * Resets the Encoders
   * Resets the NavX
   * @param forwardSpeed sets the forward speed we should be going
   * @param angle sets the target angle the we should be going
   * @param distance sets the target distance we should be going
   */
  public void startGoToAngleDistance(double forwardSpeed, double angle, double distance, int PIDCheck){
    TargetDistance = distance;
    TargetAngle = angle;
    this.forwardSpeed = forwardSpeed;
    encoderReset();
    navXReset();
    enableDrivePID();
    this.PIDCheck = PIDCheck;

  }
  /**
   * Is the PID that we use to drive the robot around
   * @param forwardSpeed sets the forward speed we should be going
   * @param angle sets the target angle the we should be going
   * @param distance sets the target distance we should be going
   */
  public void setDriveAuton(double forwardSpeed, double angle, double distance){
    int negative = (distance < 0) ? -1 : 1;

    double abscurrentposition = Math.abs(encoderPosition());

    if(abscurrentposition < Math.abs(distance)){
      if(Math.abs(distance) - abscurrentposition > 24){
        setDriveToAngle(angle, (forwardSpeed));
      }
      else if(Math.abs(distance) - abscurrentposition > 4){
        setDriveToAngle(angle,negative * -0.1);
      }
      else{
        setDriveToAngle(angle, negative * -0.05);
      }
    }
    else{
      setDriveRate(0);
    }
  }
  /**
   * Sets the drive to go to certain angle with forward speed
   * @param angle the target angle we should be going
   * @param forwardspeed Sets the forward speed we are going
   */
  public void setDriveToAngle(double angle, double forwardspeed){
    double error =  angle - navX.getAngle();
    double absError = Math.abs(error);
    int negative;
    System.out.println(absError);
    if(error < 0){
      negative = -1;
    }
    else{
      negative = 1;
    }
    if(absError > 15){
    double rate = 5 * error;
    setArcDriveRate(rate, forwardspeed);
    System.out.println(rate);

    } 
    else if(absError < 15 && absError > 10){
      double rate = 100;
      setArcDriveRate(negative * rate, forwardspeed);
      } 
    else if(absError < 1){
      setArcDriveRate(0, forwardspeed);
    }
    else{
      double rate = 70;
      setArcDriveRate(negative * rate, forwardspeed);
    }
  }
  /**
   * Sets the rate of the arc aswell as the speeds
   * @param rate taret rate we should be going
   * @param forwardSpeed sets the forward speed we should be going
   */
  public void setArcDriveRate(double rate, double forwardSpeed){
    double speed = drivePidController.calculate(navX.getRawGyroX(), rate); //calc the speed
    setSpeed(-speed + forwardSpeed, speed + forwardSpeed);
  }
  public void adjustToTarget(){
    startGoToAngleDistance(0, cameraAngle, 0, 2);
  }

  @Override
  // This method will be called once per scheduler run
  public void periodic() {
    SmartDashboard.putNumber("Right Encoder", rightEncoder.getPosition());
    SmartDashboard.putNumber("Left Encoder", leftEncoder.getPosition());
    SmartDashboard.putNumber("Angle", navX.getRawGyroX());

    if(isDrivePIDEnabled){
      if(PIDCheck == 1){
        drive.setDriveAuton(forwardSpeed, TargetAngle, TargetDistance);
        if(checkDistance()){
          isDrivePIDEnabled = false;
        }
      }
      else if(PIDCheck == 2){
        setDriveToAngle(TargetAngle, 0);
        if(checkAngle()){
          isDrivePIDEnabled = false;
        }
      }
      else if(PIDCheck == 3){
        drive.setDriveAuton(forwardSpeed, TargetAngle, TargetDistance);
        if(checkAngle() && checkDistance()){
          isDrivePIDEnabled = false;
        }
      }
  }
    
  //cameraAngle = (camera.calcAngle(camera.getCenterX()) +  navX.getAngle());
  }

  /**
   * Enables the Drive PID
   */
  public void enableDrivePID(){
    isDrivePIDEnabled = true;
  }
  /**
   * Disables the drive PID
   */
  public void disableDrivePID(){
    isDrivePIDEnabled = false;
  }
  /**
   * gets the angle of the NavX
   * @return the angle of the NavX
   */
  public double getAngle(){
    return navX.getAngle();
  }
  /**
   * Resets the NavX
   */
  public void navXReset(){
    navX.reset();
  }
  /**
   * Sets the speed that the motors are going
   * @param left left motor speed
   * @param right right motor speed
   */
  public void setSpeed(double left, double right){
    mLeftMaster.set(-left);
    mRightMaster.set(right);
  }
  
  /** 
   * @return double
   */
  public double encoderPosition(){
    return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
  }
  /**
   * Resets the encoders and the current distance wew are going
   */
  public void encoderReset(){
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }
}
