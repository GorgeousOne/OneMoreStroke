package view.drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Area;

import view.window.Camera;

public class Wall extends Drawable{
	
	private Camera camera;
	
	public Wall(int layer, Color color, int fWidth, int fHeight, Camera camera) {
		
		super(createShape(fWidth, fHeight), layer);
		setColor(color);
	
		this.camera = camera;
		
	    //setPos(getPos().getX(), camera.getY() - fHeight / 2);
		setPos(fWidth/2, fHeight/2);
	}
	
	private static Area createShape(int fWidth, int fHeight) {
		Area shape = new Area(new Rectangle(-fWidth/2 - fWidth/40, -fHeight/2, fWidth/80, fHeight));
		shape.add	(new Area(new Rectangle(-fWidth/2,			   -fHeight/2, fWidth/40, fHeight)));
		shape.add	(new Area(new Rectangle( fWidth/2 - fWidth/40, -fHeight/2, fWidth/40, fHeight)));
		shape.add	(new Area(new Rectangle( fWidth/2 + fWidth/80, -fHeight/2, fWidth/80, fHeight)));
		return shape;
	}
	
	@Override
	public void fill(Graphics g) {
		
		setPos(getPos().getX(), camera.getY());
		setScale(1, 1/camera.getZoom());
		
		super.fill(g);
	}
}
