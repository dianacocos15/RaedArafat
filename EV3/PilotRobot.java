import java.util.List;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

/**
 * @author Suhail Munshi
 *
 */
public class PilotRobot {
	/*Private sensors*/
	private EV3UltrasonicSensor usSensor;
	private EV3GyroSensor gSensor;
	private EV3ColorSensor leftColor, rightColor;
	private ColorAllocator color_identify = new ColorAllocator();
	
	/*Sample providers and sample declaration*/
	private SampleProvider distSP, gyroSP, leftSP, rightSP;
	private float[] distSample, angleSample,  leftSample, rightSample;

	static char[] direction = {'N', 'E', 'S', 'W'};
	public static int listIndex = 0;
	
	/*
	 * Constants for use in any class
	 * */
	static final int ACCELERATION = 20;
	static final int DECELERATION = 100; 
	static final int DISTANCE = 100;
	static final int TOP_SPEED = 30;
	static final int ANGULAR_ACCELERATION = 40;
	static final double DISTANCE_FROM_THE_WALL = 0.07;
	static final int ROTATE_HEAD_LEFT = 90;
	static final int ROTATE_HEAD_RIGHT = -90;
	static final int ROTATE_HEAD_CENTER = 0;
	static final int ROTATE_ROBOT_RIGHT = 30;
	static final int ROTATE_ROBOT_LEFT = -30;
	static final int SAMPLE_SIZE = 80;
	
	private MovePilot pilot;	
	public static boolean rotated = false;
	
