package control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyInput extends KeyAdapter{

	private HashMap<Integer, Boolean> keys;
	
	public KeyInput() {
		keys = new HashMap<>();
	}
	
	public Boolean isPressed(Integer key) {
		if(keys.containsKey(key))
			return keys.get(key);
		return false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
	}
}
