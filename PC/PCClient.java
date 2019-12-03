package PC;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;

public class PCClient {
	DataInputStream dIn;
	DataOutputStream dOut;
	InputStream in;
	OutputStream out;
	Socket sock;
	
	/*
	 * Connect with a chosen ip
	 * */
	public boolean connectClient(String address){
			try {
				sock = new Socket(address, 1234);
				System.out.println("Connected");
				in = sock.getInputStream();
				out = sock.getOutputStream();
				dIn = new DataInputStream(in);
				dOut = new DataOutputStream(out);
				
				if (sock.isConnected()) {
					return true;
				}
			}catch(Exception e) {
				System.out.println(e);
			}
			
		return false;
	}
	
	/*
	 * Connect with default ip
	 * */
	public boolean connectClient(){
		String ip = "192.168.70.205"; 
		return connectClient(ip);
	}
	
	/*
	 * Send a string command
	 * */
	public boolean sendCommand(String command){
		while(true) {
			try {
				dOut.writeUTF(command);
				String response = dIn.readUTF();
				System.out.println(response);
				return true;
			}catch(IOException e) {}
		}
	}
	
	/*
	 * Send a string command
	 * */
	public boolean sendMultipleCommands(String[] commands){
		for(String command : commands) {
			sendCommand(command);
		}
		return true;
	}
	
	
	/*
	 * Get a response from the PC
	 * */
	public String readValue() {
		String response = null;
		while (true) {
			try {
				response = dIn.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(response != null) {
				return response;
			}
		}
	}
	
	/*
	 * Return boolean connected status*/
	public boolean checkConnected() {
		return sock.isConnected();
	}
	
	/*
	 * Rotate right and correct
	 * */
	public boolean rotateRightWithCorrect(){
		try {
			dOut.writeUTF("90");
			dIn.readUTF();
			return true;
		}catch(IOException e) {}
		
		return false;
	}
	
	/*
	 * Rotate left and correct
	 * */
	public boolean rotateLeftWithCorrect(){
		try {
			dOut.writeUTF("-90");
			dIn.readUTF();
			return true;
		}catch(IOException e) {}
		
		return false;
	}
	
	/*
	 * Rotate 180 degrees
	 * */
	public boolean rotate180WithCorrect() {
		try {
			dOut.writeUTF("180");
			dIn.readUTF();
			return true;
		}catch(IOException e) {}
		return false;
	}
	
	/*
	 * Travels an integer number of squares
	 * */
	public boolean travel(int cells) {
		try {
			dOut.writeUTF(""+cells);
			dIn.readUTF();
			return true;
		}catch(IOException e) {}
		return false;
	}
	
	public String getColor() {
		try {
			dOut.writeUTF("Color");
			return dIn.readUTF();
		}catch(IOException e) {}
		return null;
	}
	
}
