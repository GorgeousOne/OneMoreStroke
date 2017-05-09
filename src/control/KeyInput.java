package control;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyInput extends KeyAdapter{

	private HashMap<Integer, Boolean> keys;
	private ActionListener keyPressedAction;
	private ActionListener keyReleasedAction;

	public KeyInput() {
		keys = new HashMap<>();
		
		keyPressedAction = e -> {};
		keyReleasedAction = e -> {};
	}
	
	public void addKeyPressedAction(ActionListener al) {
		keyPressedAction = al;
	}
	
	public void addKeyReleasedAction(ActionListener al) {
		keyReleasedAction = al;
	}
	
	public Boolean isPressed(Integer key) {
		if(keys.containsKey(key))
			return keys.get(key);
		return false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
		keyPressedAction.actionPerformed(null);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
		keyReleasedAction.actionPerformed(null);
	}
}
