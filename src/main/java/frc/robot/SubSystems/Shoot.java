package frc.robot.SubSystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class Shoot extends SubsystemBase{
    static Shoot shoot;

    static Shoot get_Instance(){
      if(shoot == null){
        shoot = new Shoot();
      } 
      return shoot;
    }
    
    private Shoot(){
    }
}