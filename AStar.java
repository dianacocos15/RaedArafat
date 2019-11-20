import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
 
public class AStar {
    private final List<Node> open; //list of unexpanded neighbours
    private final List<Node> closed; //list of expanded neighbours
    private final List<Node> path;
    private static int[][] maze = {
    	  {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
          {  0,  0,  0,  0,  0,  0,  0,  0},
        };
    private Node now;
    private static int xstart = 0;
    private static int ystart = 0;
    private static int xend, yend;
    static Integer [][] costs = new Integer[6][6];
 
    // Node class for convenience
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
       // Compare by f value (g + h)
       @Override
       public int compareTo(Object o) {
           Node that = (Node) o;
           return (int)((this.g + this.h) - (that.g + that.h));
       }
   }
    
   static class Location {
	   public int x;
	   public int y;
	   
	   Location(int x, int y) {
		   this.x = x;
		   this.y = y;
	   }
   }
   
   static class Obstacle {
	   public int x;
	   public int y;
	   
	   Obstacle(int x, int y){
		   this.x = x;
		   this.y = y;
	   }
   }
   
   static class Victim {
	   public int x;
	   public int y;
	   
	   Victim(int x, int y){
		   this.x = x;
		   this.y = y;
	   }
   }
 
    AStar(int[][] maze, int xstart, int ystart) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.maze = maze;
        this.now = new Node(null, xstart, ystart, 0, 0);
        this.xstart = xstart;
        this.ystart = ystart;
    }
    /*
    ** Finds path to xend/yend or returns null
    **
    ** @param (int) xend coordinates of the target position
    ** @param (int) yend
    *
    ** @return (List<Node> | null) the path
    */
    public List<Node> findPathTo(int xend, int yend) {
        this.xend = xend;
        this.yend = yend;
        this.closed.add(this.now); //add current node to closed list because we expanded it
        addNeigborsToOpenList(); //add neighbours of current node to open list for further potential expansion
        
        /*
         *	while the current position of the node is not the destination 
         * 	if the open list if empty
         * 	return a null list because there are no neighbours to visit
         */
        while (this.now.x != this.xend || this.now.y != this.yend) { 
            if (this.open.isEmpty()) { 
                return null;
            }
            
            //get the first node of the open list - the lowest F value (lowest H + G)
            this.now = this.open.get(0); // get first node (lowest f score)
            
            //remove the node with the lowest F value (first node)
            this.open.remove(0); 
            
            //add this node to the closed list because we expanded it
            this.closed.add(this.now); // and add to the closed
            
            //
            addNeigborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.xstart || this.now.y != this.ystart) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        return this.path;
    }
    /*
    ** Looks in a given List<> for a node
    **
    ** @return (bool) NeightborInListFound
    */
    private static boolean findNeighborInList(List<Node> array, Node node) {
        //return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
        
    	for(Node e: array) {
    		if(e.x == node.x && e.y == node.y) {
    			return true;
    		}
    	}
    	
    	return false;     		
    }
    /*
    ** Calulate distance between this.now and xend/yend
    **
    ** @return (int) distance
    */
    private double distance(int dx, int dy) {
       
        	return Math.abs(this.now.x + dx - this.xend) + Math.abs(this.now.y + dy - this.yend); // return "Manhattan distance"
    }
    
    private void addNeigborsToOpenList() {
        Node node;
        
        //check each neighbour of the current node
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
            	
            	//check diagonal
            	//if diagonal is true and we're not checking the current node this particular iteration
//                if (!this.diag && x != 0 && y != 0) {
//                	//
//                    continue; // skip if diagonal movement is not allowed
//                }
                
                if (x != 0 && y != 0) {
                	//
                    continue; // skip if diagonal movement is not allowed
                }
                
                
                //the following node is an instance of a neighbour
                //G = from start to current node
                //distance from current node(neighbour) to destination
                //this calculates the distance from a neighbour to destination - MANHATTAN DISTANCE
                node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));
                
                //if not current node
                //and neighbour on x coordinate is not a boundary
                //and neighbour on y coordinate is not boundary
                //and if cell was not visited (is part of path)
                //neighbour was not found in closed list
                if ((x != 0 || y != 0) // not this.now
                    && this.now.x + x >= 0 && this.now.x + x < this.maze[0].length // check maze boundaries
                    && this.now.y + y >= 0 && this.now.y + y < this.maze.length
                    && this.maze[this.now.y + y][this.now.x + x] != -1 
                    && !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
                        node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
                        node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square
                        
                        //add node to open list
                        this.open.add(node);
                }
            }
        }
        Collections.sort(this.open);
    }
 
    //public static void main(String[] args) {
        // -1 = blocked
        // 0+ = additional movement cost
