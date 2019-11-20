
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;

public class BothColor implements Behavior {
	public boolean suppressed;
	private PilotRobot me;
	private MovePilot pilot;
	private int count = 0;
	private Arbitrator arby;
	
	GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();
	
	public BothColor(PilotRobot robot){
    	 me = robot;
    	 pilot = me.getPilot();
    }
	
	public void suppress(){
		suppressed = true;
	}
	
	public boolean takeControl(){
		if(Color.BLACK == me.getLeftColourSensor() && Color.BLACK == me.getRightColourSensor() && PilotRobot.corrected_lines == false && !pilot.isMoving()) {
			pilot.stop();
			return true;
		}
		return false;
	}
	
	public void set_arby(Arbitrator a) {
		arby = a;
	}

	public void action() {
		/*Travel backwards if both black*/
		if ( Color.BLACK == me.getLeftColourSensor()  && Color.BLACK == me.getRightColourSensor()) {
			PilotRobot.correctionIncrementCount = 0;
			PilotRobot.corrected_lines = true;
			pilot.setLinearAcceleration(3);
			pilot.travel(-10);
			pilot.setLinearAcceleration(150);
			count++;
		}
		
		if(count == 2) {
			while(pilot.isMoving()) {}		// Wait till not moving, then stop arby
			arby.stop();
		}
		
	}
}