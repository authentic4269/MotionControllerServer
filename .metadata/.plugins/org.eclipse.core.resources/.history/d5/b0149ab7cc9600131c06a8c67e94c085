package main;

import gui.SwingOrientation;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import java.util.LinkedList;

import org.json.*;

import server.PhoneSocketServer;
import java.awt.*;
//This class contains the main method which reads in data from the 
public class PhoneRemote {
	
	double orientation[];
	double lastAcceleration[];
	long lastTime;
	double canonicalOrientations[][] = new double[4][3];
	double smooth[] = {-36, 9, 44, 69, 84, 89, 84, 69, 44, 9, -36};
	double smooth2[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
	double smoothDenom;
	double smooth2Denom;
	LinkedList<LinkedList<Double>> lastOrientations = new LinkedList<LinkedList<Double>>();
	LinkedList<StampedList> lastAccels = new LinkedList<StampedList>();
	int calibrate = 0;
	SwingOrientation gui;
	private Mouse mouse = null;
	
	//acceleration related fields
	final double delay = .02;
	
	private class StampedList {
		public LinkedList<Double> values;
		public long timeStamp;
		
		public StampedList()
		{
			values = new LinkedList<Double>();
		}
	}
	
	double[] prevVelocity = {0.0, 0.0, 0.0};  //will contain the most recent velocity vector 
	double[] prevPosition; //contains the most recent coordinates of the phone relative to the bottom left corner of the computer screen 
	private long prevTime = System.currentTimeMillis();
	
	
	public static void main(String[] args) throws AWTException  {
		PhoneRemote controller = new PhoneRemote();
		PhoneSocketServer connection = new PhoneSocketServer(controller);
	}
	

	public PhoneRemote() throws AWTException{
		gui = new SwingOrientation();
		gui.run();	
		initializeOrientationFields();
		initializeAccelerationFields();
	}
	
	private void initializeAccelerationFields() {

		int i, j;
		prevVelocity = new double[3];
		prevPosition = new double[3];
		lastAcceleration = new double[3];
		for (i = 0; i < 3; i++)
		{
			lastAccels.add(new StampedList());
			for (j = 0; j < 10; j++)
			{
				lastAccels.get(i).values.add(0.0);
			}
		}
		for (i = 0; i < 10; i++)
		{
			smooth2Denom += smooth2[i];
		}
	}


	private void initializeOrientationFields(){
		lastOrientations.add(new LinkedList<Double>());
		lastOrientations.add(new LinkedList<Double>());
		lastOrientations.add(new LinkedList<Double>());
		prevTime = 0;
		orientation = new double[3];
		int i, j;
		for (i = 0; i < 3; i++)
		{
			for (j = 0; j < 11; j++)
			{
				lastOrientations.get(i).add(0.0);
			}
		}
		smoothDenom = 0.0;
		for (i = 0; i < 11; i++)
		{
			smoothDenom += smooth[i];
		}

	}

	public void updateOrientation(double[] vals){
		orientation = vals;
		int i, j;
		double smoothNumer;
		LinkedList<Double> cur;
		for (i = 0; i < 3; i++)
		{
			cur = lastOrientations.get(i);
			cur.removeFirst();
			cur.add(vals[i]);
			smoothNumer = 0.0;
			for (j = 0; j < 11; j++)
			{
				smoothNumer += smooth[j] * vals[i];
			}
			orientation[i] = smoothNumer / smoothDenom;
		}
		if (mouse != null)
		{
			mouse.updateOrientation(orientation);
		}
	}

	public void addAcceleration(double[] vals, long time) {
		StampedList cur;
		int i, j;
		double smoothNumer;
		double curAcceleration[] = new double[3];
		for (i = 0; i < 3; i++)
		{
			cur = lastAccels.get(i);
			cur.values.removeFirst();
			cur.values.add(vals[i]);
			cur.timeStamp = time;
			smoothNumer = 0.0;
			for (j = 0; j < 10; j++)
			{
				smoothNumer += smooth2[j] * vals[i];
			}
			smoothNumer /= smooth2Denom;
			curAcceleration[i] = smoothNumer;
			prevTime  = time;
			
		}
		lastAcceleration = curAcceleration;
	}
	
	private double[] updatePosition(double[] prevVelocity, double[] currVelocity, double prevTime, double curTime){
		double[] newPosition = new double[3];
		for(int i= 0; i<3; i++){
			newPosition[i] = prevPosition[i] + trapezoidArea(prevVelocity[i], currVelocity[i], prevTime, curTime);
		}
		return newPosition;
	}
	
	private double[] updateVelocity(double[] prevAccel, double[] currAccel, double prevTime, double curTime){
		double[] newVelocity= new double[3];
		for(int i= 0; i<3; i++){
			newVelocity[i] = trapezoidArea(prevAccel[i], currAccel[i], prevTime, curTime);
		}
		
		return newVelocity;
	}
	
	private double trapezoidArea(double start, double end, double startTime, double endTime){
		return (start + ((end - start)/2))*(endTime - startTime)/1000;
	}

	public void calibrate() {
		int i;
		for (i = 0; i < 3; i++)
			canonicalOrientations[calibrate][i] = orientation[i];
		if (calibrate == 3)
		{
			gui.finish();
			mouse = new Mouse(canonicalOrientations);
		}
		else 
		{
			gui.showTarget(++calibrate);
		}
	}
	


	public void leftclick() {
		mouse.leftclick();
	}
	
	public void rightclick() {
		mouse.rightclick();
	}
}
