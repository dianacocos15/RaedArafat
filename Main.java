import java.io.File;
import java.util.List;


import lejos.hardware.Button;
import lejos.hardware.*;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;
//A cell is 25.5cm long.


public class Main {
	
	static PilotRobot me;
	
	public static void main(String[] args) {
		me = new PilotRobot();
		Movement m = new Movement(me);
		Corrections c = new Corrections(me);
		
		System.out.println("Waiting for PC....");
		EV3Server ev3 = new EV3Server();
		ev3.run(me);
		System.out.println("Connecting....");
		
		while(true) {
			
		}
	}
	


}
