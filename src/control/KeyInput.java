package control;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyInput extends KeyAdapter{

	private HashMap<Integer, ActionListener> keyPressActions;
	private HashMap<Integer, ActionListener> keyReleaseActions;

	public KeyInput() {
		
		keyPressActions = new HashMap<>();
		keyReleaseActions = new HashMap<>();
	}
	
	public void addKeyPressAction(int key, ActionListener al) {
		keyPressActions.put(key, al);
	}
	
	public void addKeyReleasedAction(int key, ActionListener al) {
		keyReleaseActions.put(key, al);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		
		if(keyPressActions.containsKey(e.getKeyCode()))
			keyPressActions.get(e.getKeyCode()).actionPerformed(null);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		
		if(keyReleaseActions.containsKey(e.getKeyCode()))
			keyReleaseActions.get(e.getKeyCode()).actionPerformed(null);
	}
}
