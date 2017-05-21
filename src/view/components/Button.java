package view.components;
	
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Button extends JComponent {

	//TODO reupload
	private static final long serialVersionUID = 1L;
	
	private ActionListener actionListener;
	private boolean isPressed = false;
	private Thread t;
	private Color fading;
	
	public Button() {
		
		super();
		setPreferredSize(new Dimension(50, 50));
		fading = getBackground();
		
		initMouseListener();
	}

	public void addActionListener(ActionListener al) {
		actionListener = al;
	}

	private void animation(boolean gotClicked) {
		
		if(t != null)
			t.interrupt();
		
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				
				for(int i = 0; i < 60; i++) {
					fading = new Color(fading.getRed(), fading.getGreen(), fading.getBlue(), fading.getAlpha()-255/60);
					try {
						Thread.sleep(1000/60);
					} catch (InterruptedException e) {
						return;
					}
				}
				
				isPressed = false;

				if(gotClicked)
					actionListener.actionPerformed(null);
			}
		});
		t.start();
	}
	
	@SuppressWarnings("unused")
	private void animation2() {
		
		if(t != null)
			t.interrupt();
		
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				
				for(int i = 0; i < 60; i++) {
					
					try {
						Thread.sleep(1000/60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(isPressed) {
			g2.setColor(fading);
			g2.setClip(-getX(), 0, getParent().getWidth(), getWidth());
			g2.fillRect(-getX(), 0, getParent().getWidth(), getWidth());
		
		}

		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public void initMouseListener() {
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					isPressed = true;
					if(t != null)
						t.interrupt();
					fading = getBackground();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				boolean gotClicked = getBounds().contains(new Point(e.getX() + getX(), e.getY() + getY()));
				
				if(SwingUtilities.isLeftMouseButton(e))
					animation(gotClicked);
			}
		});
	}
}
