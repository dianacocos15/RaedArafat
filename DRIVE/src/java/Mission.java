
public class Mission {

	private final int gSize;
	private char[][] gridArray;
	private ParamedicEnv globalPara;
	private Pathfinding pf;
	private int actualVictimsRemaining = 3;
	private int unvisitedPotentialLocations = 5;

	// victims
	boolean isVictim = false;
	boolean isVictimCritical = false;

	// constructor
	public Mission(int gSize, ParamedicEnv localPara) {
		this.gSize = gSize;
		this.globalPara = localPara;

		gridArray = new char[gSize][gSize];
		// initializing array
		for (int y = 0; y < gSize; y++) {
			for (int x = 0; x < gSize; x++) {
				gridArray[x][y] = '0';
			}
		}

	}

	// adds obstacles to the grid
	public void addObstacle(int x, int y) {
		System.out.println("[Mission] Obstacle received at (" + x + ", " + y + ")");
		gridArray[x][y] = 'X';
	}

	// adds hospital to the grid
	public void addHospital(int x, int y) {
		System.out.println("[Mission] Hospital received at (" + x + ", " + y + ")");
		gridArray[x][y] = 'H';
	}

	// adds potential victim locations to the grid
	public void addPotentialVictim(int x, int y) {
		System.out.println("[Mission] Potential victim received at (" + x + ", " + y + ")");
		gridArray[x][y] = 'V';
	}

	// called when finished receiving grid layout from doctor
	public void finishedRecGrid() {
		// drawGrid();
		beginRescue();
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

	// begins the path assignment for the robot
	private void beginRescue() {

		// creates instance of pathfinding to begin the rescue
		pf = new Pathfinding(this, gSize, globalPara);

	}

	// called when robot arrives at one of the possible locations and reads the
	// colour to ask for the status
	public void checkForVictim(String color, int x, int y) {
		// we visited a potential victim location so we decrement the remaining count
		unvisitedPotentialLocations--;
		if (color == "white") {
			globalPara.removePotentialVictimBelief(x, y);
		} else {
			globalPara.victimFound(color, x, y);
		}
	}

	// called when critical victim is found
	public void foundCritical(int x, int y) {
		isVictimCritical = true;
		System.out.println("[Mission] Take victim to hospital");
		gridArray[x][y] = 'C';
		System.out.println("Potential victim (V) updated to critical (C)");
		// drawGrid();
		// calls method in Pathfinding to take the victim to the hospital
		pf.goToHospital(x, y);
		actualVictimsRemaining--;
	}

	// called when non-critical victim is found
	public void foundNonCritical(int x, int y) {
		isVictimCritical = false;
		System.out.println("[Mission] Mark victim and continue search");
		gridArray[x][y] = 'N';
		System.out.println("Potential victim (V) updated to non-critical (N)");
		// drawGrid();
		// calls method in Pathfinding to continue search to next victim
		if (unvisitedPotentialLocations == 0) {
			// goes to hospital when all victim locations have been checked
			pf.goToHospital(x, y);
			actualVictimsRemaining--;
			pf.rescueRemainingNonCriticals(actualVictimsRemaining,x , y);
		} else {
			pf.goToNextVictim(x, y);
		}
	}

	public void foundNoVictim(int x, int y) {
		isVictim = false;
		System.out.println("[Mission] No victim. Continue search");
		gridArray[x][y] = '0';
		System.out.println("Potential victim (V) updated to no victim (0)");

		pf.goToNextVictim(x, y);
	}

	public char[][] getGridArray() {
		return gridArray;
	}

	// to be called when a non critical victim has been rescued
	public void rescuedNonCritical(int x, int y) {
		globalPara.removePotentialVictimBelief(x, y);
		gridArray[x][y] = '0';
	}

	// public int getActualVictimsRemaining() {
	// return actualVictimsRemaining;
	// }

	// public void setActualVictimsRemaining()

}
