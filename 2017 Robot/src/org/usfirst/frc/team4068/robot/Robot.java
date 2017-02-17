
package org.usfirst.frc.team4068.robot;

import org.opencv.core.MatOfKeyPoint;
import org.usfirst.frc.team4068.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4068.robot.subsystems.Pipeline;
import org.usfirst.frc.team4068.robot.subsystems.Launcher;
import org.usfirst.frc.team4068.robot.subsystems.Sonar;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.opencv.core.Rect;
import edu.wpi.first.wpilibj.RobotDrive;

import edu.wpi.first.wpilibj.IterativeRobot;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	
	private VisionThread visionThread;
	private double centerX = 0.0;
	private RobotDrive drive;
	
	private final Object imgLock = new Object();
	
	Joystick driveStick = new Joystick(1);
	Joystick launchStick = new Joystick(2);
	
	Launcher mainLauncher = new Launcher();
	DriveTrain mainDrive = new DriveTrain();
	
	Pipeline pipeline = new Pipeline();

	
	//Sonar ultra = new Sonar(6, 7);
	
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
 
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    	camera.setResolution(640, 480);
    	visionThread = new VisionThread(camera, new Pipeline(), pipeline -> {
            if (!pipeline.filterContoursOutput().isEmpty()) {
                Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
                synchronized (imgLock) {
                    centerX = r.x + (r.width / 2);
                }
            }
        });
    	visionThread.start();
    	//ultra.setAutomaticMode(true);
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
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
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	/*switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}*/
    	Timer t1 = new Timer();
    	t1.start();
    	while (t1.get() <= 15000000) {
    		while (t1.get() <= 6000000) {
    			mainDrive.drive(.1, 0, 0);
    		}
    		while (t1.get() > 6000000 && t1.get() <= 8000000) {
    			mainDrive.drive(0, 0, .7);
    		}
    		while (t1.get() > 8000000) {
    			mainDrive.drive(.1, 0, 0);
    		}
    	}
    	mainDrive.drive(0, 0, 0);
    	t1.stop();

        }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	
    	
    	double x = driveStick.getAxis(Joystick.AxisType.kX);
    	double y = driveStick.getAxis(Joystick.AxisType.kY);
    	double r = driveStick.getAxis(Joystick.AxisType.kTwist);
    	
    	mainDrive.drive(x, y, r);
    	
    	//Checks if the xbox right trigger has been pressed
    	boolean triggerPressed = launchStick.getRawAxis(3) > .5;
    	
    	if (triggerPressed){
    		mainLauncher.start();
    	} else {
    		mainLauncher.stop();
    	}
    	
    	MatOfKeyPoint b = new MatOfKeyPoint();
    	//b = pipeline.findBlobsOutput();
    	String z = b.toString();
    	
    	SmartDashboard.putString("imagestring", z);
    	
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    
}
