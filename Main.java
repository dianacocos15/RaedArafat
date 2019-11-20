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
		
		PilotRobot.resetTachoCount();
		pilot.setLinearAcceleration(PilotRobot.ACCELERATION);
		pilot.setLinearAcceleration(1000);
		listenForBlackLines(pilot,me);
		
		
		Button.waitForAnyPress();
	}
	
	
	
	
	/**
	 * Localise Method
	 * Travel slowly
	 * Interrupt travel using instant stop upon seeing black colour
	 * Rotate until both colours are black
	 * Reverse 12.5 cm, rotate 90 degrees and localise.
	 * Ta da we are localised.
	 * */
	
	
	
	//Method to interrupt at black lines
	public static void listenForBlackLines(MovePilot pilot, PilotRobot me) {
		Behavior left = new LeftColor(me); 
		Behavior right = new RightColor(me);
		BothColor both = new BothColor(me);
		
		Behavior cntrll = new Controller(me);	//Figures out order of behaviours, controls overall activity
		Behavior[] bArray = {cntrll, left, right, both};
		
		
		
		Arbitrator arby = new Arbitrator(bArray,true);
		
		both.set_arby(arby);
		
		
		arby.go();
		System.out.println("END BLACK LINES");
	}

}
