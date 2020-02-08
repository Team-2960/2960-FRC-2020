package frc.robot.SubSystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DigitalInput;
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
  private Encoder rightEncoder;
  private Encoder leftEncoder;
  private double currentDistance = 0;
  public double TargetDistance;
  public double TargetAngle;
  public double forwardSpeed;
  public int PIDCheck = 0;
  public boolean isDrivePIDEnabled = false;
  private Camera camera;
  public double cameraAngle = 0;
  
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    //init code
    camera = new Camera(0);
    //init all the motors
    mLeftMaster = new CANSparkMax(Constants.mLeftMaster, MotorType.kBrushless);
    mLeftFollow1 = new CANSparkMax(Constants.mLeftFollow1, MotorType.kBrushless);
    mLeftFollow2 = new CANSparkMax(Constants.mLeftFollow2, MotorType.kBrushless);

    mRightMaster = new CANSparkMax(Constants.mRightMaster, MotorType.kBrushless);
    mRightMfollow1 = new CANSparkMax(Constants.mRightMfollow1, MotorType.kBrushless);
    mRightMfollow2 = new CANSparkMax(Constants.mRightMfollow2, MotorType.kBrushless);

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
    rightEncoder = new Encoder(3, 4, false, EncodingType.k4X);
    leftEncoder = new Encoder(0, 1, false, EncodingType.k4X);

    //init drive PID
    drivePidController = new PIDController(Constants.dKp, Constants.dKi, Constants.dKd);
  }


  /**
   * Checks to see if we are at the target distance
   * @return true if we are at the target distance
   */
  public boolean checkDistance(){
    boolean isAtDistance;
    if(currentDistance < (TargetDistance + Constants.distanceTolerance) && currentDistance > (TargetDistance - Constants.distanceTolerance)){
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
    if((getAngle() < (TargetAngle + Constants.angleTolerance) && getAngle() > (TargetAngle - Constants.angleTolerance)) && Math.abs(navX.getRawGyroZ()) < 1){
      isAtAngle = true;
    }
    else{
      isAtAngle = false;
    }
    return isAtAngle;
  }
  public double rate(double previousRate, double currentRate){
    double rate;
    rate = currentRate - previousRate;
    return rate;
  }

  /**
   * Use the PID to set the rate we are going
   * @param rate is the target angle rate we want to be going
   */
  public void setDriveRate(double rate){
    double speed = drivePidController.calculate(navX.getRawGyroZ(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed, speed);
  }
  /**
   * Resets the encoders and the current distance wew are going
   */
  public void encoderReset(){
    leftEncoder.reset();
    rightEncoder.reset();
    currentDistance = 0;
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
    int negative;
    if(distance < 0){
      negative = -1;
    }
    else{
      negative = 1;
    }
    if(Math.abs(currentDistance) < Math.abs(distance)){
      currentDistance = getDistanceInches((leftEncoder.get() + -1 * rightEncoder.get())/2);
      if(Math.abs(distance) - Math.abs(currentDistance) > 24){
        setDriveToAngle(angle, (forwardSpeed));
      }
      else if(Math.abs(distance) - Math.abs(currentDistance) > 4){
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
    double speed = drivePidController.calculate(navX.getRawGyroZ(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed + forwardSpeed, speed + forwardSpeed);
  }

  /**
   * Sets the speed that the motors are going
   * @param left left motor speed
   * @param right right motor speed
   */
  public void setSpeed(double left, double right){
    SmartDashboard.putNumber("left motor value", left);
    SmartDashboard.putNumber("right motor value", right);
    mLeftMaster.set(left);
    mRightMaster.set(-right);
  }
  public void adjustToTarget(){
    startGoToAngleDistance(0, cameraAngle, 0, 2);
  }
  /**
   * Resets the NavX
   */
  public void navXReset(){
    navX.reset();
  }
  @Override
  // This method will be called once per scheduler run
  public void periodic() {
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
    SmartDashboard.putNumber("Gyro Angle", navX.getAngle());
    SmartDashboard.putNumber("left Encoder", rightEncoder.get());
    SmartDashboard.putNumber("calc encoder", currentDistance);
    cameraAngle = (camera.calcAngle(camera.getCenterX()) +  navX.getAngle());
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
     * Gets the distance we have traveled since last encoder Reset
     * @return the encoder Distance in inches
     */
    public double getDistanceInches(double encoderVal){
      return Constants.DisPerPulse * encoderVal;
    }
}
