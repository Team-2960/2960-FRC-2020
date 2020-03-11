package frc.robot.Auto.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubSystems.MEGAShooter;
import frc.robot.SubSystems.Pivot;
import frc.robot.SubSystems.Shooter;

public class pGotoAngle extends CommandBase{
    //this will adjust the shooting angle

    private MEGAShooter megashooter = MEGAShooter.get_Instance();
    private Shooter shooter = Shooter.get_Instance();
    private Pivot pivot = Pivot.get_Instance();

    private boolean isFinish = false;
    private MEGAShooter.ShooterMode mode;

    public pGotoAngle(MEGAShooter.ShooterMode mode){
        this.mode = mode;
    }

    @Override
    public void initialize() {
        super.initialize();
        megashooter.setShooterMode(mode);
    }
    
    /**
     * Returns true if all the commands in this group have been started and have
     * finished.
     * <p>
     * <p>
     * Teams may override this method, although they should probably reference
     * super.isFinished() if they do.
     * </p>
     *
     * @return whether this {@link CommandGroup} is finished
     */
    @Override
    public boolean isFinished() {
        if (isFinish)
            return true;
        else
            return false;
    }



    @Override
    public void execute() {
        isFinish = pivot.atPivotTarget() && shooter.readyToShoot();
    }

    
    /** 
     * @param interrupt
     */
    @Override
    public void end(boolean interrupt) {
        //WILL CHANGE TO THIS NAME LATER
    }
}