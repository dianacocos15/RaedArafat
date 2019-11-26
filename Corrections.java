
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;



/**
 * @author Suhail Munshi
 * Class exists to correct on lines to localise
 */
public class Corrections{
	MovePilot pilot;
	PilotRobot robot;
	Movement move;
	
	/*
	 * Constructor
	 * */
	public Corrections(PilotRobot me) {
		robot = me;
		pilot = me.getPilot();
		move = new Movement(robot);
	}
	
	/**
	 * Method to call for full inside cell localisation
	 * Uses grid lines
	 * */
	public void lineLocalisationToRight() {
		/*
		 * Travel forward with interrupt, correct using lines, move backwards 
		 * rotate 90 degrees and repeat.
		 * */
		move.moveWithInterrupt();
		correctOnLines(30, false);
		move.rotate(90);
		move.moveWithInterrupt();
		correctOnLines(30, true);
	}
	
	/**
	 * Method to call for full inside cell localisation
	 * Uses grid lines
	 * */
	public void lineLocalisationToLeft() {
		/*
		 * Travel forward with interrupt, correct using lines, move backwards 
		 * rotate 90 degrees and repeat.
		 * */
		move.moveWithInterrupt();
		correctOnLines(30, false);
		move.rotate(-90);
		move.moveWithInterrupt();
		correctOnLines(30, true);
	}
	
	/**
	 * Method to call for full inside cell localisation
	 * Uses grid lines
	 * */
	public void lineLocalisationBehind() {
		/*
		 * Travel forward with interrupt, correct using lines, move backwards 
		 * rotate 90 degrees and repeat.
		 * */
		move.rotate(180);
		move.moveWithInterrupt();
		correctOnLines(30, false);
		move.rotate(90);
		move.moveWithInterrupt();
		correctOnLines(30, true);
	}
	
	
	
	/*
	 * Correct on black lines, rotate and return to center
	 * */
	public void rotateAndLineCorrect(int angle){
		move.rotate(angle);
		correctOnLines(30,false);
	}
	
	/**
	 * Already have stopped on lines
	 * Correct on the black lines
	 * Stop moving when reaching black lines
	 * Rotate until both colour sensors reach black lines
	 * */
	public void correctOnLines(int max_degrees, boolean reset_gyro) {
		pilot.setAngularAcceleration(10);
		pilot.setLinearSpeed(10);
		int corrections_count = 0;
		
		while(true) {
			if(robot.getLeftColourSensor() == Color.BLACK && robot.getRightColourSensor() == Color.BLACK) {
				Sound.beep();
				robot.resetGyro();
				move.stopInstantly();
				break;
			}
			
			if(corrections_count*3 >= max_degrees) {
				Sound.twoBeeps();
				break;
			}
			
			//left is black, not right
			if(robot.getLeftColourSensor() == Color.BLACK && robot.getRightColourSensor() != Color.BLACK) {
				move.stopInstantly();
				pilot.rotate(-3,true);
				corrections_count ++;
			}
			
			//right is black, not left
			if(robot.getRightColourSensor() == Color.BLACK && robot.getLeftColourSensor() != Color.BLACK) {
				move.stopInstantly();
				pilot.rotate(3,true);
				corrections_count ++;
			}
			
			//neither is black and it is not moving.
			if(robot.getRightColourSensor() != Color.BLACK && robot.getLeftColourSensor() != Color.BLACK && !pilot.isMoving()) {
				move.stopInstantly();
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
		//Travel distance of half square backwards minus distance of wheel center to color sensors
		pilot.travel(-12.75+4.253);
	}
	
	
}