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

  //current Angle
  public double currentAngle = 0.0;

  //Gyro Sensor
  private AnalogGyro gyro;
  private AHRS navX;
  //Encoders
  private Encoder rightEncoder;
  private Encoder leftEncoder;
  private double currentDistance = 0;
  public double distance;
  public double angle;
  public double forwardSpeed;
  public int PIDCheck = 0;
  public boolean enableDrivePID = false;
  
  private DigitalInput photoeye;
  
  public static Drive get_Instance(){
    
    if(drive == null){
      drive = new Drive();
    } 
    return drive;
  }

  private Drive() {
    //init code
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

    gyro = new AnalogGyro(0);
    navX = new AHRS(SPI.Port.kMXP);
    rightEncoder = new Encoder(3, 4, false, EncodingType.k4X);
    leftEncoder = new Encoder(0, 1, false, EncodingType.k4X);

    gyro.calibrate();

    currentAngle = navX.getAngle();

    drivePidController = new PIDController(Constants.dKp, Constants.dKi, Constants.dKd);
  }
  public void enableDrivePIDF(){
    enableDrivePID = true;
  }
  
  public double navXAngle(){
    double angle = navX.getAngle();
    return angle;
  }
  public boolean checkDistance(){
    boolean isAtDistance;
    if(currentDistance < (distance + Constants.distanceTolerance) && currentDistance > (distance - Constants.distanceTolerance)){
      isAtDistance = true;
    }
    else{
      isAtDistance = false;
    }
    return isAtDistance;
  }
  public boolean checkAngle(){
    boolean isAtAngle;
    if((currentAngle < (angle + Constants.angleTolerance) && currentAngle > (angle - Constants.angleTolerance)) && Math.abs(navX.getRawGyroZ()) < 1){
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

  //rate drive
  public void setDriveRate(double rate){
    double speed = drivePidController.calculate(navX.getRawGyroZ(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed, speed);
  }
  public void encoderReset(){
    leftEncoder.reset();
    rightEncoder.reset();
    currentDistance = 0;
  }
  public void giveNums(double forwardSpeed, double angle, double distance){
    this.distance = distance;
    this.angle = angle;
    this.forwardSpeed = forwardSpeed;
    leftEncoder.reset();
    rightEncoder.reset();
    currentDistance = 0;
    navXReset();
  }
  public void setDriveAuton(double forwardSpeed, double angle, double distance){
    int negative;
    if(distance < 0){
      negative = -1;
    }
    else{
      negative = 1;
    }
    if(Math.abs(currentDistance) < Math.abs(distance)){
      if(Math.abs(distance) - Math.abs(currentDistance) > 24){
    currentDistance = Constants.DisPerPulse * ((leftEncoder.get() + -1 * rightEncoder.get())/2);
    setDriveToAngle(angle, (forwardSpeed));
    }
    else if(Math.abs(distance) - Math.abs(currentDistance) > 4){
      currentDistance = Constants.DisPerPulse * ((leftEncoder.get() + -1 * rightEncoder.get())/2);
      setDriveToAngle(angle,negative * -0.1);
    }
    else{
      setDriveToAngle(angle, negative * -0.05);
      currentDistance = Constants.DisPerPulse * ((leftEncoder.get() + -1 * rightEncoder.get())/2);
    }
  }
    else{
      setDriveRate(0);
    }
  }
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
  //Arc drive pid
  public void setArcDriveRate(double rate, double forwardSpeed){
    double speed = drivePidController.calculate(navX.getRawGyroZ(), rate); //calc the speed
    SmartDashboard.putNumber("speed", speed);
    setSpeed(-speed + forwardSpeed, speed + forwardSpeed);
  }

  //set lefe and right motor speed.
  public void setSpeed(double left, double right){
    SmartDashboard.putNumber("left motor value", left);
    SmartDashboard.putNumber("right motor value", right);
    mLeftMaster.set(left);
    mRightMaster.set(-right);
  }
  public void navXReset(){
    navX.reset();
  }
  @Override
  // This method will be called once per scheduler run
  public void periodic() {
    if(enableDrivePID){
     
    if(PIDCheck == 1){
      drive.setDriveAuton(forwardSpeed, angle, distance);
    if(checkDistance()){
      enableDrivePID = false;
    }
    }
    else if(PIDCheck == 2){
      setDriveToAngle(angle, 0);
    if(checkAngle()){
      enableDrivePID = false;
    }
    }
    
    else if(PIDCheck == 3){
      drive.setDriveAuton(forwardSpeed, angle, distance);
      if(checkAngle() && checkDistance()){
        enableDrivePID = false;
      }
    }
  }
    currentAngle = navX.getAngle();
    SmartDashboard.putNumber("Gyro Angle", navX.getAngle());
    SmartDashboard.putNumber("left Encoder", rightEncoder.get());
    SmartDashboard.putNumber("calc encoder", currentDistance);

  }
}
