package frc.robot.Auto.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SubSystems.*;

public class Shoot extends CommandBase{
    //shoot the ball
    
    MEGAShooter mShooter = MEGAShooter.get_Instance();
    Index index = Index.get_Instance();
    Shooter shooter = Shooter.get_Instance();
    private boolean isFinish = false;
    private double rate;

    public Shoot(double rate){
        this.rate = rate;
    }

    @Override
    public void initialize() {
        super.initialize();
        
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
        isFinish = index.indexBeltsGoneDistance(57);
        mShooter.shootAlways(rate);
    }

    
    /** 
     * @param interrupt
     */
    @Override
    public void end(boolean interrupt) {
        shooter.setShooterSpeed(0, 0);
        index.disableIndex();
    }
}