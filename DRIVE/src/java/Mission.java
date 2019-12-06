import java.io.*; 
import java.lang.Thread;
import java.util.*;

public class Mission {
	
	private final int gSize;
	private char[][] gridArray; 
	private ParamedicEnv globalPara;
	private Pathfinding pf;
	private int actualVictimsRemaining = 3;
	private int unvisitedPotentialLocations = 5;
	
	//temporary list used to store locations, so they can be added to the main list at the end, ensuring the order of locations in the final list is: HABCDE
	//private ArrayList<Location> tempVictimList = new ArrayList<Location>();
	private ArrayList<AStarTester.Obstacle> obstacleList = new ArrayList<AStarTester.Obstacle>(); 
	private ArrayList<AStarTester.Location> locationList = new ArrayList<AStarTester.Location>(); 
	private int locationNameIndex = 0; 
	private String[] locationNames = {"A", "B", "C", "D", "E"}; 
	
	
	//constructor
	public Mission(int gSize, ParamedicEnv localPara) {
		this.gSize = gSize;
		this.globalPara = localPara;
		
		gridArray = new char[gSize][gSize];
		//initializing array
		for (int y = 0; y < gSize; y++) {
			for (int x = 0; x < gSize; x++) {
				gridArray[x][y] = '0';
			}
		}
		
	}

	//adds obstacles to the array
	public void addObstacle(int x, int y) {
		System.out.println("[Mission] Obstacle received at (" + x + ", " + y + ")");
		gridArray[x][y] = 'X';
		obstacleList.add(new AStarTester.Obstacle(x, y));
	}
	
	//adds hospital to the array
	public void addHospital(int x, int y) {
		System.out.println("[Mission] Hospital received at (" + x + ", " + y + ")"); 
		gridArray[x][y] = 'H';
		locationList.add(0, new AStarTester.Location("H", x, y));
	}
	
	//adds potential victim locations to the array
	public void addPotentialVictim(int x, int y) {
		System.out.println("[Mission] Potential victim received at (" + x + ", " + y + ")");
		gridArray[x][y] = 'V';
		
	
		//tempVictimList.add(new Location(locationNames[locationNameIndex], x, y));
		//locationNameIndex++;
		
		locationList.add(new AStarTester.Location(locationNames[locationNameIndex], x, y)); 
		locationNameIndex++;
		
		
	}
	
	//called when finished receiving grid layout from doctor
	public void finishedRecGrid() {
		//drawGrid();
		beginRescue();
	}
	
	private void moveLocationsToFinalList() {
		
	}
	
	public void drawGrid() {
		System.out.println("[Mission]:");
		for (int y = 0; y < gSize; y++) {
			for (int x = 0; x < gSize; x++) {
				System.out.print(gridArray[x][y]);
			}
			System.out.println();
		}
	}
	
	//begins the path assignment for the robot
	private void beginRescue() {
		
		//creates instance of pathfinding to begin the rescue
		drawGrid();
		AStarTester as = new AStarTester(obstacleList, locationList);
		pf = new Pathfinding(this, gSize, globalPara, as);
	}
	
	//called when robot arrives at one of the possible locations and reads the colour to ask for the status
	public void checkForVictim(String color, int x, int y) {
		//we visited a potential victim location so we decrement the remaining count
		unvisitedPotentialLocations--;
		if (color.equals("white") ) {
			System.out.println("white in mission");
			globalPara.removePotentialVictimBelief(x, y);
		}
		else {
			globalPara.victimFound(color, x, y);
		}
	}
	
	//called when critical victim is found
	public void foundCritical(int x, int y) {
		System.out.println("[Mission] Take victim to hospital");
		gridArray[x][y] = 'C';
		System.out.println("Potential victim (V) updated to critical (C)");
		//drawGrid();
		//calls method in Pathfinding to take the victim to the hospital
		pf.goToHospital(x, y);
		actualVictimsRemaining--;
	}
	
	//called when non-critical victim is found
	public void foundNonCritical(int x, int y) {
		System.out.println("[Mission] Mark victim and continue search");
		gridArray[x][y] = 'N';
		System.out.println("Potential victim (V) updated to non-critical (N)");
		//drawGrid();
		//calls method in Pathfinding to continue search to next victim
		if (unvisitedPotentialLocations == 0) {
			//goes to hospital when all victim locations have been checked
			pf.goToHospital(x, y);
			actualVictimsRemaining--;
			pf.rescueRemainingNonCriticals(actualVictimsRemaining, x, y);
		}
		else {
			pf.goToNextVictim(x, y);
		}
		
		
	}
	
	public void foundNoVictim(int x, int y) {
		System.out.println("[Mission] No victim. Continue search");
		gridArray[x][y] = '0';
		System.out.println("Potential victim (V) updated to no victim (0)");
		
		pf.goToNextVictim(x, y);
	}

	public char[][] getGridArray() {
		return gridArray;
	}
	
	//to be called when a non critical victim has been rescued
	public void  rescuedNonCritical(int x, int y) {
		globalPara.removePotentialVictimBelief(x, y);
		gridArray[x][y] = '0';
	}
	
	//public int getActualVictimsRemaining() {
	//	return actualVictimsRemaining;
	//}
	
	//public void setActualVictimsRemaining()

}
