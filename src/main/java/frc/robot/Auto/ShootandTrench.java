package frc.robot.Auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Auto.Commands.*;

public class ShootandTrench extends CommandGroup{
    public ShootandTrench(){
        addSequential(new AdjustToTarget());
        //shoot not done yet

        addSequential(new gotoAngle(0));
        addSequential(new MoveDistance(-285, 0, 0.2));

        //parallel intake not done yet
        addSequential(new AdjustToTarget());
        //shoot not done yet


        
    }
}