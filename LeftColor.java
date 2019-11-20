
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;

public class LeftColor implements Behavior {
	public boolean suppressed;
	private PilotRobot me;
	private MovePilot pilot;
	
	GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
	
	public LeftColor(PilotRobot robot){
    	 me = robot;
    	 pilot = me.getPilot();
    }
	
	public void suppress(){
		suppressed = true;
	}
	
	public boolean takeControl(){
		if(Color.BLACK == me.getLeftColourSensor() && Color.BLACK != me.getRightColourSensor() && PilotRobot.corrected_lines == false) {
			pilot.stop();
			return true;
		}
		return false;
	}

	public void action() {
		pilot.setAngularAcceleration(100);
		
		System.out.println("LEFT");
		PilotRobot.last_behaviour = "Left Color";
		
		/*
		 * Issue ? Does this mean if neither is black rotate?
		 * Fix - Exclusive or (only rotate if one is black)
		 * **/
		if( Color.BLACK == me.getLeftColourSensor()  ^ Color.BLACK == me.getRightColourSensor()) {
			pilot.rotate(-3);
			PilotRobot.correctionIncrementCount++;
			
			// Only move if neither is black. Consider commenting this out as behaves odly on colours.
			if ( Color.BLACK != me.getLeftColourSensor()  && Color.BLACK != me.getRightColourSensor()){
				pilot.travel(0.5, true);
			}
			
		}
		
		/*Travel backwards if both black*/
		if ( Color.BLACK == me.getLeftColourSensor()  && Color.BLACK == me.getRightColourSensor()) {
			return;
		}
		
		if(PilotRobot.correctionIncrementCount > 10) {
			pilot.travel(0.5);
		}
		
		pilot.setAngularAcceleration(PilotRobot.ANGULAR_ACCELERATION);
		
	}
}