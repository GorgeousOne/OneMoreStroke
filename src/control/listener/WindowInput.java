package control.listener;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class WindowInput extends WindowAdapter{

	private HashMap<Integer, ActionListener> windowActions;
	
	public WindowInput() {
		windowActions = new HashMap<>();
	}
	
	public void addChangeStateAction(int state, ActionListener al) {
		windowActions.put(state, al);
	}
	
	@Override
	public void windowIconified(WindowEvent e) {
		super.windowIconified(e);
		
		if(windowActions.containsKey(WindowEvent.WINDOW_ICONIFIED))
			windowActions.get(WindowEvent.WINDOW_ICONIFIED).actionPerformed(null);
	}
}
