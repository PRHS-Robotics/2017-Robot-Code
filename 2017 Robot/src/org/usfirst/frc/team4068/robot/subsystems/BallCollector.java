package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;

public class BallCollector {
	Talon motor = new Talon(6);
	public BallCollector() {
		motor.setInverted(true);
	}
	
	public void spin() {
		motor.set(1);
	}
	public void stop() {
		motor.set(0);
	}
}