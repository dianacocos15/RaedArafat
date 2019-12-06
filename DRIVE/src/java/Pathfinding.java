import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pathfinding {

	private int gSize;
	private ParamedicEnv globalPara;
	private char[][] gridArray;
	private Mission m;
	private static AStarTester a;
	private PCClient pc;
	private AStarTester.Location[] locations = new AStarTester.Location[6];

	/*fOR TESTING*/
	int curr_x = 0;
	int curr_y = 0;

	public Pathfinding(Mission m, int gSize, ParamedicEnv localPara, AStarTester tester) {
		this.gSize = gSize;
		this.globalPara = localPara;
		gridArray = new char[gSize][gSize];
		this.m = m;
		this.a = tester;
		//this.astar = astar;
		
		gridArray = m.getGridArray();
		//astar = new AStar(maze, AStar.xstart, AStar.ystart);
		//AStar astar = new AStar(maze, 0, 0);
		drawGrid();
		//goToNextVictim(0,0);
		pc = new PCClient();
		while(true){
			if(pc.connectClient()){
				break;
			}
		}

		goToFirstVictim();

		// TEST
		// m.checkForVictim("cyan", 4, 3);
		//m.checkForVictim("burgandy", 1, 3);
		// m.checkForVictim("white", 0, 4);
	}
	
	
	/*To add hospital or specific location/ landmark */
	public void addLocation(String name, int x, int y, int index) {
		AStarTester.Location new_point = new AStarTester.Location(name, x ,y);
		locations[index] = new_point;
		System.out.println("Location is: " + locations[index].name + " " + locations[index].x + " " + locations[index].y);
	}
	
	/*Finished Adding Locations, Run this function*/
	public void finishedAddingLocations(){
		goToNextVictim(0, 0);
	}
	
	// this method is called to take a victim to the hospital
	public void goToHospital(int currentX, int currentY) {
		// get the updated array from Mission
		updateGrid();
		System.out.println("[Pathfinding] Got updated array");
		drawGrid();
		System.out.println("[Pathfinding] Instructed to take victim to to hospital");

		/*Set next start coordinates to 0,0*/
		curr_x = 0;
		curr_y = 0;

		List<String> commands = a.getListOfCommandsFromOneLocationToAnother(currentX, currentY, 0, 0);
		pc.sendMultipleCommands(commands);
		goToNextVictim(curr_x,curr_y);
	}
	
	public void goToFirstVictim() {
		System.out.println("[Pathfinding] Instructed to go to the first victim");
		
		AStarTester.Location firstLocation = AStarTester.locations[AStarTester.indexes.get(1)];
		curr_x = firstLocation.x;
		curr_y = firstLocation.y;
		
		List<String> commands = a.getListOfCommandsFromOneLocationToAnother(0, 0, curr_x, curr_y);
		if (pc.sendMultipleCommands(commands)){
			System.out.println("GETTING COLOR");

			//Now at victim, check color sensor values and check for victim (NEEDS IMPLEMENTING)
			String values = pc.getColor();

			System.out.println(values);

			//Check for victim in square
			m.checkForVictim(values, curr_x, curr_y);
		}
		else{
			System.out.println("Failed");
		}
	}

	// this method is called to go to next potential victim location
	public void goToNextVictim(int currentX, int currentY) {
		// next unvisited victim
		// get the updated array from Mission
		updateGrid();
		System.out.println("[Pathfinding] Got updated array");
		drawGrid();
		System.out.println("[Pathfinding] Instructed to continue to next victim");

		AStarTester.Location nextLocation = a.goToNextUnvisitedLocation(currentX, currentY);
		
		//Location coords goal
		curr_x = nextLocation.x;
		curr_y = nextLocation.y;
	
		List<String> commands = a.getListOfCommandsFromOneLocationToAnother(currentX, currentY, curr_x, curr_y);

		if (pc.sendMultipleCommands(commands)){
			System.out.println("GETTING COLOR");

			//Now at victim, check color sensor values and check for victim (NEEDS IMPLEMENTING)
			String values = pc.getColor();

			System.out.println(values);

			//Check for victim in square
			m.checkForVictim(values, curr_x, curr_y);
		}
		else{
			System.out.println("Failed");
		}
		
	}

	// this method is called after all critical victims (if they exist) have been
	// taken care of and all potential victim squares have been visited,
	// meaning there are only non-critical victims remaining at known locations.
	// the number of remaining non-critical victims is passed here, so we know how
	// many are left to rescue
	public void rescueRemainingNonCriticals(int remaining, int currentX, int currentY) {
		// for every victim that is saved, the method: m.rescuedNonCritical(x,y) must be
		// called upon taking the victim in question to the hospital.
		// This is important to remove the Jason belief for the victim location in
		// paramedic

		List<Node> noncriticalVictims = new ArrayList<>();
		int xNow = 0;
		int yNow = 0;
		int xNext = 0;
		int yNext = 0;
		
		for(int y = 0; y < gridArray.length; y++) {
			for (int x = 0; x < gridArray[y].length; x++) {
				if (gridArray[y][x] == 'N') {
					noncriticalVictims.add(new Node(null, x, y, 0, 0));
				}
			}
		}
		
		for (int i = 0; i < noncriticalVictims.size(); i++) {
			xNow = noncriticalVictims.get(i).x;
			yNow = noncriticalVictims.get(i).y;
			
			xNext = noncriticalVictims.get(i+1).x;
			yNext = noncriticalVictims.get(i+1).y;
			
			a.getListOfCommandsFromOneLocationToAnother(xNow, yNow, xNext, yNext);
		}	
	}

	// updates the array in Pathfinding to resemble the one in Mission
	private void updateGrid() {
		gridArray = m.getGridArray();
	}

	public void drawGrid() {
		System.out.println("[Pathfinding]:");
		for (int y = 0; y < gSize; y++) {
			for (int x = 0; x < gSize; x++) {
				System.out.print(gridArray[x][y]);
			}
			System.out.println();
		}
	}

	// The program works in the following way:

	// - The Mission class will be initialized by the Paramedic Env class.
	// - The Mission class will then communicate to the doctor and receive the grid
	// layout with the positions of obstacles, potential victims, and the hospital
	// - Mission will then begin the rescue mission by initializing the Pathfinding
	// class (your class) and passing it the 2D array resembling the grid
	// - This Pathfinding class should include the code for reaching different parts
	// of the grid which is not included now
	// - This following methods are available to it
	// - method m.checkForVictim(colour, x, y) which asks the doctor the status of
	// the victim, depending upon the status, the following methods will be called:
	// - goToHospital() : the robot must take the victim to the hospital or
	// - goToNextVictim() : the robot must leave the victim where it is and move
	// onto the next possible location
	// - rescueRemainingNonCriticals(remaining) : the robot has dealt with all
	// critical victims (if there are any) and is now to rescue the remaining
	// critical victims,
	// this method will only be called by Mission when all the victim locations are
	// known. All the robot must therefore do is take each individual victim to the
	// hospital.
	// As soon as each victim is picked up to be taken to the hospital, the method
	// m.rescuedNonCritical(x,y) must be called for the Jason part, and
	// followed by the call of the method updateGrid() that will update the
	// gridArray in this class with the new values
	// The method rescueRemainingNonCriticals method will have the number of
	// remaining victims available to it, so Pathfinding can know how many are left,
	// this value must be decremented every time a victim is picked up to be brought
	// to the hospital
	//

}

