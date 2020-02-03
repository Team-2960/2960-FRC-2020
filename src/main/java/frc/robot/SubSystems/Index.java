package frc.robot.SubSystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Index extends SubsystemBase {
  private static Index index;
  private static DigitalInput photoeye;

  public static Index get_Instance(){
    if(index == null){
      index = new Index();
    } 
    return index;
  }
  public boolean checkForBall(){
    return photoeye.get();
  }
  private Index() {
    photoeye = new DigitalInput(0);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("photoeye", photoeye.get());
  }
}
