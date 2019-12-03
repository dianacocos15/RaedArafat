//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PCMain {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		PCClient pc = new PCClient();
//		System.out.println("Connecting....");
//		while (true) {
//			if (pc.connectClient()) {
//				break;
//			}
//		}
//
//		AStar astar = new AStar();
//		List<String> commands = astar.generateListOfCommands();
//		
//		//List<String> commands = new ArrayList<>();
//		//commands.add("Got one command");
//
//		for (String command : commands) {
//			System.out.println(command);
//		}
//
//		try {
//			pc.sendMultipleCommands(commands);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Exiting");
//	}
//
//}
