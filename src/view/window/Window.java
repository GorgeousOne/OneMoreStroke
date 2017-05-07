package view.window;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public Window(String title) {
		super(title);
	}
	
	public void loadIconImage(String path) {
		try {
			setIconImage(ImageIO.read(getClass().getResource(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void activate() {
		pack();
		setVisible(true);
	}
}