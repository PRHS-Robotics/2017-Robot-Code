package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Launcher {

	Talon motor = new Talon(1);
	
	double launchPower = 1;
	
	public Launcher() {
	
		motor.setInverted(true);
		
		SmartDashboard.putNumber("Launch Power", launchPower);
		
	}
	
	
	
	public void start() {
		
		launchPower = SmartDashboard.getNumber("Launch Power", 1);
		

		motor.set(launchPower);
		
	}
	
	public void stop() {
		motor.set(0);
	}
	
	
}
