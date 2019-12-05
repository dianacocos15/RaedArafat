import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JTable;
//import javax.swing.table.DefaultTableCellRenderer;
//import lejos.hardware.Sound;
//import lejos.hardware.ev3.LocalEV3;
//import lejos.hardware.lcd.GraphicsLCD;
//import lejos.robotics.navigation.MovePilot;
//import lejos.robotics.subsumption.Behavior;
//import lejos.robotics.*;

public class PCClient {
	DataInputStream dIn;
	DataOutputStream dOut;
	InputStream in;
	OutputStream out;
	Socket sock;

	/*
	 * Connect with a chosen ip
	 **/
	public boolean connectClient(String address) {
		try {
			sock = new Socket(address, 1234);
			System.out.println("Connected");
			out = sock.getOutputStream();
			dOut = new DataOutputStream(out);
			in = sock.getInputStream();
			dIn = new DataInputStream(in);

			if (sock.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return false;
	}

	/*
	 * Connect with default ip
	 */
	public boolean connectClient() {
		String ip = "192.168.70.205";
		return connectClient(ip);
	}

	/*
	 * Send a string command
	 */
	public boolean sendCommand(String command) throws IOException {
		dOut.writeUTF(command);
		System.out.println(command);
		String response = dIn.readUTF();
		System.out.println(response);
		return true;
	}

	/*
	 * Send a string command
	 */
	public boolean sendMultipleCommands(List<String> commands) {
		/*Remove any travel commands and replace with numbers*/
		int newPos = 0;
		List<String> newCommands = new ArrayList<>();
		
		/*Populate new command array*/
		for(int pos = 0; pos< commands.size() ; pos++) {
			/*Get count of travel statements found*/
			if(commands.get(pos).equals("travel")) {
				newCommands.add("" + getNumber(commands,pos));
				pos = pos + getNumber(commands,pos) - 1;
				newPos +=1;
			}
			else {
				newCommands.add(commands.get(pos));
				newPos +=1;
			}
		}
		
		
		try {
			for (String command : newCommands) {
				sendCommand(command);
			}
			return true;
		} catch (IOException e) {}
		
		return false;
	}
	
	
	private int getNumber(List<String> commands, int position) {
		// TODO Auto-generated method stub
		int count = 0;
		for(int pos = position; pos < commands.size() ; pos++ ) {
			if(commands.get(pos).equals("travel")) {
				count++;
			}
			else {
				break;
			}
		}
		return count;
	}

	/*
	 * Get a response from the PC
	 */
	public String readValue() throws IOException {
		String response = dIn.readUTF();

		return response;
	}

	/*
	 * Return boolean connected status
	 */
	public boolean checkConnected() {
		return sock.isConnected();
	}

	/*
	 * Rotate right and correct
	 */
	public boolean rotateRightWithCorrect() {
		try {
			dOut.writeUTF("90");
			dIn.readUTF();
			return true;
		} catch (IOException e) {
		}

		return false;
	}

	/*
	 * Rotate left and correct
	 */
	public boolean rotateLeftWithCorrect() {
		try {
			dOut.writeUTF("-90");
			dIn.readUTF();
			return true;
		} catch (IOException e) {
		}

		return false;
	}

	/*
	 * Rotate 180 degrees
	 */
	public boolean rotate180WithCorrect() {
		try {
			dOut.writeUTF("180");
			dIn.readUTF();
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	/*
	 * Travels an integer number of squares
	 */
	public boolean travel(int cells) {
		try {
			dOut.writeUTF("" + cells);
			dIn.readUTF();
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public String getColor() {
		try {
			dOut.writeUTF("Color");
			return dIn.readUTF();
		} catch (IOException e) {
		}
		return null;
	}

}