package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;

public class BallCollector {
	Talon motor = new Talon(7);
	public BallCollector() {
		motor.setInverted(true);
	}
	
	public void spin(double speed) {
		motor.set(speed);
	}
	public void stop() {
		motor.set(0);
	}
}