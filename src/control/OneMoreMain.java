package control;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JFrame;

import view.window.Window;

public class OneMoreMain {

	private Window frame;
	
	public OneMoreMain() {
		
		frame = new Window("One More Stroke");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets taskbar = Toolkit.getDefaultToolkit().getScreenInsets(frame.getContentPane().getGraphicsConfiguration());
		
		int fHeight = (int) screenSize.getHeight() - taskbar.top - taskbar.bottom;
		int fWidth = (int) (fHeight * 5/9);
		
		frame.setPreferredSize(new Dimension(fWidth, fHeight));
		frame.setLocation((int) (screenSize.getWidth()/2 - fWidth/2) , taskbar.top);
		frame.activate();
		
		new Menu(frame);
	}
	
	public static void main(String[] args) {
		new OneMoreMain();
	}
}