package view.drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;

import view.window.Camera;

public class Wall extends Drawable{
	
	private Color nextColor;
	private Camera camera;
	
	public Wall(int layer, int fWidth, int fHeight, Camera camera) {
		
		super(createShape(fWidth, fHeight), layer);
		setPos(fWidth/2, fHeight/2);
		setColor(RED);

		this.camera = camera;
		nextColor = RED;
	}
	
	public void setNextColor(Color c) {
		nextColor = c;
	}
	
	private static Area createShape(int fWidth, int fHeight) {
		Area shape = new Area(new Rectangle(-fWidth/2 - fWidth/32, -fHeight/2, fWidth/64, fHeight));
		shape.add	(new Area(new Rectangle(-fWidth/2,			   -fHeight/2, fWidth/32, fHeight)));
		shape.add	(new Area(new Rectangle( fWidth/2 - fWidth/32, -fHeight/2, fWidth/32, fHeight)));
		shape.add	(new Area(new Rectangle( fWidth/2 + fWidth/64, -fHeight/2, fWidth/64, fHeight)));
		return shape;
	}
	
	@Override
	public void fill(Graphics g) {
		
		setPos(getPos().getX(), camera.getY());
		setScale(1, 1/camera.getZoom());
		
		if(!getColor().equals(nextColor))
			courseColorTo(nextColor, 10);
			
		super.fill(g);
	}
}
