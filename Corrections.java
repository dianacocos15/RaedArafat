
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;



/**
 * Class exists to allow arby to stop only when goal accomplished*/
public class Corrections{
	MovePilot pilot;
	PilotRobot robot;
	
	/*
	 * Constructor
	 * */
	public Corrections(PilotRobot me) {
		robot = me;
		pilot = me.getPilot();
	}
	
	/**
	 * Method to call for full inside cell localisation
	 * Uses grid lines
	 * */
	public void lineLocalisation() {
		/*
		 * Travel forward with interrupt, correct using lines, move backwards 
		 * rotate 90 degrees and repeat.
		 * */
		robot.rotate(90);
		moveWithInterrupt();
		correctOnLines(30, false);
		robot.rotate(-90);
		moveWithInterrupt();
		correctOnLines(30, true);
	}

	
	/**
	 * Already have stopped on lines
	 * Correct on the black lines
	 * Stop moving when reaching black lines
	 * Rotate until both colour sensors reach black lines
	 * */
	public void correctOnLines(int max_degrees, boolean set_gyro) {
		pilot.setAngularAcceleration(10);
		int corrections_count = 0;
		
		while(true) {
			if(robot.getLeftColourSensor() == Color.BLACK && robot.getRightColourSensor() == Color.BLACK) {
				Sound.beep();
				pilot.setLinearAcceleration(150);
				pilot.stop();
				break;
			}
			
			if(corrections_count*3 >= max_degrees) {
				Sound.twoBeeps();
				break;
			}
			
			//left is black, not right
			if(robot.getLeftColourSensor() == Color.BLACK && robot.getRightColourSensor() != Color.BLACK) {
				pilot.stop();
				pilot.rotate(-3,true);
				corrections_count ++;
			}
			
			//right is black, not left
			if(robot.getRightColourSensor() == Color.BLACK && robot.getLeftColourSensor() != Color.BLACK) {
				pilot.stop();
				pilot.rotate(3,true);
				corrections_count ++;
			}
			
			//neither is black
			if(robot.getRightColourSensor() != Color.BLACK && robot.getLeftColourSensor() != Color.BLACK && !pilot.isMoving()) {
				pilot.stop();
				pilot.setLinearAcceleration(3);
				pilot.travel(10,true);
				corrections_count = 0;
			}
		
		}
		
		robot.resetGyro();
		bothBlackAction();
	}
	
	
	/**
	 * Upon reaching black lines
	 * do this action
	 * */
	public void bothBlackAction() {
		pilot.setLinearAcceleration(PilotRobot.ACCELERATION);
		pilot.travel(-10);
		pilot.setLinearAcceleration(PilotRobot.ACCELERATION);
	}
	
	
	/**
	 * Move off at a slow speed with interrupt
	 * */
	public void moveWithInterrupt() {
		pilot.setLinearSpeed(5);
		pilot.setLinearAcceleration(3);
		pilot.travel(25.5, true);
		pilot.setLinearAcceleration(150);
	}
	
}