	/*
	 * PilotRobot Constructor
	 * */
	public PilotRobot() {
		Brick myEV3 = BrickFinder.getDefault();
		
		/*
		 * Initialise sensors
		 * */
		usSensor = new EV3UltrasonicSensor(myEV3.getPort("S3"));
		gSensor = new EV3GyroSensor(myEV3.getPort("S2"));
		leftColor = new EV3ColorSensor(myEV3.getPort("S1"));
		rightColor = new EV3ColorSensor(myEV3.getPort("S4"));
		
		/*
		 * Create sample providers
		 * */
		leftSP = leftColor.getRGBMode();
		rightSP = rightColor.getRGBMode();
		distSP = usSensor.getDistanceMode();
		gyroSP = gSensor.getAngleMode();
		
		/*Create samples of size 1*/
		leftSample = new float[leftSP.sampleSize()];
		rightSample = new float[rightSP.sampleSize()];
		distSample = new float[distSP.sampleSize()];
		angleSample = new float[gyroSP.sampleSize()];

		/*
		 * Robot Chassis construction
		 * */
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 4.253133).offset(-5.58227848);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 4.253133).offset(5.58227848);
		Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
	    pilot = new MovePilot(myChassis);
	    
	    /*Set default movement values*/
	    pilot.setAngularAcceleration(ANGULAR_ACCELERATION);
	    pilot.setLinearSpeed(TOP_SPEED);
	    
		// Reset the value of the gyroscope to zero
		gSensor.reset();
		
	}
	
	/*Closes robot*/
	public void closeRobot() {
		leftColor.close();
		rightColor.close();
		usSensor.close();
		gSensor.close();
	}
	
	/*Fetches colour int from left colour sensor*/
	public Color getLeftColourSensor() {
    	leftSP.fetchSample(leftSample, 0);
    	Color left = color_identify.findClosest(leftSample);
    	return left;
	}
	
	public float[] getRGB() {
		leftSP.fetchSample(leftSample, 0);
		rightSP.fetchSample(rightSample, 0);
		float[] avg = {0.0f,0.0f,0.0f};
		for(int i=0;i<3;i++) {
			avg[i] = (leftSample[i] + rightSample[i])/2;
		}
		return avg;
	}
	
	/*Fetches colour int from right color sensor*/
	public Color getRightColourSensor() {
		rightSP.fetchSample(rightSample, 0);
    	Color right = color_identify.findClosest(rightSample);
    	return right;
	}
	
	public float[] sampleColors(){
		
		float values[][] = new float[1000][3];
		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;
		
		
		for(int i = 0; i <1000;i++) {
			values[i] = getRGB();
		}
	
		
		for (float v[] : values) {
			red += v[0];
			green += v[1];
			blue += v[2];
		}
		
		red = red/1000;
		blue = blue/1000;
		green = green/1000;
		
		float rgb[] = {0.0f,0.0f,0.0f};
		rgb[0] = red;
		rgb[1] = green;
		rgb[2] = blue;
		
		return rgb;
		
	}
	
	/*Sample colours and return a value*/
	public Color getColor() {
		float rgb[] = sampleColors();
		Color sample = color_identify.findClosest(rgb);
		return sample;
	}
	
	/*Fetches distance from ultrasound*/
	public float getDistance() {
    	distSP.fetchSample(distSample, 0);
    	return distSample[0];
	}

	/*Returns angle using gyroscope*/
	public float getAngle() {
    	gyroSP.fetchSample(angleSample, 0);
    	return angleSample[0];
	}
	
	/*Returns pilot for controlling movement*/
	public MovePilot getPilot() {
		return pilot;
	}
	
	/*
	 * Method for detecting obstacles straight ahead
	 * Return obstacle boolean
	 * */
	public boolean obstacleAhead() {
		if(getDistanceWithSampling() <= 0.06) {
			return true;
		}
		return false;
	}
	
	/*
	 * Checks if obstacle appears while moving, returns bool
	 * */
	public boolean checkObstacleAheadWhileMoving() {
		boolean obstacle = false;
		while(pilot.isMoving()) {
			if(obstacleAhead()) {
				return true;
			}
		}
		return obstacle;
	}
	
	/*
	 * Rotates head of ultra sound sensor to specified position
	 * */
	public void rotateHead(int position) {
		Motor.C.rotateTo(position);
	}
	
	/*
	 * Get the distance the wheels have travelled 
	 * Using Pi x diameter x turns
	 * */
	public static double getTravelDistance() {
		float numberOfRevolutionsB = Motor.B.getTachoCount();
		float numberOfRevolutionsD = Motor.D.getTachoCount();
		
		float avgRevolutions = (numberOfRevolutionsB + numberOfRevolutionsD)/720;
		
		double distance = (double)(Math.PI * 4.2531333 * avgRevolutions);
		
		return distance;
	}
	
	/*
	 * Reset wheel turn count to 0
	 * */
	public static void resetTachoCount() {
		Motor.B.resetTachoCount();
		Motor.D.resetTachoCount();	
	}
	
	/*
	 * Average number of times the wheels have turned
	 * */
	public static float returnRevolutions() {
		float numberOfRevolutionsB = Motor.B.getTachoCount();
		float numberOfRevolutionsD = Motor.D.getTachoCount();
		float avgRevolutions = (numberOfRevolutionsB + numberOfRevolutionsD)/720;
		
		return avgRevolutions;
	}
	
	/*
	 * Get Distance in meters using appropriate sampling of average
	 * @return float average_distance
	 * */
	public float getDistanceWithSampling() {
		float currentDistance = getDistance();
		float[] distances = new float[SAMPLE_SIZE];
		
		float sum = 0;
		int ptr = 0;
		int count = 0;
		
		while (ptr < distances.length) {
			if(currentDistance > 0 && currentDistance != Float.POSITIVE_INFINITY) {
				distances[ptr] = getDistance();
				ptr++;
			}
			count ++;
			if (count > SAMPLE_SIZE*3) {
				return 100;
			}
		}
		
		for (float d : distances) {
			sum += d;
		}
		
		//return average distance
		return sum/distances.length;
	}
	
	/*
	 * Reset gyroscope
	 * */
	public void resetGyro() {
		// TODO Auto-generated method stub
		gSensor.reset();
	}	
}

