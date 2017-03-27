package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Drive train controls
 * @author James.York
 */
public class DriveTrain {

	Talon frontRight = new Talon(5);
	Talon backRight = new Talon(1);
	Talon backLeft = new Talon(6);
	Talon frontLeft = new Talon(3);
	
	double FLK = 1;
	double FRK = 1;
	double BLK = 1;
	double BRK = 1;
	
	
	
	public DriveTrain(){
		frontRight.setInverted(false);
		backRight.setInverted(true);
		backLeft.setInverted(false);
		frontLeft.setInverted(false);
		
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
		
		flpower = (y -x +r);
        frpower = (-y -x +r);
        blpower = (y +x +r);
        brpower = (-y +x +r);
		
		FLK = SmartDashboard.getNumber("FLK", 1);
		FRK = SmartDashboard.getNumber("FRK", 1);
		BLK = SmartDashboard.getNumber("BLK", 1);
		BRK = SmartDashboard.getNumber("BRK", 1);
		
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
