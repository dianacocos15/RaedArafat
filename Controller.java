
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;



/**
 * Class exists to allow arby to stop only when goal accomplished*/
public class Controller implements Behavior {
	public boolean suppressed;
	private PilotRobot me;
	private MovePilot pilot;
	private boolean move = true;
	private boolean rotate = true;
	private boolean reverse = true;
	int correct_count = 0;
	
	
	public Controller(PilotRobot robot){
		me = robot;
		pilot = me.getPilot();
	}

	public boolean takeControl(){
		if(PilotRobot.EndLocalise == false && !pilot.isMoving()) {
			return true;
		}
		return false;
	}

	public void action() {
	
		if(move) {
			pilot.setLinearAcceleration(3);
			pilot.travel(30,true);
			pilot.setLinearAcceleration(150);
			move = false;
			rotate = true;
			return;
		}
		
		if(rotate) {
			pilot.setAngularAcceleration(PilotRobot.ANGULAR_ACCELERATION);
			me.rotate(90);
			rotate = false;
			move = true;
			return;
		}
		
		PilotRobot.last_behaviour = "Controller";
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
}