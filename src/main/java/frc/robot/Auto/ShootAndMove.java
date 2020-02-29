package frc.robot.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Auto.Commands.*;

public class ShootAndMove extends SequentialCommandGroup{
    public ShootAndMove(){
        new pGotoAngle();
        new Shoot();

        new MoveDistance(40, 0, 0.5);
        new MoveDistance(-285, 0, 0.2);       
    }
}