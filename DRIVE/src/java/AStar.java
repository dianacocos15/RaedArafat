import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AStar {
	int[][] maze;
	
	public AStar(int[][] maze) {
		this.maze = maze;
	}
	
	public void printPath(List<Node> path){
		int[][] localMaze = new int[6][6];
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 6; y++){
				switch(maze[x][y]){
					case 0:
						localMaze[x][y] = 0;
						break;
					case 100:
						localMaze[x][y] = 2;
						break;
				}
			}
		}

		for(Node n: path) {
        	localMaze[n.y][n.x] = -1;
        }

		for (int[] maze_row : localMaze) {
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
						break;
                }
            }
        	System.out.println();
        }
	}

	public List<Node> findPathTo(int xstart, int ystart, int xend, int yend) {
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		List<Node> path = new ArrayList<Node>();
		Node currentNode = new Node(null, xstart, ystart, 0, 0);
		
		closed.add(currentNode); // add current node to closed list because we expanded it
		open = addNeigborsToOpenList(currentNode, open, closed, xend, yend); // add neighbours of current node to open list for further potential expansion

		while (currentNode.x != xend || currentNode.y != yend) {
			if (open.isEmpty()) {
				return null;
			}

			currentNode = open.get(0); // get first node (lowest f score)
			open.remove(0);
			closed.add(currentNode); // and add to the closed

			open = addNeigborsToOpenList(currentNode, open, closed, xend, yend);
		}

		

		while (currentNode.x != xstart || currentNode.y != ystart) {
			path.add(0, currentNode);
			currentNode = currentNode.parent;
		}

		path.add(0, currentNode);

		return path;
	}

	private static boolean findNeighborInList(List<Node> array, Node node) {
		for (Node e : array) {
			if (e.x == node.x && e.y == node.y) {
				return true;
			}
		}
		return false;
	}

	private double distance(Node currentNode, int dx, int dy, int xend, int yend) {
		return Math.abs(currentNode.x + dx - xend) + Math.abs(currentNode.y + dy - yend); // return "Manhattan
																								// distance"
	}

	private ArrayList<Node> addNeigborsToOpenList(Node currentNode, ArrayList<Node> open, ArrayList<Node> closed, int xend, int yend) {
		Node node;

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x != 0 && y != 0) {
					continue;
				}

				node = new Node(currentNode, currentNode.x + x, currentNode.y + y, currentNode.g, distance(currentNode, x, y, xend, yend));

				if ((x != 0 || y != 0) // not this.now
						&& currentNode.x + x >= 0 && currentNode.x + x < maze[0].length // check maze boundaries
						&& currentNode.y + y >= 0 && currentNode.y + y < maze.length
						&& maze[currentNode.y + y][currentNode.x + x] != -1 && !findNeighborInList(open, node)
						&& !findNeighborInList(closed, node)) { // if not already done
					node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
					node.g += maze[currentNode.y + y][currentNode.x + x]; // add movement cost for this square

					open.add(node);
				}
			}
		}
		Collections.sort(open);

		return open;
	}
	
	public List<Node> getOptimalPath(int xstart, int ystart, int xend, int yend) {
		List<Node> path = findPathTo(xstart, ystart, xend, yend);
		printPath(path);

		System.out.println("\n Path be : "); 
		for(Node n : path){
			System.out.print(n.x + "," + n.y + " ");
		}

		return path;
	}
}
		