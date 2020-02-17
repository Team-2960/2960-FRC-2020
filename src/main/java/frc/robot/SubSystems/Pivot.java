package frc.robot.SubSystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.Util.Trapezoid;
import frc.robot.Camera.*;

public class Pivot extends SubsystemBase{
    public static Pivot pivot;
    private Camera camera = Camera.get_Instance();
    //Pivot motor
    private CANSparkMax mLeftPivot;
    private CANSparkMax mRightPivot;

    //Pivot PID controler
    private PIDController aPidController;
    private ArmFeedforward armfeedforward;

    private boolean isPivotEnabled = false;

    private Trapezoid trapezoid;  
    private double pivotTarget;

    private boolean isFront = true;
    //encoder
    private Encoder pEncoder;
    private DutyCycleEncoder pabsEncoder;
    /** 
     * @return Pivot
     */
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
        pEncoder = new Encoder(Constants.pEncoder1, Constants.pEncoder2, false, CounterBase.EncodingType.k4X);
        pEncoder.setMaxPeriod(.1);
        pEncoder.reset();

        pabsEncoder = new DutyCycleEncoder(0);

        //Arm PID Setup
        aPidController = new PIDController(Constants.pKp, Constants.pKi, Constants.pKd);
        armfeedforward = new ArmFeedforward(Constants.pKs, Constants.pKcos, Constants.pKv, Constants.pKa);

        mRightPivot.setInverted(true);
    }
    /**
     * Set the motor speed
     * @param speed set speed
     */
    public void SetPivotSpeed(double speed){
      mLeftPivot.set(speed);
      mRightPivot.set(speed);
    }
    /**
     * set the motor voltage
     * @param Voltage set voltage
     */
    public void SetPivotVoltage(double Voltage){
        mLeftPivot.setVoltage(Voltage);
        mRightPivot.setVoltage(Voltage);
    }
    /**
     * set the motor rate with pid
     * @param rate target rate
     */
    public void SetPivotPIDRate(double rate){
        double pid_output = aPidController.calculate(pEncoder.getRate(), rate);
        double feedforward = armfeedforward.calculate(pabsEncoder.getDistance(), 0) / RobotController.getBatteryVoltage();
        double speed = pid_output + feedforward;
        SetPivotSpeed(speed);
    }
    /**
     * set the target
     * @param target set target
     */
    public void setPTargetAngle(double target){
      EnablePivotPID();
      pivotTarget = target;
      trapezoid = new Trapezoid(1, 325, -100, -2000, 2000, pabsEncoder.getDistance(), target, pEncoder.getRate(), -200, -200);
    }
  
    /**
     * go to target angle
     * @param angle
     */
    private void gotoAngle(){
      double rate = trapezoid.trapezoidCalc(pabsEncoder.getDistance());
      SetPivotPIDRate(rate);
    }
    
    public boolean atPivotTarget(){
      double error = pEncoder.getDistance() - pivotTarget;
      return error < Constants.angleTolerance;
    }

    public void smartdashboard(){

    }
    
    /**
     * run every time
     */
    public void periodic() {
      // This method will be called once per scheduler run
      //enable pivot PID
      if(isPivotEnabled){
          gotoAngle();
      }
      if(!camera.isTargetFound() || !(pabsEncoder.getDistance() < 100 || pabsEncoder.getDistance() > 180)){
        //
      }else{
        //pivot to target
      }
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
    pivotTarget = 0;
    SetPivotSpeed(0);
  }

  public void isFront(){
    isFront = true;
  }
  public void isBack(){
    isFront = false;
  }
}