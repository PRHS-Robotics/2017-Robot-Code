package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Drive train controls
 * @author James.York
 */
public class DriveTrain {

	Talon frontRight = new Talon(1);
	Talon backRight = new Talon(5);
	Talon backLeft = new Talon(4);
	Talon frontLeft = new Talon(2);
	
	double FLK = 1;
	double FRK = 1;
	double BLK = 1;
	double BRK = 1;
	
	
	
	public DriveTrain(){
		frontRight.setInverted(true);
		backRight.setInverted(true);
		backLeft.setInverted(true);
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
		
		double sum = (Math.abs(x) + Math.abs(y) + Math.abs(r));
		
		if(sum != 0){
			flpower = ( y - x + r) / sum;
			frpower = (-y - x + r) / sum;
			blpower = ( y + x + r) / sum;
			brpower = (-y + x + r) / sum;
		} else {
			flpower = frpower = blpower = brpower = 0;
		}
		
		/*flpower = (y -x +r);
        frpower = (-y -x +r);
        blpower = (y +x +r);
        brpower = (-y +x +r);*/
		
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
