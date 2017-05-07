package control;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

import view.window.Window2;

public class OneMoreMain {

	private Window2 frame;
	
	public OneMoreMain() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int fHeight = (int) screenSize.getHeight();
		int fWidth = (int) (fHeight * 5/9);
		
		frame = new Window2("One More Stroke");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(fWidth, fHeight));
		frame.setLocation((int) (screenSize.getWidth()/2 - fWidth/2) , 0);
		frame.activate();
		
		new Menu2(frame);
	}
	
	public static void main(String[] args) {
		
//		new Game();
		new OneMoreMain();
	}
}