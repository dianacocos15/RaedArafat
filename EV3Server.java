
import java.io.*;
import java.net.*;

public class EV3Server extends Thread {
	public static final int port = 1234;
	DataOutputStream dOut;
	OutputStream out;
	
	DataInputStream dIn;
	InputStream in;
	
	ServerSocket server;
	Socket client;
	
	/*Thread run function, loops.*/
	public void run(PilotRobot me){
		
		System.out.println("Connecting...");
		
		/*Ensure connected to client*/
		while(true) {
			if(connectedToClient()) {
				break;
			}
		}
		
		System.out.println("Connected!");
		
		/*Retrieve action commands and performs them. Then send appropriate values.*/
		ActionHandler action = new ActionHandler(me);
		while(true) {
			String action_code = readValue();
			
			if(action_code != null) {
				String response = action.performAction(action_code);
				try {
					dOut.writeUTF(response);
				} catch (IOException e) {}
			}
			
		}
		
	}
	
	/*
	 * Reads a action code*/
	public String readValue() {
		String action_code = null;
		while (true) {
			try {
				action_code = dIn.readUTF();
			} catch (IOException e) {}
			
			if(action_code != null) {
				return action_code;
			}
		}
	}
	
	
	/*Tries to connect and returns connection status*/
	public boolean connectedToClient() {
		try {
			
			/*Connect to client*/
			server = new ServerSocket(port);
			client = server.accept();
			
			/*Initialise output streams*/
			out = client.getOutputStream();
			dOut = new DataOutputStream(out);
			
			/*Initialise input streams*/
			in = client.getInputStream();
			dIn = new DataInputStream(in);
			
			return client.isConnected();
		}catch(IOException e) {
			System.out.println(e);
		}
		
		return false;
	}
	
	
	
	
	
}