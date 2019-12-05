import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AStar {
	private final List<Node> open; // list of unexpanded neighbours
	private final List<Node> closed; // list of expanded neighbours
	private final List<Node> path;
	static int[][] maze = { 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0 },
			};
	private Node now;
	static final int xstart = 0;
	static final int ystart = 0;
	private static int xend, yend;
	
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
	
	public AStar(int[][] maze) {
		this.open = new ArrayList<Node>();
		this.closed = new ArrayList<Node>();
		this.path = new ArrayList<Node>();
		this.now = new Node(null, xstart, ystart, 0, 0);
		this.maze = maze;
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

		while (this.now.x != this.xstart || this.now.y != this.ystart) {
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
						&& this.now.x + x >= 0 && this.now.x + x < maze[0].length // check maze boundaries
						&& this.now.y + y >= 0 && this.now.y + y < maze.length
						&& maze[this.now.y + y][this.now.x + x] != -1 && !findNeighborInList(this.open, node)
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
		AStar as = new AStar(maze);
		List<Node> path = as.findPathTo(xend, yend);
		
		if (path != null) {
        	for(Node n: path) {
        		//System.out.print("[" + n.x + ", " + n.y + "] ");
        		maze[n.y][n.x] = -1;
        	}
        	
        	for (int[] maze_row : maze) {
                for (int maze_entry : maze_row) {
                    switch (maze_entry) {
                        case 0:
                            System.out.print("_");
                            break;
                        case -1:
                            System.out.print("*");
                            break;
                        default:
                            System.out.print("#");
                    }
                }
                System.out.println();
            }
            return path;
        }
        	return null;
	}
	
	public static void main(String[] args) {
		int[][] maze = {
            {  0,  0,  0,  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0,  0,  0,  0},
            {  0,  0,  0,100,100,100,  0,  0},
            {  0,  0,  0,  0,  0,100,  0,  0},
            {  0,  0,100,  0,  0,100,  0,  0},
            {  0,  0,100,  0,  0,100,  0,  0},
            {  0,  0,100,100,100,100,  0,  0},
            {  0,  0,  0,  0,  0,  0,  0,  0},
        };
		
		AStar as = new AStar(maze);
		
		as.getOptimalPath(0,0,5,5);
	}
}