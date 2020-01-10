package frc.robot.Camera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
public class Camera{
    public UsbCamera camera;
    private int IMG_WIDTH = 320;
    private int IMG_HEIGHT = 160;

	
	private double visionTarget; 
	private Object IMG_LOCK;


	private CvSink cam_sink;
	private Thread visionThread;

	private CvSource hsv_threashold_source;
	private CvSource erode_source;
    
    public Camera( int cameraPort){
        camera = CameraServer.getInstance().startAutomaticCapture(cameraPort);
        hsv_threashold_source = CameraServer.getInstance().putVideo("HSV Threshold", IMG_WIDTH, IMG_HEIGHT);
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
        IMG_LOCK = new Object();
        cam_sink = CameraServer.getInstance().getVideo();

        startThread();
    }
    private void startThread(){
	
		visionThread = new Thread(() -> {
			GripPipeline pipeline = new GripPipeline();
			Mat cam_frame = new Mat();
			Boolean lTargetFound;
			Double lVisionTarget;
			
			//Run this code as long as the thread is not interrupted
			while(!Thread.interrupted()){
				//reset target varibles
				lTargetFound = false;
				lVisionTarget = 0.0;

				//Grab image from camera
				long result = cam_sink.grabFrameNoTimeout(cam_frame);
				
				//Check whether we received an image
				if(result == 0){
					//System.out.println(cam_sink.getError());
					lTargetFound = false;
				}else{
					//Use grip code to process image
					pipeline.process(cam_frame);
					
					//Find countors in image
					if (!pipeline.filterContoursOutput().isEmpty()){


					}
					synchronized(IMG_LOCK){
						visionTarget = lVisionTarget;
					}
					//Output to smartdash board - It may not like having this inside the thread
					hsv_threashold_source.putFrame(pipeline.hsvThresholdOutput());
					erode_source.putFrame(pipeline.cvErodeOutput());
				}
			}
        }
        );
		visionThread.start();
	
    }
    public double getImageResults() {
		//Get results from vision thread -- This will change. 
		synchronized(IMG_LOCK){
			return visionTarget;
		}
	}
}