package view.window;

import java.awt.*;
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
	
	public Panel(int fWidth, int fHeight) {
		
		setPreferredSize(new Dimension(fWidth, fHeight));
		setLayout(null);
		
		this.fWidth = fWidth;
		this.fHeight = fHeight;
		shapes = new ArrayList<>();
		buffer = new BufferedImage(fWidth, fHeight, BufferedImage.TYPE_3BYTE_BGR);
		camera = new Camera();
	}

	public ArrayList<Drawable> getShapes() {return shapes;}
	
	public void setCamera(Camera camera) {this.camera = camera;}

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
		
		for(Component c : getComponents())
			c.paint(buffer.getGraphics());
		
		g2.dispose();
		
		getGraphics().drawImage(buffer, 0, 0, this);
	}
}
