package org.usfirst.frc.team4068.robot.subsystems;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sonar {
	
	private AnalogInput sonar = new AnalogInput(7);
	
	/** puts gets distance from sonar thing and puts it on
	 *  SmartDashboard and returns it as a double
	 */
	public double getInches() {
		double voltage = sonar.getVoltage();
		double sensitivity = 1/5120;
		SmartDashboard.putNumber("Sensitivity", sensitivity);
		sensitivity = SmartDashboard.getNumber("Sensitivity", 1);
		double mm = sensitivity * voltage;
		return mm;
		
	}

}
