import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AStar {
	private final List<Node> open; // list of unexpanded neighbours
	private final List<Node> closed; // list of expanded neighbours
	private final List<Node> path;
	List<Node> optimalPath;
	static int[][] maze = { 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
			};
	private Node now;
	private static int xstart = 0;
	private static int ystart = 0;
	private static int xend, yend;
	Double[][] costs = new Double[6][6];

	private final static int EAST = 0;
	private final static int SOUTH = 1;
	private final static int WEST = 2;
	private final static int NORTH = 3;

	Location H = new Location("H", 0, 0);
	Location A = new Location("A", 1, 3);
	Location B = new Location("B", 2, 4);
	Location C = new Location("C", 5, 3);
	Location D = new Location("D", 4, 2);
	Location E = new Location("E", 1, 5);

	Location[] locations = { H, A, B, C, D, E };

	static class Node implements Comparable {
		public Node parent;
		public int x, y;
		public double g;
		public double h;

		Node(Node parent, int xpos, int ypos, double g, double h) {
			this.parent = parent;
			this.x = xpos;
			this.y = ypos;
			this.g = g;
			this.h = h;
		}

		@Override
		public int compareTo(Object o) {
			Node that = (Node) o;
			return (int) ((this.g + this.h) - (that.g + that.h));
		}
	}

	class Location {
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

	class Victim {
		public int x;
		public int y;

		Victim(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	class LocationOrder {
		int nextLocation = 0;
		int currentLocation = 0;
		boolean[] wasLocationVisited = { false, false, false, false, false, false };
		List<Integer> locationsOrder = new ArrayList<Integer>();

		public Double[][] computeCostsBetweenLocations() {

			for (int i = 0; i < locations.length; i++) {
				for (int j = 0; j < locations.length; j++) {

					double distance = getOptimalPath(locations[i].x, locations[i].y, +locations[j].x, locations[j].y)
							.get(optimalPath.size() - 1).g;

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
			Double[][] costs = computeCostsBetweenLocations();
			double smallestCost = 200;
			for (int i = currentLocation; i < 6; i++) {
				// double smallestCost = 200;
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
				// System.out.println("Location " + currentLocation + " smallest cost " +
				// smallestCost + ". Next location is " + nextLocation);
				currentLocation = nextLocation;
			}

			locationsOrder.add(0);

			System.out.println();

			return locationsOrder;
		}
	}

	public AStar() {
		this.open = new ArrayList<Node>();
		this.closed = new ArrayList<Node>();
		this.path = new ArrayList<Node>();
	}

	public AStar(int[][] maze, int xstart, int ystart) {
		this.open = new ArrayList<Node>();
		this.closed = new ArrayList<Node>();
		this.path = new ArrayList<Node>();
		AStar.maze = maze;
		this.now = new Node(null, xstart, ystart, 0, 0);
		AStar.xstart = xstart;
		AStar.ystart = ystart;
	}

	public List<Node> findPathTo(int xend, int yend) {
		AStar.xend = xend;
		AStar.yend = yend;
		this.closed.add(this.now); // add current node to closed list because we expanded it

		addNeigborsToOpenList(); // add neighbours of current node to open list for further potential expansion

		while (this.now.x != AStar.xend || this.now.y != AStar.yend) {
			if (this.open.isEmpty()) {
				return null;
			}

			this.now = this.open.get(0); // get first node (lowest f score)
			this.open.remove(0);
			this.closed.add(this.now); // and add to the closed

			addNeigborsToOpenList();
		}

		this.path.add(0, this.now);

		while (this.now.x != AStar.xstart || this.now.y != AStar.ystart) {
			this.now = this.now.parent;
			this.path.add(0, this.now);
		}
		return this.path;
	}

	private static boolean findNeighborInList(List<Node> array, Node node) {
		for (Node e : array) {
			if (e.x == node.x && e.y == node.y) {
				return true;
			}
		}
		return false;
	}

	private double distance(int dx, int dy) {
		return Math.abs(this.now.x + dx - AStar.xend) + Math.abs(this.now.y + dy - AStar.yend); // return "Manhattan
																								// distance"
	}

	private void addNeigborsToOpenList() {
		Node node;

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x != 0 && y != 0) {
					continue;
				}

				node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));

				if ((x != 0 || y != 0) // not this.now
						&& this.now.x + x >= 0 && this.now.x + x < AStar.maze[0].length // check maze boundaries
						&& this.now.y + y >= 0 && this.now.y + y < AStar.maze.length
						&& AStar.maze[this.now.y + y][this.now.x + x] != -1 && !findNeighborInList(this.open, node)
						&& !findNeighborInList(this.closed, node)) { // if not already done
					node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
					node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square

					this.open.add(node);
				}
			}
		}
		Collections.sort(this.open);
	}


	public List<Node> getOptimalPath(int xstart, int ystart, int xend, int yend) {
		List<String> commandsList = new ArrayList<>();
		AStar as = new AStar(maze, xstart, ystart);
		
		optimalPath = as.findPathTo(xend, yend);
		
		return optimalPath;
	}
	
	
	 public List<String> generateListOfCommands() {
		List<String> commandsList = new ArrayList<String>();
		int currentDirection = 0;
		int nextDirection = 0;
		List<Node> path = new ArrayList<>();
		
		LocationOrder order = new LocationOrder();
		List<Integer> indexes = order.computeOrderOfLocations();

		for (int i = 0; i < indexes.size() - 1; i++) {
			Location currentLocation = locations[indexes.get(i)];
			Location nextLocation = locations[indexes.get(i+1)];

			path = getOptimalPath(currentLocation.x, currentLocation.y, nextLocation.x, nextLocation.y);

			System.out.println();

			for (int a = 0; a < path.size() - 1; a++) {
				Node currentNode = path.get(a);
				Node nextNode = path.get(a + 1);

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
						commandsList.add("-90");
						break;
					case NORTH:
						commandsList.add("90");
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
						commandsList.add("90");
						break;
					case WEST:
						commandsList.add("-90");
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
						commandsList.add("90");
						break;
					case NORTH:
						commandsList.add("-90");
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
						commandsList.add("-90");
						break;
					case WEST:
						commandsList.add("90");
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

			}

			// for(String command: commandsList) {
			// System.out.println();
			// System.out.print(command);
			// }

			// commandsList.clear();
			// System.out.println();

		}
		return commandsList;
	}
}

