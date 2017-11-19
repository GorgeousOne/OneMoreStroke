package view.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

import view.drawables.Drawable;

public class Panel extends JPanel{

	private static final long serialVersionUID = 1L;

	private ArrayList <Drawable> shapes;

 	private int fWidth, fHeight;
	private BufferedImage buffer;
	private Camera camera;
	
	public Panel() {
		super();
		
		fWidth = fHeight = 1;
		buffer = new BufferedImage(fWidth, fHeight, BufferedImage.TYPE_3BYTE_BGR);
		shapes = new ArrayList<>();
		camera = new Camera();
	}
	
	public BufferedImage getScreenShot() {
		return new BufferedImage(buffer.getColorModel(), buffer.getRaster(), buffer.isAlphaPremultiplied(), null);
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public ArrayList<Drawable> getDrawables() {
		return shapes;
	}
	
	public void addDrawable(Drawable d) {
		for(int i = 0; i < shapes.size(); i++) {
			if(d.getLayer() >= shapes.get(i).getLayer()) {
				shapes.add(i, d);
				return;
			}
		}
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
	public void setSize(Dimension size) {
		super.setSize(size);
		fWidth = (int) size.getWidth();
		fHeight = (int) size.getHeight();
		
		buffer = new BufferedImage(fWidth, fHeight, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, fWidth, fHeight);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.translate(fWidth/2, fHeight/2);
		g2.scale(camera.getZoom(), camera.getZoom());
		g2.translate(-fWidth/2, -fHeight/2);
		
		g2.translate(fWidth/2 - camera.getX(),fHeight/2 -camera.getY());

		for(Drawable d : shapes) {
			try {
				d.fill(g2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		g.drawImage(buffer, 0, 0, this);
		g2.dispose();
	}
}