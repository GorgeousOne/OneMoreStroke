package control;

import java.awt.Dimension;
import java.awt.Toolkit;

import view.window.Window;

public class OneMoreMain {

	private Window frame;
	
	public OneMoreMain() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int fHeight = (int) screenSize.getHeight();
		int fWidth = (int) (fHeight * 5/8);
		
		frame = new Window(fWidth, fHeight);
		frame.setLocation((int) (screenSize.getWidth()/2 - fWidth/2) , 0);
		new Menu(frame);
	}
	
	public static void main(String[] args) {
		new Game();
		//new OneMoreMain();
	}
}