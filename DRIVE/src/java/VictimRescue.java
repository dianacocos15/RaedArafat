//import java.util.ArrayList;
//import java.util.List;
//
//public class VictimRescue {
//	 Location H = new Location("H", 0, 0);
//	 Location A = new Location("A", 1, 3);
//	 Location B = new Location("B", 2, 4);
//	 Location C = new Location("C", 5, 3);
//	 Location D = new Location("D", 4, 2);
//	 Location E = new Location("E", 1, 5);
//	    
//	 Location[] locations = {H, A, B, C, D, E};
//	 
//	 private final static int EAST = 0;
//	 private final static int SOUTH = 1;
//	 private final static int WEST = 2;
//	 private final static int NORTH = 3;
//	    
//	 LocationOrder order = new LocationOrder();
//	 Location currentLocation;
//	 Location nextLocation;
//	    
//	 List<Integer> indexes = order.computeOrderOfLocations();
//	 
//	 AStar astar = new AStar();
//	 
//	 public VictimRescue() {}
//	    
//	    
//	 class Location {
//		 public String name;
//	     public int x;
//	     public int y;
//
//	     Location(String name, int x, int y) {
//	    	 this.name = name;
//	         this.x = x;
//	         this.y = y;
//	     }
//	 }
//	    
//	 class LocationOrder {
//		int nextLocation = 0;
//	    int currentLocation = 0;
//	    boolean[] wasLocationVisited = {false, false, false, false, false, false};
//	    List<Integer>locationsOrder = new ArrayList<Integer>();
//	    double distance;
//	       
//	    public Double[][] computeCostsBetweenLocations() {
//	    		
//	    	for(int i = 0; i < locations.length; i++) {
//	    		for (int j = 0; j < locations.length; j++) {	
//	    			
//	    			distance = astar.getOptimalPath(locations[i].x, locations[i].y,+ locations[j].x, locations[j].y).get(astar.optimalPath.size() - 1).g;
//
//	                    //if value of rows and columns is the same, e.g. H == H
//	                    //then set that value to 100 to avoid including it in the optimal path
//	                if (locations[i].x == locations[j].x && locations[i].y == locations[j].y) {
//	                	astar.costs[i][j] = 100.00;
//	                }
//	                else {
//	                    astar.costs[i][j] = distance;
//	                }
//	                    
//	                    //System.out.print(costs[i][j]);
//	                    //System.out.print(" ");
////	                    table.setValueAt(AStar.costs[i][j], i, j);
////	                    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//	                }
//
//	               // System.out.println();
//
//	            }
//	    		
//	    		return astar.costs;
//	    	}
//	    	
//	    	public List<Integer> computeOrderOfLocations() {
//	    		Double[][] costs = computeCostsBetweenLocations();
//	    		for (int i = currentLocation; i < 6; i++) {
//	                double smallestCost = Double.MAX_VALUE;
//	                locationsOrder.add(currentLocation);
//	                wasLocationVisited[currentLocation] = true;
//	                
//	                for (int j = 0; j < 6; j++) {
//	                	if (locationsOrder.contains(j)) continue;
//	                	else {
//	    	            	if (costs[currentLocation][j] < smallestCost) {
//	    	                    smallestCost = costs[currentLocation][j];
//	    	                    nextLocation = j;
//	    	                }
//
//	    	            	wasLocationVisited[nextLocation] = true;        
//	                	}
//	                }
//	                //System.out.println("Location " + currentLocation + " smallest cost " + smallestCost + ". Next location is " + nextLocation);          
//	                	currentLocation = nextLocation;
//	           }
//	            
//	            locationsOrder.add(0);
//	        
//	            System.out.println();
//	            
//	            return locationsOrder;
//	    	}
//	    }
//	 
//	 private void goToNextUnvisitedLocation() {	    	
//		    for (int x = 0; x < indexes.size()-1; x++) {
//				if (order.wasLocationVisited[x+1] == true) continue;
//				else nextLocation = locations[indexes.get(x+1)];
//			}
//	    }
//	    
//	 public List<AStar.Node> generatePathBetweenLocations() {
//	    	ParamedicEnv env = new ParamedicEnv();
//	    	Mission mission = new Mission(6, env);
//	    	
//			List<AStar.Node> path = new ArrayList<AStar.Node>();
//			
//			
//			for (int i = 0; i < indexes.size()-1; i++) {
//				currentLocation = locations[indexes.get(i)];
//				//nextLocation = locations[indexes.get(i+1)];
//				
//				
//				//if current location == HOSPITAL
//				//check for next unvisited location and set it as the next location
//				if (currentLocation.x == 0 && currentLocation.y == 0) {
//					mission.addHospital(currentLocation.x, currentLocation.y);
//					goToNextUnvisitedLocation();
//				}
//				
//				else {
//					mission.addPotentialVictim(currentLocation.x, currentLocation.y);
//					
//					if (mission.isVictim == true) {
//						
//						if (mission.isVictimCritical == true) {
//							mission.foundCritical(currentLocation.x, currentLocation.y);
//							nextLocation = locations[indexes.get(0)];
//						} else {
//							mission.foundNonCritical(currentLocation.x, currentLocation.y);
//							goToNextUnvisitedLocation();
//						}
//					} 
//					
//					else {
//						mission.foundNoVictim(currentLocation.x, currentLocation.y);
//						goToNextUnvisitedLocation();
//					}
//				}
//				
//	
//				path = astar.getOptimalPath(currentLocation.x, currentLocation.y, nextLocation.x, nextLocation.y);
//				System.out.println();
//			}
//			
//			return path;
//			
//	    }
//	 
//	 public List<String> generateListOfCommands(List<AStar.Node> path) {
//		int currentDirection = 0;
//		int nextDirection = 0;
//		List<String> commandsList = new ArrayList<String>();
//			
//			for (int a = 0; a < path.size()-1; a++) {
//				AStar.Node currentNode = path.get(a);
//				AStar.Node nextNode = path.get(a+1);
//				
//				int currentX = currentNode.x;
//				int currentY = currentNode.y;
//				
//				int nextX = nextNode.x;
//				int nextY = nextNode.y;
//				
//				//0 = EAST
//				//1 = SOUTH
//				//2 = WEST
//				//3 = NORTH
//
//				//y changes
//				if (currentX == nextX) {
//					if (currentY > nextY) nextDirection = 1;
//					else nextDirection = 3;	
//				}
//				
//				//x changes
//				else {
//					if (currentX > nextX) nextDirection = 2;
//					else nextDirection = 0;
//				}
//		
//				switch (currentDirection) {
//				
//					case EAST: {
//						switch (nextDirection) {
//							case SOUTH: commandsList.add("-90");
//								break;
//							case NORTH: commandsList.add("90");
//								break;
//							case WEST: commandsList.add("-180");
//								break;
//							default:
//								break;
//						}
//					}			
//					break;
//					
//					case SOUTH: {
//						switch (nextDirection) {
//							case EAST: commandsList.add("90");
//								break;
//							case WEST: commandsList.add("-90");	
//								break;
//							case NORTH: commandsList.add("180");
//								break;
//							default:
//								break;
//						}
//					}
//					break;
//					
//					case WEST: {
//						switch (nextDirection) {
//							case SOUTH: commandsList.add("90");
//								break;
//							case NORTH: commandsList.add("-90");
//								break;
//							case EAST: commandsList.add("180");
//								break;
//							default:
//								break;
//						}
//					}
//					break;
//					
//					case NORTH: {
//						switch (nextDirection) {
//							case EAST: commandsList.add("-90");
//								break;
//							case WEST: commandsList.add("90");
//								break;
//							case SOUTH: 
//								commandsList.add("-180");
//								break;
//							default:
//								break;
//						}
//					}
//					break;
//				}	
//				
//				commandsList.add("travel");
//				currentDirection = nextDirection;
//
//			}		
//			
////			for(String command: commandsList) {
////				System.out.println();
////				System.out.print(command);
////			}
//			
//			//commandsList.clear();
//			//System.out.println();
//			
//		return commandsList;
//	 }
//}
