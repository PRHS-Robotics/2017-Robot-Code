package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Drive train controls
 * @author James.York
 */
public class DriveTrain {

	Talon frontRight = new Talon(3);
	Talon backRight = new Talon(4);
	Talon backLeft = new Talon(5);
	Talon frontLeft = new Talon(2);
	
	double FLK = 1;
	double FRK = 1;
	double BLK = 1;
	double BRK = 1;
	
	
	
	public DriveTrain(){
		frontRight.setInverted(true);
		backRight.setInverted(true);
		backLeft.setInverted(false);
		frontLeft.setInverted(true);
		
		SmartDashboard.putNumber("FLK", FLK);
		SmartDashboard.putNumber("FRK", FRK);
		SmartDashboard.putNumber("BLK", BLK);
		SmartDashboard.putNumber("BRK", BRK);
		
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
		flpower += (y/(1 + abs(x) + abs(r)));
		flpower += (r/(1 + abs(x) + abs(y)));
		
		FLK = SmartDashboard.getDouble("FLK", 1);
		FRK = SmartDashboard.getDouble("FRK", 1);
		BLK = SmartDashboard.getDouble("BLK", 1);
		BRK = SmartDashboard.getDouble("BRK", 1);
		
		SmartDashboard.putNumber("Front Left", flpower * FLK);
		SmartDashboard.putNumber("Front Right", frpower * FRK);
		SmartDashboard.putNumber("Back Left", blpower * BLK);
		SmartDashboard.putNumber("Back Right", brpower * BRK);
		
		
		frontRight.set(frpower * FRK);
		backRight.set(brpower * BRK);
		backLeft.set(blpower * BLK);
		frontLeft.set(flpower * FLK);
			
	}
	
}
