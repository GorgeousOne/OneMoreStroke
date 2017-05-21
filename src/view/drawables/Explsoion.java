package view.drawables;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import view.Vector2D;

public class Explsoion extends Drawable{

	private boolean isLaunched = false;
	
	public Explsoion(Point2D center, double range, int tiles, long duration) {
		super(null, 1);
	}

	public void launch() {
		
		if(isLaunched)
			return;
		
		isLaunched = true;
	}
	
	@SuppressWarnings("unused")
	private static class Fragment extends Drawable{
		
		private double spin;
		private Vector2D speed;
		
		public Fragment(Point2D center, double radius, double speed) {
			super(createShape(center, radius), 0);
			
			this.spin = Math.random() * Math.PI/8 - Math.PI/16;
			this.speed = new Vector2D(Math.random() * 2*Math.PI, speed);
		}
		
		private static Polygon createShape(Point2D center, double radius) {
			
			int[] x = {(int) center.getX(), (int) (center.getX() + radius * Math.cos(Math.PI/6)), 
											(int) (center.getX() - radius * Math.cos(Math.PI/6))};
			int[] y = {(int) (center.getY() - radius), (int) (center.getY() + radius * Math.sin(Math.PI/6)),
													   (int) (center.getY() + radius * Math.sin(Math.PI/6))};
			return new Polygon(x, y, 3);
		}
		
		public void update() {
			rotate(spin);
			spin *= 0.9;
			
			translate(speed.x, speed.y);
			speed = speed.multiply(0.5);
		}
	}
}
