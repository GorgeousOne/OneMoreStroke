package classPackage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import graphicsPackage.Drawable;

public class Panel extends JPanel{

	private static final long serialVersionUID = 1L;

	private ArrayList <Drawable> shapes;
	private int frameWidth;
	private int frameHeight;
	private BufferedImage buffer;
	private Point2D camera;
	
	public Panel(int fWidth, int fHeight) {
		
		setPreferredSize(new Dimension(fWidth, fHeight));
		
		shapes = new ArrayList<>();
		frameWidth = fWidth;
		frameHeight = fHeight;
		buffer = new BufferedImage(fWidth, fHeight, BufferedImage.TYPE_3BYTE_BGR);
		camera = new Point2D.Double();
	}

	public ArrayList<Drawable> getShapes() {return shapes;}
	
	public void setCamera(Point2D camera) {this.camera = camera;}

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
	
	public void update() {
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();

		double dx = frameWidth/2 - camera.getX();
		double dy = frameHeight/2 - camera.getY();
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, frameWidth, frameHeight);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(Drawable d : shapes) {
			try {
				d.fill(g2, dx, dy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		paintComponent(getGraphics());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}
}
