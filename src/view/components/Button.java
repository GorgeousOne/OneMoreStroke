package view.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class Button extends JComponent{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private boolean entered = false;
	
	public Button() {
	
		this.setBounds(50, 50, 100, 100);
		this.setForeground(Color.RED);
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				entered = true;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				entered = false;
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		g2.setColor(getBackground());
		g2.fill(getBounds());
	}
}
