
package org.usfirst.frc.team4068.robot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.usfirst.frc.team4068.robot.subsystems.BallCollector;
import org.usfirst.frc.team4068.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4068.robot.subsystems.Pipeline;
import org.usfirst.frc.team4068.robot.subsystems.Launcher;
import org.usfirst.frc.team4068.robot.subsystems.Sonar;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Rect;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	
	private VisionThread visionThread;
	public double centerX = 0.0;
	private RobotDrive drive;
	
	private final Object imgLock = new Object();
	
	
	
	Joystick driveStick = new Joystick(1);
	Joystick launchStick = new Joystick(2);
	
	Launcher mainLauncher = new Launcher();
	DriveTrain mainDrive = new DriveTrain();
	BallCollector collector = new BallCollector();
	
	Pipeline pipeline = new Pipeline();
	
	
	Sonar sonar = new Sonar();
	
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    static double currentVoltage = 12;
    static Object voltage_rw_Lock = new Object();
    String autoSelected;
    SendableChooser chooser;
 
    Talon climber1 = new Talon(7);
    Talon climber2 = new Talon(11);
    Talon ballSpinner = new Talon(8);
    

	PowerDistributionPanel pdp = new PowerDistributionPanel();
    
    ByteArrayOutputStream autoStreamRecorder;
    boolean autoProgramWritten;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	pdp.clearStickyFaults();
    	SmartDashboard.putBoolean("Load Auto From File", true);
    	new Thread(){
    		public void run(){
    			while (Robot.this.isAutonomous()&&Robot.this.isEnabled()){
    				double readVoltage = Robot.this.m_ds.getBatteryVoltage();
    				synchronized(voltage_rw_Lock){
    					currentVoltage = readVoltage;
    				}
    			}
    		}
    	}.start();
    	
    	climber1.setInverted(true);
    	climber2.setInverted(true);
    	//UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    	//camera.setResolution(640, 480);
    	/*
    	visionThread = new VisionThread(camera, new Pipeline(), new VisionRunner.Listener<Pipeline>() {
    		@Override
    		public void copyPipelineOutputs(Pipeline pipeline) {
	            if (!pipeline.filterContoursOutput().isEmpty()) {
	                Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
	                synchronized (imgLock) {
	                    centerX = r.x + (r.width / 2);
	                }
	            }
    		}
        });
    	*/
    	autoStreamRecorder = new ByteArrayOutputStream();
    	/*
    	visionThread.start();
    	*/
    	//ultra.setAutomaticMode(true);
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
    }
    
    public static double getCurrentVoltage(){
    	synchronized(voltage_rw_Lock){
    		return currentVoltage;
    	}
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	
    	boolean fileAuto = SmartDashboard.getBoolean("Load Auto From File");
    	
    	try {
    		ByteArrayInputStream autoStreamPlayback;
    		if (fileAuto){
    			Path path = Paths.get("/home/lvuser/Auto1.auto");
    			byte[] data = Files.readAllBytes(path);
    			autoStreamPlayback = new ByteArrayInputStream(data);
        	}else{
        		autoStreamPlayback = new ByteArrayInputStream(autoStreamRecorder.toByteArray());
        	}
	    	
	    	byte[] doubleBuffer = new byte[8];
	    	while (autoStreamPlayback.available() >= 24){
	    		autoStreamPlayback.read(doubleBuffer);
	    		double x = ByteBuffer.wrap(doubleBuffer).getDouble();
	    		autoStreamPlayback.read(doubleBuffer);
	    		double y = ByteBuffer.wrap(doubleBuffer).getDouble();
	    		autoStreamPlayback.read(doubleBuffer);
	    		double r = ByteBuffer.wrap(doubleBuffer).getDouble();
	    		
	    		mainDrive.drive((Math.abs(x)>.2)?x:0, (Math.abs(y)>.2)?y:0, (Math.abs(r)>.1)?r:0);
	    		
	    		
	    		autoStreamPlayback.read(doubleBuffer);
	    		//LSB of the double is a boolean for if we should run the launcher
	    		boolean runLauncher = (doubleBuffer[7] & 0x01) == 0x01;
	    		
	    		if (runLauncher){
	        		mainLauncher.start();
	        		ballSpinner.set(.7);
	        	} else {
	        		mainLauncher.stop();
	        		ballSpinner.set(0);
	        	}
	    		
	    		double voltage_recorded = ByteBuffer.wrap(doubleBuffer).getDouble();
	    		double voltage_now = this.m_ds.getBatteryVoltage();
	    		int ms_delay = (int) Math.round(20f * (voltage_recorded/voltage_now));
	    		Thread.sleep(ms_delay);
	    	}
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	
    	//set everything to 0 after auto has finished, so that it doesn't run anymore
    	mainDrive.drive(0, 0, 0);
    	mainLauncher.stop();
    	ballSpinner.set(0);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//Rope Climber
    	double climberSpeed = (-(-driveStick.getRawAxis(3) + 1) / 2) * ((launchStick.getRawButton(6))?-1:1); //convert the range from [-1, 1] to [0, 1]
    	climber1.set(climberSpeed);
    	climber2.set(climberSpeed);
    	
    	
    	//Drive Train
    	double exp = 1.0;
    	
    	double x = -driveStick.getAxis(Joystick.AxisType.kTwist);
    	double y = -driveStick.getAxis(Joystick.AxisType.kY);
    	double r = driveStick.getAxis(Joystick.AxisType.kX);
    	
    	if (driveStick.getRawButton(2)){ //button 2 switches drive controls for the left side to act as the front
	    	double x_tmp = Math.signum(x) * Math.pow(Math.abs(x), exp);
	    	double y_tmp = Math.signum(y) * Math.pow(Math.abs(y), exp);
	    	double r_tmp = Math.signum(r) * Math.pow(Math.abs(r), exp);
	    	x = y_tmp;
	    	y = x_tmp;
	    	r = r_tmp;
    	}else{
    		x = Math.signum(x) * Math.pow(Math.abs(x), exp);
	    	y = Math.signum(y) * Math.pow(Math.abs(y), exp);
	    	r = Math.signum(r) * Math.pow(Math.abs(r), exp);
    	}
    	
    	
    	mainDrive.drive((Math.abs(x)>.2)?x:0, (Math.abs(y)>.2)?y:0, (Math.abs(r)>.1)?r:0);
    	
    	//Launcher
    	//Checks if the xbox right trigger has been pressed
    	boolean rightTriggerPressed = launchStick.getRawAxis(3) > .5;
    	if (rightTriggerPressed){
    		mainLauncher.start();
    	} else {
    		mainLauncher.stop();
    	}
    	
    	
    	//Ball Collector
    	boolean leftTriggerPressed = launchStick.getRawAxis(2) > .5;
    	if (leftTriggerPressed) {
    		collector.spin(.7);
    	} else {
    		collector.stop();
    	}
    	
    	
    	//Basket Spinner
    	ballSpinner.set(launchStick.getRawAxis(0));
    	
    	
    	//Vision Auto-aim
    	boolean aButtonPressed = launchStick.getRawButton(1);
    	if (aButtonPressed) {
    		if (centerX < 305 ) {
    			mainDrive.drive(0, 0, .5);
    		} else if (centerX > 335){
    			mainDrive.drive(0, 0, -.5);
    		} else {
    			if (sonar.getDistancemm() < 2743.2) {
    				mainDrive.drive(0, -.5, 0);
    			} else if (sonar.getDistancemm() > 3352.8) {
    				mainDrive.drive(0, .5, 0);
    			}
    		}
    		
    		mainDrive.drive(0, 0, 0);
    			
    	}
    	
    	try {
	    	if (launchStick.getRawButton(3)){
	    		byte[] bytes = new byte[8];
	    	    ByteBuffer.wrap(bytes).putDouble(x);
	    		this.autoStreamRecorder.write(bytes);
	    		ByteBuffer.wrap(bytes).putDouble(y);
	    		this.autoStreamRecorder.write(bytes);
	    		ByteBuffer.wrap(bytes).putDouble(r);
	    		this.autoStreamRecorder.write(bytes);
	    		
	    		ByteBuffer.wrap(bytes).putDouble(getCurrentVoltage());
	    		if (rightTriggerPressed){
	    			bytes[7] |= 1 << 0;
	    		}else{
	    			bytes[7]  &= ~(1 << 0);
	    		}
	    		this.autoStreamRecorder.write(bytes);
	    	}
	    	
	    	if (launchStick.getRawButton(4)){
	    		File f = new File("/home/lvuser/Auto1.auto");
	    		if(!f.exists()){
	    			f.createNewFile();
	    		}
	    		FileOutputStream fos = new FileOutputStream(f);
	    		fos.write(autoStreamRecorder.toByteArray());
	    		fos.close();
	    	}
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    
}
