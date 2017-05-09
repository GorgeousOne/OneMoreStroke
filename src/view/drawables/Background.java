package view.drawables;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Background extends Drawable{

	private int fWidth, fHeight;
	
	public Background(int fWidth, int fHeight) {
		super(createShape(fWidth, fHeight), 10);
		
		this.fWidth = fWidth;
		this.fHeight = fHeight;
	}

	private static Area createShape(int fWidth, int fHeight) {
		Area shape = new Area(new Rectangle2D.Double());
		return shape;
	}
	
	@Override 
	public void fill(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		g2.fillRect(0, 0, fWidth, fHeight);
	}
}
