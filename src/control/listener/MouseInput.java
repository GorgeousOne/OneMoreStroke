package control.listener;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class MouseInput extends MouseAdapter implements MouseWheelListener{

	private ActionListener pressListener;
	private ActionListener releaseListener;
	
	public MouseInput() {
		
		pressListener = e -> {};
		releaseListener = e -> {};
	}
	
	public void addPressAction(ActionListener al) {
		pressListener = al;
	}
	
	public void addReleaseAction(ActionListener al) {
		releaseListener = al;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))
			pressListener.actionPerformed(null);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))
			releaseListener.actionPerformed(null);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {}
}
