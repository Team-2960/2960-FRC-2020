package frc.robot.Camera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
public class Camera{
    public UsbCamera camera;
    public Camera( int cameraPort){
        camera = CameraServer.getInstance().startAutomaticCapture(cameraPort);
    }

}