package org.usfirst.frc.team4068.robot.subsystems;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sonar {
	
	private AnalogInput sonar = new AnalogInput(0);
	
	/** puts gets distance from sonar thing and puts it on
	 *  SmartDashboard and returns it as a double
	 */
	public double getDistancemm() {
		double voltage = sonar.getVoltage();
		double sensitivity = 6/5120;
		
		double mm = sensitivity * voltage;
		SmartDashboard.putNumber("distance mm", mm);
		return mm;
		
	}

}
