package frc.robot.Camera;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.Constants;
import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import frc.robot.Constants;
public class Camera{
	public UsbCamera camera;
	private CvSink cam_sink;
	private Thread visionThread;
	private double centerX;
	private double centerY;
	private Boolean targetFound;
	private Object IMG_LOCK;
	private CvSource hsv_threashold_source;
	private CvSource erode_source;
	

    public Camera(int cameraPort){
		System.out.println("startconstructor");
		camera = CameraServer.getInstance().startAutomaticCapture(cameraPort);
		if(camera != null){
			camera.setResolution(Constants.cWidth, Constants.cHeight);
			camera.setFPS(10);
			cam_sink = CameraServer.getInstance().getVideo();
			hsv_threashold_source = CameraServer.getInstance().putVideo("HSV Threshold", Constants.cWidth, Constants.cWidth);
			erode_source = CameraServer.getInstance().putVideo("Erode", Constants.cWidth, Constants.cWidth);
		}

		IMG_LOCK = new Object();
		System.out.println("call start thread");	
		startThread();
	}
	
    private void startThread(){
		visionThread = new Thread(() -> {
			System.out.println("thread started");
			GripPipeline pipeline = new GripPipeline();
			Mat cam_frame = new Mat();
			Boolean lTargetFound;
			Double LcenterX;
			Double LcenterY;
			
			
			
			//Run this code as long as the thread is not interrupted
			while(!Thread.interrupted()){
				//reset target varibles
				lTargetFound = false;
				LcenterX = 0.0;
				LcenterY = 0.0;

				//Grab image from camera
				long result = cam_sink.grabFrameNoTimeout(cam_frame);
				
				//Check whether we received an image
				
				if(result == 0){
					System.out.println("no image found");
					//System.out.println(cam_sink.getError());
					lTargetFound = false;
				}else{
					//Use grip code to process image
					pipeline.process(cam_frame);
					
					//Find countors in image
					if (!pipeline.filterContoursOutput().isEmpty()){
						System.out.println("target found");
							Rect tempRec = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
							//System.out.println("Target: " + count + " x: " + tempRec.x + " Angle: " + tempAngle.angle);						}
							System.out.println("finding center");
						
						
						
				
						//Did we find a target? Record center value
							LcenterX = (double) tempRec.width/2 + tempRec.x;
							LcenterY = (double) tempRec.height/2 + tempRec.y;
							lTargetFound = true;
						

						

					}else{
						System.out.println("target not found");
						lTargetFound = false;
						//System.out.println("No Contours");
					}
					
					//System.out.println("Is target found:" + lTargetFound);
					//Allow main thread to access center
					//System.out.println("Distance from center: " + lVisionTarget);
					synchronized(IMG_LOCK){
						targetFound = lTargetFound;
						centerX = LcenterX;
						centerY = LcenterY;

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
	
	public double getImageResultsX() {
		//Get results from vision thread -- This will change. 
		synchronized(IMG_LOCK){
			return centerX;
			
		}
	}
		
	public double getImageResultsY() {
		//Get results from vision thread -- This will change. 
		synchronized(IMG_LOCK){
			return centerY;
			
		}
	}
	
	public Boolean isImageFound() {
		//Get results from vision thread -- This will change. 
		synchronized(IMG_LOCK){
			return targetFound;
		}
	}
}