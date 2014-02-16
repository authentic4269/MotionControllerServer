package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingOrientation extends Thread {
	
	JLabel label;
	JPanel panel;
	JFrame frame;
	
	public SwingOrientation() {
		
	}
	
	public void run() {
		frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel(new BorderLayout());
		// 3. Create components and put them in the frame.
		// ...create emptyLabel...
		
		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		frame.setSize(xSize,ySize);  
		 
		// 4. Size the frame.

		// 5. Show it.
		ImageIcon im = createImageIcon("resources/target.jpg", "target");
		label = new JLabel(im);
		panel.setSize(xSize, ySize);
		panel.add(label, BorderLayout.LINE_END);
	
		
		
		panel.setSize(xSize, ySize);
		frame.setContentPane(panel);
		
		
		frame.show(); 
		frame.setVisible(true);
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	public void showTarget(int i) {
		panel.remove(label);
		if (i == 1)
			panel.add(label, BorderLayout.LINE_START);
		else if (i == 2)
			panel.add(label, BorderLayout.PAGE_START);
		else if (i == 3)
			panel.add(label, BorderLayout.PAGE_END);
		frame.validate();
	}

	public void finish() {
		frame.dispose();
	}

}
