package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Launcher {

	Talon motor = new Talon(2);
	Servo servo = new Servo(0);
	double launchPower = 1;
	
	public Launcher() {
	
		motor.setInverted(true);
		
		SmartDashboard.putNumber("Launch Power", launchPower);
		
	}
	
	long startTimestamp = 0;
	boolean running = false;
	
	public void start() {
		
		if (!running){
			running = true;
			startTimestamp = System.currentTimeMillis();
		}
		
		launchPower = SmartDashboard.getNumber("Launch Power", 1);
		
		if ((System.currentTimeMillis()-startTimestamp)>500){
			servo.setAngle(100);
		}

		motor.set(launchPower);
		
	}
	
	public void stop() {
		motor.set(0);
		servo.setAngle(38);
		running = false;
	}
	
	
}
