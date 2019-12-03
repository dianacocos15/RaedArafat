import java.io.File;
import java.util.List;

import lejos.hardware.*;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.*;
//A cell is 25.5cm long.


public class DoomTheme extends Thread{
	int[][] Notes = {
			{ 33, 35, 37, 39, 41, 44, 46, 49, 52, 55, 58, 62 },
			{ 65, 69, 73, 78, 82, 87, 92, 98, 104, 110, 117, 123 },
			{ 131, 139, 147, 156, 165, 175, 185, 196, 208, 220, 233, 247 },
			{ 262, 277, 294, 311, 330, 349, 370, 392, 415, 440, 466, 494 },
			{ 523, 554, 587, 622, 659, 698, 740, 784, 831, 880, 932, 988 },
			{ 1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, 1661, 1760, 1865, 1976 },
			{ 2093, 2217, 2349, 2489, 2637, 2794, 2960, 3136, 3322, 3520, 3729, 3951 },
			{ 4186, 4435, 4699, 4978, 5274, 5588, 5920, 6272, 6645, 7040, 7459, 7902 }
		};
	

	int NOTE_C  = 0;
	int NOTE_CS =1;
	int NOTE_D  = 2;
	int NOTE_DS = 3;
	int NOTE_E  = 4;
	int NOTE_F= 5;
	int NOTE_FS= 6;
	int NOTE_G =7;
	int NOTE_GS =8;
	int NOTE_A =9;
	int NOTE_AS =10;
	int NOTE_B =11;
	
	
	void noteDoomBase(int octave, int speed) {
		playNote(octave - 1, NOTE_E, speed / 2);
		delay(speed / 2);
		playNote(octave - 1, NOTE_E, speed);
	}
	
	public void run() {
		System.out.println("Press a button.");
		Button.LEDPattern(8);
		Button.waitForAnyEvent();
		while(true) {
			loop();
		}
	}

	
	private void delay(int i) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void playNote(int octave, int note, int duration)
	{
		Sound.playTone(Notes[octave][note], duration, 20);
	}
	
	void loop()
	{

		fastPart();
		slowPart();		
	}
	
	void fastPart() {
		// Fast part
		int speed = 75;
		int octave = 3;
		playNote(octave, NOTE_B, speed);
		playNote(octave, NOTE_G, speed);
		playNote(octave, NOTE_E, speed);
		playNote(octave, NOTE_C, speed);

		playNote(octave, NOTE_E, speed);
		playNote(octave, NOTE_G, speed);
		playNote(octave, NOTE_B, speed);
		playNote(octave, NOTE_G, speed);

		playNote(octave, NOTE_B, speed);
		playNote(octave, NOTE_G, speed);
		playNote(octave, NOTE_E, speed);
		playNote(octave, NOTE_G, speed);

		playNote(octave, NOTE_B, speed);
		playNote(octave, NOTE_G, speed);
		playNote(octave, NOTE_B, speed);
		playNote(octave + 1, NOTE_E, speed);
	}
	
	void slowPart() {
		// Main theme
		int speed = 150;
		int octave = 3;
		
		noteDoomBase(octave, speed);
		playNote(octave, NOTE_E, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_D, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_C, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_AS, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_B, speed);
		playNote(octave, NOTE_C, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_E, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_D, speed);

		noteDoomBase(octave, speed);
		playNote(octave, NOTE_C, speed);

		noteDoomBase(octave, speed);
		playNote(octave- 1, NOTE_AS, speed * 2);
	}
}
