import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;

public class RightColor implements Behavior {
	public boolean suppressed;
	private PilotRobot me;
	private MovePilot pilot;
	
	GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
	
	public RightColor(PilotRobot robot){
    	 me = robot;
    	 pilot = me.getPilot();
    }
	
	public void suppress(){
		suppressed = true;
	}
	
	public boolean takeControl(){
		if(Color.BLACK != me.getLeftColourSensor() && Color.BLACK == me.getRightColourSensor() && PilotRobot.corrected_lines == false) {
			pilot.stop();
			return true;
		}
		return false;
	}
	
	public void action() {
		
		System.out.println("Right");
		PilotRobot.last_behaviour = "Right Color";

		
		pilot.setAngularAcceleration(100);
		if( Color.BLACK == me.getLeftColourSensor()  ^ Color.BLACK == me.getRightColourSensor()) {
			pilot.rotate(3);
			PilotRobot.correctionIncrementCount++;
			
			// Only move if neither is black.
			if ( Color.BLACK != me.getLeftColourSensor()  && Color.BLACK != me.getRightColourSensor()){
				pilot.travel(0.5);
			}
			
		}
		
		/* Travel if both black */
		if ( Color.BLACK == me.getLeftColourSensor()  && Color.BLACK == me.getRightColourSensor()) {
			return;
		}
		
		if(PilotRobot.correctionIncrementCount > 10) {
			pilot.travel(0.05);
			//PilotRobot.runMove = true;
		}
		
		pilot.setAngularAcceleration(PilotRobot.ANGULAR_ACCELERATION);
		
	}
}
