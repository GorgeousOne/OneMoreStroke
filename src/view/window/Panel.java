package view.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

import view.drawables.Drawable;

public class Panel extends JPanel{

	private static final long serialVersionUID = 1L;

	private ArrayList <Drawable> shapes;
	private int fWidth, fHeight;
	private BufferedImage buffer;
	private Camera camera;
	
	public Panel(int fWidth, int fHeight) {
		
		setSize(new Dimension(fWidth, fHeight));
		setLayout(null);
		
		this.fWidth = fWidth;
		this.fHeight = fHeight;
		shapes = new ArrayList<>();
		buffer = new BufferedImage(fWidth, fHeight, BufferedImage.TYPE_3BYTE_BGR);
		camera = new Camera();
	}

	public ArrayList<Drawable> getDrawables() {return shapes;}
	
	public void addCamera(Camera camera) {this.camera = camera;}

	public void addDrawable(Drawable d) {
		
		for(int i = 0; i < shapes.size(); i++) {
			if(d.getLayer() >= shapes.get(i).getLayer()) {
				shapes.add(i, d);
				return;
			}
		}
		shapes.add(d);
	}
		
	public void removeDrawable(Drawable d) {shapes.remove(d);}
	
	public void clear() {
		shapes.clear();
		this.removeAll();
		camera = new Camera();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();

		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, fWidth, fHeight);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.translate(fWidth/2, fHeight/2);
		g2.scale(camera.getZoom(), camera.getZoom());
		g2.translate(-fWidth/2, -fHeight/2);
		
		g2.translate(fWidth/2 - camera.getX(),fHeight/2 -camera.getY());

		Iterator<Drawable> iter = shapes.iterator();
		
		while(iter.hasNext()) {

			try {
				iter.next().fill(g2);
			} catch (Exception e) {
			}
		}
		
//		for(Drawable d : shapes) {
//			try {
//				d.fill(g2);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		g.drawImage(buffer, 0, 0, this);
		g2.dispose();
	}
}