//        int[][] maze = {
//            {  0,  0,  0,  0,100,  0,  0,  0},
//            {  0,  0,  0,  0,  0,  0,  0,  0},
//            {  0,  0,  0,100,100,100,  0,  0},
//            {  0,  0,  0,  0,  0,100,  0,  0},
//            {  0,  0,100,  0,  0,100,  0,  0},
//            {  0,  0,100,  0,  0,100,  0,  0},
//            {  0,  0,100,100,100,100,  0,  0},
//            {  0,  0,100,  0,  0,  0,  0,  0},
//        }
    
    public static Integer runAlgorithm(int xstart, int ystart, int xend, int yend) {
        AStar as = new AStar(maze, xstart, ystart);
        List<Node> path = as.findPathTo(xend, yend);
//        if (path != null) {
//        	for(Node n: path) {
//        		//System.out.print("[" + n.x + ", " + n.y + "] ");
//        		maze[n.y][n.x] = -1;
//        	}
        		//System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);
 
//            for (int[] maze_row : maze) {
//                for (int maze_entry : maze_row) {
//                    switch (maze_entry) {
//                        case 0:
//                            System.out.print("_");
//                            break;
//                        case -1:
//                            System.out.print("*");
//                            break;
//                        default:
//                            System.out.print("#");
//                    }
//                }
//                System.out.println();
//            }
        //}
        return (int)path.get(path.size() - 1).g;
    }
    
    public static void main(String[] args) {
    	JFrame frame = new JFrame("Paramedic Navigation");
        JTable table = new JTable(6,6);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setRowHeight(50);
    	table.setFont(new java.awt.Font("", 1, 20));
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.getContentPane().add(table); 
        frame.setLocation(700, 200);
        frame.setVisible(true);
        
    	Location H = new Location(0,0);
    	Location A = new Location(1,3);
    	Location B = new Location(2,4);
    	Location C = new Location(2,1);
    	Location D = new Location(3,1);
    	Location E = new Location(4,2);
    	
    	Obstacle o1 = new Obstacle(3,5);
    	Obstacle o2 = new Obstacle(1,2);
    	
    	Victim v1 = new Victim(1,1);
    	Victim v2 = new Victim(4,4);
    	
    	Location[] locations = {H, A, B, C, D, E};
    	
    	List<Obstacle> obstacles = new ArrayList<>();
    	obstacles.add(o1);
    	obstacles.add(o2);
    	
    	List<Victim> victims = new ArrayList<>();
    	victims.add(v1);
    	victims.add(v2);
    	
    	//Integer [][] costs = new Integer[6][6];
    	
    	for(int i = 0; i < locations.length; i++) {
    		for (int j = 0; j < locations.length; j++) {
    			costs[i][j] = runAlgorithm(locations[i].x, locations[i].y,+ locations[j].x, locations[j].y);
    			for(int x = 0; x < obstacles.size(); x++) {
    		    	if (i == obstacles.get(x).x && j == obstacles.get(x).y) {
    		    		costs[i][j] = 100;
    		    	}
    		    }
    			
    			for(int x = 0; x < victims.size(); x++) {
    		    	if (i == victims.get(x).x && j == victims.get(x).y) {
    		    		costs[i][j] = -1;
    		    	}
    		    }
    			
    			System.out.print(costs[i][j]);
    			System.out.print(" ");
    			table.setValueAt(AStar.costs[i][j], i, j);
				table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

    		}
    		System.out.println();
    	}
    	
    	}
}