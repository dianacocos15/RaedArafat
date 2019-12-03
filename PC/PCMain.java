package PC;

public class PCMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PCClient pc = new PCClient();
		System.out.println("Connecting....");
		while(true) {
			if(pc.connectClient()) {
				break;
			}
		}
		
		String[] commands = {"2","12","2","100", "180", "2", "90", "2", "100", "1", "90", "4", "100"};
		pc.sendMultipleCommands(commands);
		System.out.println("Exiting");
	}

}