// public static void main(String[] args) {
// JFrame frame = new JFrame("Paramedic Navigation");
// JTable table = new JTable(6,6);
// DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

// centerRenderer.setHorizontalAlignment(JLabel.CENTER);
// table.setRowHeight(50);
// table.setFont(new java.awt.Font("", 1, 20));
// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// frame.setSize(500,500);
// frame.getContentPane().add(table);
// frame.setLocation(700, 200);
// frame.setVisible(true);

// Location H = new Location("H", 0, 0);
// Location A = new Location("A", 1, 3);
// Location B = new Location("B", 2, 4);
// Location C = new Location("C", 5, 3);
// Location D = new Location("D", 4, 2);
// Location E = new Location("E", 1, 5);
//
// Location[] locations = {H, A, B, C, D, E};

// Obstacle o1 = new Obstacle(2,5);
// Obstacle o2 = new Obstacle(1,2);
//
// Victim v1 = new Victim(3,2);
// Victim v2 = new Victim(5,1);
// Victim v3 = new Victim(2,6);

// List<Obstacle> obstacles = new ArrayList<>();
// obstacles.add(o1);
// obstacles.add(o2);

// Victim[] victims = {v1, v2, v3};

// LocationOrder order = new LocationOrder();
// order.computeCostsBetweenLocations(H, A, B, C, D, E);
// List<Integer> locationsOrder = order.computeOrderOfLocations();
// generateListOfCommands(locations, locationsOrder);

// for(int i = 0; i < costs.length; i++) {
// for(int j = 0; j < costs[i].length; j++) {
// table.setValueAt(AStar.costs[i][j], i, j);
// table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
// }
// }
// }