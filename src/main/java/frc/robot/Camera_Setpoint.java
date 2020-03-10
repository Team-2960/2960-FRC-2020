package frc.robot;

class Camera_Setpoint extends Shooter_Setpoint{
    double distance = 0;
    public Camera_Setpoint(
        double distance,
        double shooter_speed,
        double pivot_setpoint,
        double min_left_speed,
        double max_left_speed,
        double min_right_speed,
        double max_right_speed
    ){
        super(shooter_speed, pivot_setpoint,min_left_speed, max_left_speed, min_right_speed, max_right_speed);
        this.distance = distance;
    }
}