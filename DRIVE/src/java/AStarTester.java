import java.util.List;
import java.util.ArrayList;

/*Project astar tester code referenced by PathFinding*/ 
public class AStarTester {
	//Costs of paths between locations stored in matrix
	private static Double[][] costs = new Double[6][6];

	//Optimal path
	static List<Node> optimalPath;

	//Directions
	private static int currentDirection;
	private final static int EAST = 0;
	private final static int SOUTH = 1;
	private final static int WEST = 2;
	private final static int NORTH = 3;

	//Locations
	Location nextLocation;

	

	/* Array of locations to travel to individually. */
	Location[] locations;

	//Obstacles
	List<Obstacle> obstacles = new ArrayList<Obstacle>();

	//Instance of inner class LocationOrder
	LocationOrder order;

	//List that holds the order of locations the robot needs to travel to
	List<Integer> indexes;

	//Declare AStar instance
	AStar astar;

	//AStar initialization
	int[][] maze = { 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 },
			};
	
	AStar aObject;

	public AStarTester(List<Obstacle> obstacles, List<Location> locations) {
		/*Mark each obstacle on map as 100*/
		for (Obstacle o : obstacles) {
			this.maze[o.y][o.x] = 100;
		}

		/*Set local array equal to called arraylist*/
		int index = 0;
		this.locations = new Location[6];
		for(Location loc : locations){
			this.locations[index] = loc;
			index = index + 1;
		}

		aObject = new AStar(this.maze);

		order = new LocationOrder();
		indexes = order.computeOrderOfLocations();

		for(int i = 0; i < 6; i++ ){
			order.wasLocationVisited[i] = false;
		}

	}

	static class Location {
		public String name;
		public int x;
		public int y;

		Location(String name, int x, int y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}
	}

	static class Obstacle {
		public int x;
		public int y;
		
		Obstacle(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	class LocationOrder {
		int nextLocation = 0;
		int currentLocation = 0;
		boolean[] wasLocationVisited = { false, false, false, false, false, false };
		List<Integer> locationsOrder = new ArrayList<>();

		public Double[][] computeCostsBetweenLocations() {

			for (int i = 0; i < locations.length; i++) {
				for (int j = 0; j < locations.length; j++) {

					optimalPath = aObject.findPathTo(locations[i].x, locations[i].y, locations[j].x, locations[j].y);
					printPath(optimalPath);

					double distance = optimalPath.get(optimalPath.size() - 1).g;

					// if value of rows and columns is the same, e.g. H == H
					// then set that value to 100 to avoid including it in the optimal path
					if (locations[i].x == locations[j].x && locations[i].y == locations[j].y) {
						costs[i][j] = 100.00;
					} else {
						costs[i][j] = distance;												
					}
					

					// System.out.print(costs[i][j]);
					// System.out.print(" ");
					// table.setValueAt(AStar.costs[i][j], i, j);
					// table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				}

				// System.out.println();

			}

			return costs;
		}

		public List<Integer> computeOrderOfLocations() {
			costs = computeCostsBetweenLocations();
			for (int i = currentLocation; i < 6; i++) {
				double smallestCost = Double.MAX_VALUE;
				locationsOrder.add(currentLocation);
				wasLocationVisited[currentLocation] = true;

				for (int j = 0; j < 6; j++) {	
					if (locationsOrder.contains(j))
						continue;
					else {
						if (costs[currentLocation][j] < smallestCost) {
							smallestCost = costs[currentLocation][j];
							nextLocation = j;
						}

						wasLocationVisited[nextLocation] = true;
					}
				}
				 System.out.println("Location " + currentLocation + " smallest cost " +
				 smallestCost + ". Next location is " + nextLocation);
				currentLocation = nextLocation;
			}

			locationsOrder.add(0);

			System.out.println();

			return locationsOrder;
		}
	}

	public Location goToNextUnvisitedLocation(int currentX, int currentY) {
		int currentIndex = 0;
		System.out.println(currentX);
		System.out.println(currentY);

		/*Print order of indexes*/
		System.out.println("ORDER IS : ");
		for(int i = 0; i< indexes.size()-1; i++ ){
			System.out.print(indexes.get(i));
		}
		System.out.println();

		for (int i = 0; i < indexes.size()-1; i++) {
			System.out.println("Locations Are : " + locations[i].name);
			
			if (currentX == locations[indexes.get(i)].x && currentY == locations[indexes.get(i)].y) {
				currentIndex = indexes.get(i);

				if (order.wasLocationVisited[indexes.get(currentIndex + 1)] == true) continue;
				else{
					nextLocation = locations[indexes.get(currentIndex +1)];
					//set as visited
					order.wasLocationVisited[indexes.get(currentIndex + 1)] = true;
				}
			
			}
		}
	
		return nextLocation;
	}	

	public List<String> getListOfCommandsFromOneLocationToAnother(int xstart, int ystart, int xend, int yend) {
		List<String> commandsList = new ArrayList<>();
		int nextDirection = 0;
		
		optimalPath = aObject.findPathTo(xstart, ystart, xend, yend);
		aObject.printPath(optimalPath);

		
		for (int a = 0; a < optimalPath.size() - 1; a++) {
			Node currentNode = optimalPath.get(a);
			Node nextNode = optimalPath.get(a + 1);

			int currentX = currentNode.x;
			int currentY = currentNode.y;

			int nextX = nextNode.x;
			int nextY = nextNode.y;

			// 0 = EAST
			// 1 = SOUTH
			// 2 = WEST
			// 3 = NORTH

			// y changes
			if (currentX == nextX) {
				if (currentY > nextY)
					nextDirection = 1;
				else
					nextDirection = 3;
			}

			// x changes
			else {
				if (currentX > nextX)
					nextDirection = 2;
				else
					nextDirection = 0;
			}

			switch (currentDirection) {

			case EAST: {
				switch (nextDirection) {
				case SOUTH:
					commandsList.add("90");
					break;
				case NORTH:
					commandsList.add("-90");
					break;
				case WEST:
					commandsList.add("-180");
					break;
				default:
					break;
				}
			}
				break;

			case SOUTH: {
				switch (nextDirection) {
				case EAST:
					commandsList.add("-90");
					break;
				case WEST:
					commandsList.add("90");
					break;
				case NORTH:
					commandsList.add("180");
					break;
				default:
					break;
				}
			}
				break;

			case WEST: {
				switch (nextDirection) {
				case SOUTH:
					commandsList.add("-90");
					break;
				case NORTH:
					commandsList.add("90");
					break;
				case EAST:
					commandsList.add("180");
					break;
				default:
					break;
				}
			}
				break;

			case NORTH: {
				switch (nextDirection) {
				case EAST:
					commandsList.add("90");
					break;
				case WEST:
					commandsList.add("-90");
					break;
				case SOUTH:
					commandsList.add("-180");
					break;
				default:
					break;
				}
			}
				break;
			}

			commandsList.add("travel");
			currentDirection = nextDirection;

			for (String command: commandsList) {
				System.out.println(command);
			}
			commandsList.clear();
		}
		
		return commandsList;
	}
	
	public void printPath(List<Node> holder){
		for(int i = 0; i < holder.size(); i++) {
			int holderX = holder.get(i).x;
			int holderY = holder.get(i).y;
		}
	}



}