
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
		
		int code = Integer.parseInt(command);
		System.out.println("Code is : " +code);
		
		/*Rotate and correct action codes*/
		if(code == 90) {
			correct.rotateAndLineCorrect(90);
			return "0";
		}
		if(code == -90) {
			correct.rotateAndLineCorrect(-90);
			return "0";
		}
		if(code == 180) {
			correct.rotateAndLineCorrect(180);
			return "0";
		}
		
		/*Cell localise action codes*/
		if(code == 11) {
			correct.lineLocalisationToLeft();
			return "0";
		}
		if(code == 12) {
			correct.lineLocalisationToRight();
			return "0";
		}
		
		/*Move cells action codes*/
		if(code <= 6 && code >=1) {
			move.travelCellDistance(code);
			return "0";
		}
		
		/*Color sense action code*/
		if(code == 100) {
			Color color = robot.getColor();
			return ""+color;
		}
		
		return "1";
	}

}
