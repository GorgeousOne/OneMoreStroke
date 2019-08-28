package me.gorgeousone.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import me.gorgeousone.drawing.Drawable;

public class Canvas extends JPanel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Drawable> shapes;
	
	private BufferedImage buffer;
	private Camera camera;
	
	public Canvas() {
		super();
		
		setLayout(null);
		
		shapes = new ArrayList<>();
		camera = new Camera();
		
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			}
		});
	}
	
	public BufferedImage getScreenShot() {
		return new BufferedImage(buffer.getColorModel(), buffer.getRaster(), buffer.isAlphaPremultiplied(), null);
	}
	
	public Camera getCamera() {
		return camera;
	}

	public ArrayList<Drawable> getDrawables() {
		return shapes;
	}
	
	public void addDrawable(Drawable d) {
		shapes.add(d);
	}
		
	public void removeDrawable(Drawable d) {
		shapes.remove(d);
	}
	
	public void clear() {
		shapes.clear();
		this.removeAll();
		camera = new Camera();
	}
	
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		camera.applyFocus(this, g2);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Iterator<Drawable> iter = shapes.iterator();
		
		while(iter.hasNext())
			iter.next().paint(g2);
		
		g.drawImage(buffer, 0, 0, this);
		g2.dispose();
	}
}