package frc.robot.Auton.Commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.SubSystems.Drive;

public class gotoAngle extends CommandGroup{
    //go to a angle;
    
    private boolean isFinish = false;
    private double angle;

    public gotoAngle(double angle){
        this.angle = angle;
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    /**
     * Starts up the command. Gets the command ready to start. <p> Note that the command will
     * eventually start, however it will not necessarily do so immediately, and may in fact be
     * canceled before initialize is even called. </p>
     *
     *
     */
    @Override
    public synchronized void start() {
        super.start();
    }


    /**
     * Returns true if all the commands in this group have been started and have
     * finished.
     * <p>
     * <p> Teams may override this method, although they should probably reference super.isFinished()
     * if they do. </p>
     *
     * @return whether this {@link CommandGroup} is finished
     */
    @Override
    protected boolean isFinished() {
        if(isFinish)
            return true;
        else
            return false;
    }



    @Override
    protected void execute() {
        isFinish = drive.gotoAngle(angle);
    }

    @Override
    protected void end() {
        drive.setSpeed(0,0);
        //WILL CHANGE TO THIS NAME LATER
    }



    /**
     * Returns whether or not the command is running. This may return true even if the command has
     * just been canceled, as it may not have yet called
     *
     * @return whether or not the command is running
     */
    @Override
    public synchronized boolean isRunning() {
        return super.isRunning();
    }
}