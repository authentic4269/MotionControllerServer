package main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.util.LinkedList;

public class Mouse {

	double[][] co;
	double dif0;
	double dif1;
	Robot r;
	double height;
	double width;
	LinkedList<Double> smoothx;
	LinkedList<Double> smoothy;
	final double screenWidth = .3589; //width of this machine's screen in meters
	final double screenHeight = .2471; //height of this machine's screen in meters 
	double x;
	double y;
	
	public Mouse(double[][] canonicalOrientations) {
		co = canonicalOrientations;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		try {
			r = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateOrientation(double[] orientation) {
		x = width * (orientation[0] - co[1][0]) / (co[0][0] - co[1][0]);
		y = height * (orientation[1] - co[2][1]) / (co[3][1] - co[2][1]);
		r.mouseMove((int) x, (int) y);
	}
	
	public void updateCoordinates(double[] currentPosition){
		
		int newXCoord = (int) Math.floor((currentPosition[0]/screenWidth)*width);
		int newYCoord = (int) (height - Math.floor((currentPosition[1]/screenHeight)*height));
		System.out.println("x:" + newXCoord + ", y:" + newYCoord);
		//r.mouseMove(newXCoord, newYCoord);
	}

	public void leftclick() {
		r.mousePress(InputEvent.BUTTON1_MASK);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public void rightclick() {
			r.mousePress(InputEvent.BUTTON3_MASK);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			r.mouseRelease(InputEvent.BUTTON3_MASK);
	}

}
