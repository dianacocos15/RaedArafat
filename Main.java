import java.util.List;


import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;
//A cell is 25.5cm long.


public class Main {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MovePilot pilot;
		PilotRobot me;
		
		me = new PilotRobot();
		
		pilot = me.getPilot();
		
		// Create new Travel Cells function in PilotRobot
		// Create new Localise Gyroscope function.
//		System.out.println(me.getAngle());
//		pilot.rotate(90);
//		System.out.println(me.getAngle());
		Corrections c = new Corrections(me);
		while(true) {
			me.travelCellDistance(4);
			pilot.rotate(-90);
			c.lineLocalisation();
		}
	}

}
