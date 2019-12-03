import lejos.robotics.navigation.MovePilot;

/**
 * 
 */

/**
 * @author Suhail Munshi
 *
 */
public class Movement {
	MovePilot pilot;
	PilotRobot robot;
	
	private final double CELL_WIDTH = 25.5;
	
	/*
	 * Constructor
	 * */
	public Movement(PilotRobot me) {
		robot = me;
		pilot = me.getPilot();
	}
	
	/*
	 * Travel a number of cells in a straight line
	 * Travels along x or y axis
	 * @param int cells
	 * */
	public void travelCellDistance(int number_of_cells) {
		PilotRobot.resetTachoCount();
		double required_distance;
		double cell_width = 25.5;
					// Distance in cm
		
		pilot.setLinearAcceleration(PilotRobot.ACCELERATION);
		pilot.setLinearSpeed(PilotRobot.TOP_SPEED);
		
		if(PilotRobot.rotated = true) {
			required_distance = cell_width*number_of_cells -8.5;
			PilotRobot.rotated = false;
		}
		else {
			required_distance = cell_width*number_of_cells;
		}
		
		pilot.travel(required_distance);
		
		while(true) {
			//if we have travelled enough then break
			int actual_distance = (int)PilotRobot.getTravelDistance();
			
			if(actual_distance == (int)required_distance){
				break;
			}
			else {
				pilot.travel(required_distance - actual_distance);
			}
		}
		pilot.setLinearAcceleration(PilotRobot.DECELERATION);
	}
	
	/*
	 * Rotate an integer value with appropriate corrections
	 * @param int angle
	 * */
	public void rotate(int value) {
		
		int initialAngle = (int)robot.getAngle(); //gyroscope
		int finalAngle = (int)robot.getAngle();
		int difference = (int)(finalAngle - initialAngle);
		pilot.setAngularAcceleration(15);
		//While we havent rotated the appropriate value
		while (value != difference) {
			finalAngle = (int)robot.getAngle();
			difference = finalAngle - initialAngle;
			pilot.rotate(value - difference);
			
		}
	}
	
	/*
	 * Set acceleration to max and stop instantly
	 * */
	public void stopInstantly() {
		pilot.setLinearAcceleration(150);
		pilot.stop();
	}
	
	
	/*
	 * Move with interrupt, default distance = 1 cell
	 * */
	public void moveWithInterrupt() {
		moveWithInterrupt(CELL_WIDTH);
	}
	
	/**
	 * Move off at a slow speed with interrupt
	 * */
	public void moveWithInterrupt(double distance) {
		pilot.setLinearSpeed(5);
		pilot.setLinearAcceleration(3);
		pilot.travel(distance, true);
		pilot.setLinearAcceleration(150);
	}
	
}
