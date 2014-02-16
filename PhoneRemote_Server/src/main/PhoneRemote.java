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

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import server.PhoneSocketServer;
import java.awt.*;
//This class contains the main method which reads in data from the 
public class PhoneRemote {
	
	public static void main(String[] args) {
		PhoneRemote controller;

			controller = new PhoneRemote();
			PhoneSocketServer connection = new PhoneSocketServer(controller);

	}
	
	public static double orientation[];
	double lastAcceleration[];
	double canonicalOrientations[][] = new double[4][3];
	double smooth[] = {-36, 9, 44, 69, 84, 89, 84, 69, 44, 9, -36};
	double smooth2[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
	double smoothDenom;
	double smooth2Denom;
	LinkedList<LinkedList<Double>> lastOrientations = new LinkedList<LinkedList<Double>>();
	LinkedList<LinkedList<Double>> lastAccels = new LinkedList<LinkedList<Double>>();
	int calibrate = 0;
	SwingOrientation gui;
	private Mouse mouse = null;

	
	
	public PhoneRemote() {
		gui = new SwingOrientation();
		gui.run();	
		initializeOrientationFields();
		initializeAccelerationFields();
	}
	
	private void initializeAccelerationFields() {
		int i, j;
		lastAcceleration = new double[3];
		for (i = 0; i < 3; i++)
		{
			lastAccels.add(new LinkedList<Double>());
			for (j = 0; j < 10; j++)
			{
				lastAccels.get(i).add(0.0);
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
		LinkedList<Double> cur;
		int i, j;
		double smoothNumer;
		double curAcceleration[] = new double[3];
		for (i = 0; i < 3; i++){
			cur = lastAccels.get(i);
			cur.removeFirst();
			cur.add(vals[i]);
			smoothNumer = 0.0;
			for (j = 0; j < 10; j++)
			{
				smoothNumer += smooth2[j] * vals[i];
			}
			smoothNumer /= smooth2Denom;
			curAcceleration[i] = smoothNumer;
		}
		lastAcceleration = curAcceleration;
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

	public void scrollMouse(int data, int down) {
		mouse.scrollMouse(data, down);
		
	}

	public void zoom(int data) {
		mouse.zoom(data);
	}
}
