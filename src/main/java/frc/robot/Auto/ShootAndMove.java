package frc.robot.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Auto.Commands.*;
import frc.robot.SubSystems.MEGAShooter.ShooterMode;

public class ShootAndMove extends SequentialCommandGroup{
    public ShootAndMove(){
        addCommands(
            new pGotoAngle(ShooterMode.auton_Mode),
            new Shoot(),
            new DriveWithTime(0.25,0.25, 0.5)
        );

    }
}