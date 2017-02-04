package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Launcher {

	Talon motor = new Talon(1);
	
	double la = 1;
	
	public Launcher(){
	
		motor.setInverted(true);
		
		SmartDashboard.putNumber("La", la);
		
	}
	
	
	
	public void launch (){
		
		la = SmartDashboard.getDouble("la", 1);
		
		SmartDashboard.putNumber("LaunchPower", la);

		motor.set(la);
		
	}
	
	
}
