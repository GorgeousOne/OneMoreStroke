package view.window;

import java.awt.Dimension;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public Window(int fWidth, int fHeight) {
		super("One More Stroke");
	    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(fWidth, fHeight));
		pack();

		System.out.println(getWidth() + ", " + getHeight());
		
		try {
			setIconImage(ImageIO.read(getClass().getResource("/view/images/Icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setVisible(true);

	}
}