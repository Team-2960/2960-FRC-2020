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
    public boolean cameraTrackingEnabled =false;
    private Trapezoid trapezoid;  
    private double pivotTarget;
    public static boolean isPivotFront;
    public static int lookUpPos;
    public int pivotTablePos = 0;
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
        pabsEncoder.setDistancePerRation(360);
        //Arm PID Setup
        aPidController = new PIDController(Constants.pKp, Constants.pKi, Constants.pKd);
        armfeedforward = new ArmFeedforward(Constants.pKs, Constants.pKcos, Constants.pKv, Constants.pKa);

        mRightPivot.setInverted(true);
    }
    public void setpivotDirection(boolean front){
      isPivotFront = front;
      if(front){
        lookUpPos = 1;
      }
      else{
        lookUpPos = 2;
      }
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
     * set the motor rate with pid
     * @param rate target rate
     */
    private void SetPivotPIDRate(double rate){
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
      if(pivotTarget != target){
        pivotTarget = target;
        trapezoid = new Trapezoid(1, 325, -100, -2000, 2000, pabsEncoder.getDistance(), target, pEncoder.getRate(), -200, -200);
      }
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
    public boolean pivotInWindow(){
      boolean isInWindow = false;
      if(isPivotFront){
        if(pabsEncoder.getDistance() > Constants.frontWindowMin && pabsEncoder.getDistance() < Constants.frontWindowMax){
          isInWindow = true;
        }
      }
      else{
        if(pabsEncoder.getDistance() > Constants.backWindowMin && pabsEncoder.getDistance() < Constants.backWindowMax){
          isInWindow = true;
        }
      }
      return isInWindow;
    }
    public double frontOrBack(){
      double neuturalPos;
      if(isPivotFront){
        neuturalPos = Constants.neuturalPosFront;
      }
      else{
        neuturalPos = Constants.neuturalPosBack;
      }
      return neuturalPos;
    }
    public void pivotToTarget(int pos){
      if(cameraTrackingEnabled){
        if(!pivotInWindow() || !camera.isTargetFound()){
            setPTargetAngle(frontOrBack());
        }    
        
        else{
          setPTargetAngle(Constants.pivotTable[pos][lookUpPos]);
        }
      }
    }
    public void smartdashboard(){
      SmartDashboard.putNumber("Encoder Value Rate", pEncoder.getRate());
      SmartDashboard.putNumber("ABS Encoder Value Degrees", pabsEncoder.getDistance());
    }
    
    /**
     * run every time
     */
    public void periodic() {
      // This method will be called once per scheduler run
      smartdashboard();
      //enable pivot PID
      double distance = camera.getTargetDistance();
      if(cameraTrackingEnabled){
        pivotTablePos = 0;
        while(distance > Constants.pivotTable[pivotTablePos][0] && Constants.pivotTable[pivotTablePos][0] < Constants.pivotTable.length){
          pivotTablePos++; 
        }
        double under = distance - Constants.pivotTable[pivotTablePos][0];
        double above = 0;
        try{
          above = distance - Constants.pivotTable[pivotTablePos + 1][0];
        }
        catch(Exception e){
          above = under;
        }
        
        if(above< under){
          pivotTablePos = pivotTablePos + 1;
        }
        pivotToTarget(pivotTablePos);
      }
  
      
      if(isPivotEnabled){
          gotoAngle();
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

}