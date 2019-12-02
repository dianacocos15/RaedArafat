
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
		int distance = 0;
		/*Rotate and correct action codes*/
		switch (code) {
			case "90": rotation = 90;
				break;
			case "-90": rotation = -90;
				break;
			case "180": rotation = 180;
				break;
			case "-180": rotation = -180;
				break;
			case "travel": distance = 1;
				break;
			default: return "1";
		}
	
		move.rotate(rotation);
		move.travelCellDistance(distance);
		
		return "0";
		
		/*Cell localise action codes*/
//		if(code == 11) {
//			correct.lineLocalisationToLeft();
//			return "0";
//		}
//		if(code == 12) {
//			correct.lineLocalisationToRight();
//			return "0";
//		}
		
		/*Move cells action codes*
		
		/*Color sense action code*/
//		if(code == 100) {
//			Color color = robot.getColor();
//			return ""+color;
//		}
	}

}
