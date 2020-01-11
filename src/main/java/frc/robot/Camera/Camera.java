package frc.robot.Camera;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.Constants;
import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import frc.robot.Constants;
public class Camera{
    public UsbCamera camera;
    
    public Camera(int cameraPort){
		camera = CameraServer.getInstance().startAutomaticCapture(cameraPort);
		if(camera != null)
			camera.setResolution(Constants.cWidth, Constants.cHeight);
    }
    private void startThread(){
	
	
    }
}