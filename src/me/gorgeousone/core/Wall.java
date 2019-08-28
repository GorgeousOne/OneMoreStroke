package me.gorgeousone.core;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import me.gorgeousone.collision.Collidable;
import me.gorgeousone.util.NeonColor;

public class Wall extends Collidable {
	
//	private Color nextColor;
	private Camera camera;
	
	public Wall(int canvasWidth, int canvasHeight, Camera camera) {
		
		super(createShape(canvasWidth, canvasHeight));
		setColor(NeonColor.PINK);

		this.camera = camera;
//		nextColor = NeonColor.RED;
	}
	
//	public void setNextColor(Color c) {
//		nextColor = c;
//	}
	
	@Override
	public void paint(Graphics2D graphics) {
		
		setPos(getPos().getX(), camera.getY());
		setScale(1, 1/camera.getZoom());
		
//		if(!getColor().equals(nextColor))
//			courseColorTo(nextColor, 10);
			
		super.paint(graphics);
	}

	@Override
	public boolean canCollide() {
		return true;
	}

	@Override
	public boolean hasPhysics() {
		return false;
	}

	private static Area createShape(int canvasWidth, int canvasHeight) {
		
		Area shape = new Area(new Rectangle(-canvasWidth/2 - canvasWidth/32, -canvasHeight/2, canvasWidth/64, canvasHeight));
		shape.add	(new Area(new Rectangle(-canvasWidth/2,			       -canvasHeight/2, canvasWidth/32, canvasHeight)));
		shape.add	(new Area(new Rectangle( canvasWidth/2 - canvasWidth/32, -canvasHeight/2, canvasWidth/32, canvasHeight)));
		shape.add	(new Area(new Rectangle( canvasWidth/2 + canvasWidth/64, -canvasHeight/2, canvasWidth/64, canvasHeight)));
		return shape;
	}
}
