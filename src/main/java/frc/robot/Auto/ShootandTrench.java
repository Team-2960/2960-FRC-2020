package frc.robot.Auto;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShootandTrench extends CommandGroup{
    public ShootandTrench(){
        addSequential(new IntakeAdjustMove(), .1);
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        addSequential(new MoveForwardTime(.1, .5));
        if (gameData.charAt(0) == 'L') {
            addSequential(new MoveForwardDistanceVelocity(125, 102));
            addParallel(new ElevatorMove(Elevator.mElevatorState.Switch, 0), 5);
            addSequential(new TurnToTarget(-90, 120));
            addSequential(new MoveForwardDistanceVelocity(10, 102));
            addSequential(new IntakeAdjustMove(), .75);
            addSequential(new IntakeMove(Intake.mIntakeState.backward), 1.5);
    
        }
        else {
            addSequential(new MoveForwardDistanceVelocity(168, 102));
           }
        addSequential(new MoveForwardDistanceVelocity(15, -30));
        }

}