package frc.robot;

public class Shooter_Setpoint{
    public double shooter_speed;
    public double pivot_setpoint;
    public double min_left_speed;
    public double max_left_speed;
    public double min_right_speed;
    public double max_right_speed;

    public Shooter_Setpoint(
        double shooter_speed,
        double pivot_setpoint,
        double min_left_speed,
        double max_left_speed,
        double min_right_speed,
        double max_right_speed
    ){
        this.shooter_speed = shooter_speed;
        this.pivot_setpoint = pivot_setpoint;
        this.min_left_speed = min_left_speed;
        this.max_left_speed = max_left_speed;
        this.min_right_speed = min_right_speed; 
        this.max_right_speed = max_right_speed;
    }
}