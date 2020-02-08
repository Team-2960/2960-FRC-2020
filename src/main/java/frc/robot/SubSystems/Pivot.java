package frc.robot.SubSystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;

public class Pivot extends SubsystemBase{
    public static Pivot pivot;
    //Pivot motor
    private CANSparkMax mLeftPivot;
    private CANSparkMax mRightPivot;

    //Pivot PID controler
    private PIDController aPidController;
    private ArmFeedforward armfeedforward;

    TrapezoidProfile trapezoidProfile;

    private CANEncoder EArm;  
    private Encoder eArm;

    
    private boolean isPivotEnabled = false;
    private double pTargetPivot;


    private Timer trapezidTimer;

    public static Pivot get_Instance(){
        if(pivot == null){
            pivot = new Pivot();
        } 
        return pivot;
    }
    private Pivot(){
        //init code
        mLeftPivot = new CANSparkMax(Constants.mLeftPivot, MotorType.kBrushless);
        mRightPivot = new CANSparkMax(Constants.mRightPivot, MotorType.kBrushless);
  
        //encoder 
        eArm = new Encoder(4, 5, true, Encoder.EncodingType.k4X);
      
        //Arm PID Setup
        eArm.reset();
        eArm.setMaxPeriod(.1);
        eArm.setMinRate(10);
        eArm.setSamplesToAverage(7);
        eArm.setDistancePerPulse(360.0/1024.0);
        aPidController = new PIDController(Constants.pKp, Constants.pKi, Constants.pKd);
        armfeedforward = new ArmFeedforward(Constants.pKs, Constants.pKcos, Constants.pKv, Constants.pKa);

        trapezidTimer = new Timer();
        
        EArm = new CANEncoder(mLeftPivot);

        mRightPivot.setInverted(true);

        

    }
    /**
     * Set the motor speed
     * @param speed
     */
    public void SetPivotSpeed(double speed){
      mLeftPivot.set(speed);
      mRightPivot.set(speed);
    }
    /**
     * set the motor voltage
     * @param Voltage
     */
    public void SetPivotVoltage(double Voltage){
        mLeftPivot.setVoltage(Voltage);
        mRightPivot.setVoltage(Voltage);
    }
    /**
     * set the motor rate with pid
     * @param rate
     */
    public void SetPivotPIDRate(double rate){
        double pid_output = aPidController.calculate(EArm.getVelocity(), rate);
        double feedforward = armfeedforward.calculate(EArm.getPosition(), 0) / RobotController.getBatteryVoltage();
        double speed = pid_output + feedforward;
        SmartDashboard.putNumber("pidout", pid_output);
        SmartDashboard.putNumber("feed", feedforward);
        SetPivotSpeed(speed);
    }
    /**
     * set the target
     * @param target
     */
    public void setPTargetAngle(double target){
      pTargetPivot = target;
    }
  
    /**
     * go to target angle
     * @param angle
     */
    private void gotoAngle(double Angle){

    }
  
    /**
     * enable the pivot pid
     */
    public void EnablePivotPID(){
        isPivotEnabled = true;
    }
    /**
     * disable the pivot pid
     */
    public void DisablePivotPID(){
      isPivotEnabled = false;
      pTargetPivot = 0;
      SetPivotSpeed(0);
    }
    public void smartdashboard(){
      SmartDashboard.putNumber("Encoder ", eArm.getDistance());
      SmartDashboard.putNumber("Encoder rate ", EArm.getVelocity());
      SmartDashboard.putNumber("mSpeed ", mLeftPivot.get());
      SmartDashboard.putNumber("Error ", eArm.getDistance() - -40);
    }
    
    /**
     * run every time
     */
    public void periodic() {
      // This method will be called once per scheduler run
      //enable pivot PID
      if(isPivotEnabled){
        gotoAngle(pTargetPivot);
      }
      else{
        pTargetPivot = 0;
      }
    }
}