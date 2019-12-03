/**
 * 
 */
package PC;

/**
 * @author sgmmunsh
 *
 */
public class Travel {
	public static String[][] grid = {{"0","0","0","0","0","0"},
								     {"0","0","0","0","0","0"},
									 {"0","0","0","0","0","0"},
									 {"0","X","0","0","0","0"},
									 {"0","0","0","0","0","0"},
									 {"0","0","0","0","0","0"}};
	
	
	//Grid cells start at 0
	//Down is x
	//Across is y
	/*
	 * Grid accessed as [x][y]
	 * */
	public Travel(String[][] map) {
		this.grid = map;
	}
	
	public Travel() {
		
	}
	
	//Entree value is either 90 or -90
	public boolean checkCorrectionRotationFree(String rotate, int orientation, int position_x , int position_y) {
		boolean north_cell_free = checkCellFree(position_x, position_y + 1);
		boolean east_cell_free = checkCellFree(position_x + 1, position_y);
		boolean west_cell_free = checkCellFree(position_x - 1, position_y);
		boolean south_cell_free = checkCellFree(position_x, position_y - 1);
		
		
		if(rotate == "90") {
			//north
			if(orientation == 3) {
				if(north_cell_free && east_cell_free) {
					return  true;
				}
			}
			//east
			if(orientation == 0) {
				if(south_cell_free && east_cell_free) {
					return  true;
				}
			}
			//south
			if(orientation == 1) {
				if(south_cell_free && west_cell_free) {
					return  true;
				}
			}
			//west
			if(orientation == 2) {
				if(north_cell_free && west_cell_free) {
					return  true;
				}
			}
		}
		else if(rotate == "-90") {
			//north
			if(orientation == 3) {
				if(north_cell_free && west_cell_free) {
					return  true;
				}
			}
			//east
			if(orientation == 0) {
				if(north_cell_free && east_cell_free) {
					return  true;
				}
			}
			//south
			if(orientation == 1) {
				if(south_cell_free && east_cell_free) {
					return  true;
				}
			}
			//west
			if(orientation == 2) {
				if(south_cell_free && west_cell_free) {
					return  true;
				}
			}
		}
		
		return false;
	}
	
	/*
	 * Check cell of coordinate x and y is free
	 * */
	public boolean checkCellFree(int x, int y) {
		/*Returns false if x or y is out of bounds*/
		if(x >= grid.length || x < 0) {
			return false;
		}
		if(y >= grid[0].length || y < 0) {
			return false;
		}
		
		/*Return true if cell is free*/
		if(grid[x][y] == "0") {
			return true;
		}
		return false;
	}
	
}
