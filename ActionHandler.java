
public class ActionHandler {

	PilotRobot robot;
	Movement move;
	Corrections correct;
	DoomTheme dt;
	
	public ActionHandler(PilotRobot me) {
		// TODO Auto-generated constructor stub
		robot = me;
		move = new Movement(me);
		correct = new Corrections(me);
	}
	
	/**
	 * 
	 * @param command String code corresponding to action
	 * @return 0 for success 1 for failure
	 */
	public String performAction(String command) {
		command = command.replaceAll("\\s+","");
		
		String code = command;
		System.out.println("Code is : " + code);
		
		int rotation = 0;
		int cell_distance = 0;
		
		/*Rotate and correct action codes*/
		switch (code) {
			/*Rotation statements*/
			case "90": rotation = 90;
				break;
			case "-90": rotation = -90;
				break;
			case "180": rotation = 180;
				break;
			case "-180": rotation = -180;
				break;
		}
		
		/*Ensure rotation not 0 before travelling*/
		if(rotation !=0) {
			correct.rotateAndLineCorrect(rotation);
			return "0";
		}
		
		/*Travel statements*/
		switch(code) {
			case "1": cell_distance = 1;
				break;
			case "2": cell_distance = 2;
				break;
			case "3": cell_distance = 3;
				break;
			case "4": cell_distance = 4;
				break;
			case "5": cell_distance = 5;
				break;
			case "travel": cell_distance = 1;
				break;
		}
		
		/*Ensure distance not 0 before travelling*/
		if(cell_distance != 0) {
			move.travelCellDistance(cell_distance);
			return "0";
		}
		
		/*Additional commands*/
		switch(code) {
			/*Color sensor command*/
			case "100":
				Color color = robot.getColor();
				return ""+color;
			/*Localise left*/
			case "11":
				correct.lineLocalisationToLeft();
				return "0";
			/*Localise Right*/
			case "12":
				correct.lineLocalisationToRight();
				return "0";
		}
		
		return "1";
	}

}
