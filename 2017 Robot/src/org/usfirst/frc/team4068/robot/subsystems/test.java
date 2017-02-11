package org.usfirst.frc.team4068.robot.subsystems;

import org.opencv.core.MatOfKeyPoint;

public class test {
	public void x() {
		GripPipeline pipeline = new GripPipeline();
    	MatOfKeyPoint y = new MatOfKeyPoint();
    	y = pipeline.findBlobsOutput();
    	String z = y.toString();
    	
    	System.out.println(z);
    }
	public static void main(String[] args) {
		test a = new test();
		a.x();
	}
}
