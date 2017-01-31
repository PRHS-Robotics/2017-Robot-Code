package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;


/**
 * Drive train controls
 * @author James.York
 */
public class DriveTrain {

	Talon frontRight = new Talon(1);
	Talon backRight = new Talon(2);
	Talon backLeft = new Talon(3);
	Talon frontLeft = new Talon(4);
	
	public DriveTrain(){
		frontRight.setInverted(true);
		backRight.setInverted(true);
		backLeft.setInverted(true);
		frontLeft.setInverted(true);
		
	}
	
	private static double abs(double a) {
		return Math.abs(a);
	}
	
	public void drive(double x, double y, double r){
		double frpower, brpower, blpower, flpower;
		
		
		frpower = (-x/(1 + abs(y) + abs(r)));
		frpower += (-y/(1 + abs(x) + abs(r)));
		frpower += (r/(1 + abs(x) + abs(y)));
		
		brpower = (x/(1 + abs(y) + abs(r)));
		brpower += (-y/(1 + abs(x) + abs(r)));
		brpower += (r/(1 + abs(x) + abs(y)));
		
		blpower = (x/(1 + abs(y) + abs(r)));
		blpower += (y/(1 + abs(x) + abs(r)));
		blpower += (r/(1 + abs(x) + abs(y)));
		
		flpower = (-x/(1 + abs(y) + abs(r)));
		flpower += (y/(1 + abs(y) + abs(r)));
		flpower += (r/(1 + abs(x) + abs(y)));
		
		frontRight.set(frpower);
		backRight.set(brpower);
		backLeft.set(blpower);
		frontLeft.set(flpower);
			
	}
	
}
