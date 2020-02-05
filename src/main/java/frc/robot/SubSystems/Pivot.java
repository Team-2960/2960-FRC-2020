package frc.robot.SubSystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.ArmFeedforward;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private CANEncoder EArm;  
    private Encoder eArm;

    
    private boolean isPivotEnabled = false;
    private double pTargetPivot;

    private double startPivotError = 0.0;

    public static Pivot get_Instance(){
        if(pivot == null){
            pivot = new Pivot();
        } 
        return pivot;
    }
    private Pivot(){
        //init code
        //mLeftShooter = new TalonFX(Constants.mLeftShooter);
        //mRightShooter = new TalonFX(Constants.mRightShooter);
  
        mLeftPivot = new CANSparkMax(Constants.mLeftPivot, MotorType.kBrushless);
        mRightPivot = new CANSparkMax(Constants.mRightPivot, MotorType.kBrushless);
  
        //encoder 
        eArm = new Encoder(4, 5, true, Encoder.EncodingType.k4X);
      
        //Arm PID
        eArm.reset();
        eArm.setMaxPeriod(.1);
        eArm.setMinRate(10);
        eArm.setSamplesToAverage(7);
        eArm.setDistancePerPulse(360.0/1024.0);
        aPidController = new PIDController(Constants.pKp, Constants.pKi, Constants.pKd);
        armfeedforward = new ArmFeedforward(Constants.pKs, Constants.pKcos, Constants.pKv, Constants.pKa);

        EArm = new CANEncoder(mLeftPivot);

        mRightPivot.setInverted(true);
    }
    public void SetPivotSpeed(double speed){
        mLeftPivot.set(speed);
        mRightPivot.set(speed);
      }
    public void SetPivotVoltage(double Voltage){
        mLeftPivot.setVoltage(Voltage);
        mRightPivot.setVoltage(Voltage);
    }
    //set Pivot speed
    public void SetPivotPIDRate(double rate){
        double pid_output = aPidController.calculate(EArm.getVelocity(), rate);
        double feedforward = armfeedforward.calculate(EArm.getPosition(), 0) / RobotController.getBatteryVoltage();
        double speed = pid_output + feedforward;
        SmartDashboard.putNumber("pidout", pid_output);
        SmartDashboard.putNumber("feed", feedforward);
        SetPivotSpeed(speed);
    }
    //set target Pivot Pivot
    public void setPTargetAngle(double target){
        pTargetPivot = target;
        startPivotError = eArm.getDistance() - target;
    }
  
      //set Pivot Pivot
      private void setPAngle(double Pivot){
        double error = eArm.getDistance() - Pivot;
        double start = error / startPivotError;
        double scale =0;
        if(start > 0.5){
          scale =20;
        }else{
          scale = 30;
        }
        System.out.println("scale " + scale);
        double set_speed = scale * error;
        SmartDashboard.putNumber("speed set point", set_speed );
        set_speed = MathUtil.clamp(set_speed, -1200, 2000);
  
        SetPivotPIDRate(set_speed);
  
        SmartDashboard.putNumber("speed set point - Clamped", set_speed );
      }
  
        //enable the pivot pid
    public void EnablePivotPID(){
        isPivotEnabled = true;
    }
        //disable the pivot pid
  public void DisablePivotPID(){
    isPivotEnabled = false;
    pTargetPivot = 0;
    SetPivotSpeed(0);
  }
      @Override
    public void periodic() {
      // This method will be called once per scheduler run
      //enable pivot PID
      if(isPivotEnabled){
        setPAngle(pTargetPivot);
      }
      else{
        pTargetPivot = 0;
      }
  
  
      SmartDashboard.putNumber("Encoder ", eArm.getDistance());
      SmartDashboard.putNumber("Encoder rate ", EArm.getVelocity());
      SmartDashboard.putNumber("mSpeed ", mLeftPivot.get());
      SmartDashboard.putNumber("Error ", eArm.getDistance() - -40);
      
    }
}