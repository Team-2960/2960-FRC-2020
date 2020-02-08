package frc.robot.Camera;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import edu.wpi.cscore.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.Timer;
public class Camera{
	//init code
	public UsbCamera camera;
	private CvSink cam_sink;
	private Thread visionThread;
	private Timer timer;

	//getting the center X and Y
	private double centerX;
	private double centerY;

	private Boolean targetFound;

	private Object IMG_LOCK;

	private CvSource hsv_threashold_source;
	private CvSource erode_source;

	//the width and the height of the vision type
	private double  width = 0.0;
	public double height = 0.0;

	//may be keep
	public double turningSpeed = 0.0;
	public double targetRatio = 0.0;
	
	/**
	 * Constructor
	 * @param cameraPort
	 */
	public Camera(int cameraPort){
		//init camera
		timer = new Timer();
		camera = CameraServer.getInstance().startAutomaticCapture(cameraPort);
		if(camera != null){
			//send to smartdash board
			camera.setResolution(Constants.cWidth, Constants.cHeight);
			camera.setFPS(30);
			cam_sink = CameraServer.getInstance().getVideo();
			hsv_threashold_source = CameraServer.getInstance().putVideo("HSV Threshold", Constants.cWidth, Constants.cWidth);
			erode_source = CameraServer.getInstance().putVideo("Erode", Constants.cWidth, Constants.cWidth);
		}

		IMG_LOCK = new Object();
		startThread();
	}

	/**
	 * calculate the distance away from the target
	 * @param pixels
	 * @return distance away from the target
	 */
	public double distanceCalc(double pixels){
		return (15.75 * (double) Constants.cHeight)/(2.0 * pixels * Math.tan(Math.toRadians(Constants.verticalViewAngle)));
	}

	/**
	 * calculate the angle away from the target
	 * @param pixels
	 * @return angle away from the target
	 */
	public double calcAngle(double pixels){
		double angle;
		int negative;
		if(getCenterX() > Constants.cWidth/2){
			negative = 1;
		}
		else{
			negative = -1;
		}
		angle = negative * (Math.abs((double) pixels - Constants.cWidth/2) * (Constants.deg_per_px));
		return angle;
	}

	/**
	 * Start vision code
	 */
    private void startThread(){
		visionThread = new Thread(() -> {
			timer.start();
			GripPipeline pipeline = new GripPipeline();
			Mat cam_frame = new Mat();
			Boolean lTargetFound;
			double LcenterX;
			double LcenterY;
			double Lwidth = 0.0;
			double Lheight = 0.0;
			double LTurningSpeed = 0.0;
			double LtargetRatio = 0.0;

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
						
						Rect tempRec = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
						//System.out.println("Target: " + count + " x: " + tempRec.x + " Angle: " + tempAngle.angle);						}
							
						//Did we find a target? Record center value
							LcenterX = (double) tempRec.width/2 + tempRec.x;
							LcenterY = (double) tempRec.height/2 + tempRec.y;
								LtargetRatio = (double) tempRec.height/tempRec.width;

								Lwidth = (double) tempRec.width;
								Lheight = (double) tempRec.height;
								double distancePixels;
								double p = 0.005;
								distancePixels = LcenterX - Constants.cWidth;
								
								LTurningSpeed = (distancePixels * p);
							lTargetFound = true;
					}else{
						
						lTargetFound = false;
					}
					
					synchronized(IMG_LOCK){
						targetFound = lTargetFound;
						centerX = LcenterX;
						centerY = LcenterY;
						width = Lwidth;
						height = Lheight;
						turningSpeed = LTurningSpeed;
						targetRatio = LtargetRatio;

					}
					//Output to smartdash board - It may not like having this inside the thread
					hsv_threashold_source.putFrame(pipeline.hsvThresholdOutput());
					erode_source.putFrame(pipeline.cvErodeOutput());
					SmartDashboard.putNumber("Timer Value", timer.get());
					timer.reset();
				}
			}
        }
        );
		visionThread.start();
	}
	
	/**
	 * return synchronized CenterX of the target
	 * @return CenterX of the target
	 */	
	public double getCenterX() {
		synchronized(IMG_LOCK){
			return centerX;
		}
	}

	/**
	 * return synchronized CenterY of the target
	 * @return CenterY of the target
	 */	
	public double getCenterY() {
		synchronized(IMG_LOCK){
			return centerY;
		}
	}
	/**
	 * return synchronized Width of the target
	 * @return Width of the target
	 */
	public double getTargetWidth(){
		synchronized(IMG_LOCK){
			return width;
		}
	}
	/**
	 * return synchronized height of the target
	 * @return height of the target
	 */
	public double getTargetHeight(){
		synchronized(IMG_LOCK){
			return height;
		}
	}
	/**
	 * return synchronized ratio
	 * @return Ratio
	 */
	public double getImageResultsTurningSpeed(){
		synchronized(IMG_LOCK){
			return turningSpeed;
		}
	}
	/**
	 * return synchronized ratio
	 * @return Ratio
	 */
	public double getImageResultsTargetRatio(){
		synchronized(IMG_LOCK){
			return targetRatio;
		}
	}
	/**
	 * return synchronized is target found or not
	 * @return target found or not
	 */
	public Boolean isTargetFound() {
		synchronized(IMG_LOCK){
			return targetFound;
		}
	}
	/**
	 * put to smartdashboard
	 */
	public void SmartDashboard(){
		SmartDashboard.putNumber("CenterX", getCenterX());
		SmartDashboard.putNumber("CenterY", getCenterY());
        SmartDashboard.putNumber("Constants.hsvThresholdHueMin", Constants.hsvThresholdHue[0]);
        SmartDashboard.putNumber("Constants.hsvThresholdHueMax", Constants.hsvThresholdHue[1]);
        SmartDashboard.putNumber("Constants.hsvThresholdSaturationMin", Constants.hsvThresholdSaturation[0]);
        SmartDashboard.putNumber("Constants.hsvThresholdSaturationMax", Constants.hsvThresholdSaturation[1]);
        SmartDashboard.putNumber("Constants.hsvThresholdValueMin", Constants.hsvThresholdValue[0]);
		SmartDashboard.putNumber("Constants.hsvThresholdValueMax", Constants.hsvThresholdValue[1]);
	}
}