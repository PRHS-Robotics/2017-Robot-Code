package org.usfirst.frc.team4068.robot.subsystems;

import edu.wpi.first.wpilibj.CameraServer;

public class CustomCameraServer{
	CustomCameraServer instance = null;
	CameraServer server;
	private CustomCameraServer(){
		server = CameraServer.getInstance();
	}
	
	public CustomCameraServer getInstance(){
		if (instance==null){
			instance = new CustomCameraServer();
		}
		return instance;
	}
	
	public void addCamera(String cam){
		
	}
}
