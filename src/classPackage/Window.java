package classPackage;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.JFrame;

public class Window extends JFrame implements KeyListener, WindowListener{

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, Boolean> keys;
	
	public Window(int fWidth, int fHeight) {
		super("One More Stroke");
	    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setPreferredSize(new Dimension(fWidth + 6, fHeight + 28));
		pack();

		keys = new HashMap<>();
		addKeyListener(this);
		addWindowListener(this);
	}

	public Boolean isKeyPressed(Integer key) {
		if(keys.containsKey(key))
			return keys.get(key);
		return false;
	}
	
	//WindowListener
	@Override
	public void windowActivated(WindowEvent arg0) {	}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		//save game
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		//go on
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		//stop game
	}

	@Override
	public void windowOpened(WindowEvent arg0) {}


	//KeyListener	
	@Override
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
