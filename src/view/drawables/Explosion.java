package view.drawables;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import view.Vector2D;

public class Explosion extends Drawable {

	private long duration;
	private Particle[] particles;
	private boolean isLaunched = false;
	
	private Point2D center;
	
	public Explosion(Point2D pos, int pieces, double speed, double friction, long duration) {
		super(null, 0);

		this.duration = duration;
		this.particles = new Particle[pieces];
		this.center = pos;
		
		for(int i = 0; i < pieces; i++)
			this.particles[i] = new Particle((Point2D) pos.clone(), 10, speed, friction);
	}

	public void launch(ArrayList<Drawable> solids) {
		
		if(isLaunched)
			return;
		
		isLaunched = true;
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				
				for(int i = 0; i < duration/16; i++) {
					
					for(Particle p : particles)
						p.update(solids);
					
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		t.start();
	}
	
	@Override
	public void setColor(java.awt.Color color) {
		for(Particle p : particles)
			p.setColor(color);
	};
	
	@Override
	public void fill(Graphics g) {
		
		if(!isVisible())
			return;
		
		for(Particle p : particles)
			p.fill(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.draw(new Rectangle2D.Double(center.getX(), center.getY(), 1, 1));
	}
	
	private class Particle extends Drawable {

		private double spin;
		private Vector2D speed;
		private double friction;
		
		private Area intersect;
		
		public Particle(Point2D center, double radius, double speed, double friction) {
			super(createTriangle(radius), 0);
			
			setPos(center);
			setRotation(Math.random() * 2*Math.PI);
			
			speed -= Math.random()*speed/2;
			this.spin = Math.random() * Math.PI/32 - Math.PI/64;
			this.friction = friction;
			this.speed = new Vector2D(Math.cos(getRotation()) * speed, 
									  Math.sin(getRotation()) * speed);
		}
		
		public void update(ArrayList<Drawable> solids) {

			translate(speed.getX(), speed.getY());
			rotate(spin);
			
			for(Drawable d : solids) {
				
				intersect = (Area) getShape();
				intersect.intersect((Area) d.getShape());
				
				if(!intersect.isEmpty()) {
					
					Point2D center = new Point2D.Double(intersect.getBounds2D().getCenterX(),
														intersect.getBounds2D().getCenterY());
					
					double pushAngle = getAngleBetween(getAngleTo(center));
					double radius = getPos().distance(center);
					
					translate(-speed.getX(), -speed.getY());
					rotate(-spin);
					
					speed.negate().multiply(Math.cos(pushAngle));
					spin += Math.sin(pushAngle) / radius;
				}
			}
			
			speed.multiply(1/friction);
			spin /= friction;
		}
		
		private double getAngleTo(Point2D p2) {
			double deltaX = p2.getX() - getPos().getX();
			double deltaY = p2.getY() - getPos().getY();
			double angle = Math.atan(deltaY / deltaX);
			
			if(deltaX < 0)
				angle += Math.PI;
			else if(deltaY < 0)
				angle += 2*Math.PI;
			
			return angle;
		}
		
		private double getAngleBetween(double theta) {
			double angle = theta - speed.getAngle();
			
			if(Math.abs(angle) > Math.PI)
				angle -= Math.signum(angle) * 2*Math.PI;

			return angle;
		}
	}
	
	private static Polygon createTriangle(double radius) {
		
		int[] x = {0, (int) (radius * Math.cos(Math.PI/6)), 
					 (int) (-radius * Math.cos(Math.PI/6))};
		int[] y = {(int) (-radius), (int) (radius * Math.sin(Math.PI/6)),
									(int) (radius * Math.sin(Math.PI/6))};
		return new Polygon(x, y, 3);
	}
}
