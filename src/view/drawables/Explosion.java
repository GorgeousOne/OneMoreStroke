package view.drawables;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import view.Vector2D;

public class Explosion extends Drawable {

	private long duration;
	private ArrayList<Particle> particles;
	private boolean isLaunched;
	private int pCount;
	private float pSpeed, pFriction;
	
	public Explosion(Area shape, int layer) {
		super(shape, layer);
		
		isLaunched = false;
		particles = new ArrayList<>();
		pCount = 10;
		pSpeed = 1f;
		pFriction = 1f;
	}

	public boolean isLaunched() {
		return isLaunched;
	}
	
	public void launch(ArrayList<Drawable> solids) {
		
		if(isLaunched)
			return;
		
		isLaunched = true;
		
		for(int i = 0; i < pCount; i++) {
			Particle p = new Particle(createShape(), solids);
			p.setScale(getScale());
			p.setPos((Point2D) getPos().clone());
			p.setColor(getColor());
			particles.add(p);
		}
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				
				for(int i = 0; i < duration/16; i++) {
					
					for(Particle p : particles)
						p.update(solids);
					
					try {
						Thread.sleep(16);	//TODO add variable
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		t.start();
	}
	
	public void setCount(int count) {
		pCount = (int) Math.max(1, count);
	}
	
	public void setSpeed(float speed) {
		pSpeed = speed;
	}
	
	public void setFriction(float friction) {
		pFriction = friction;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@Override
	public void fill(Graphics g) {
		
		if(!isVisible())
			return;
		
		for(Particle p : particles)
			p.fill(g);
	}
	
	private class Particle extends Drawable {

		private double spin;
		private Vector2D velocity;
		private double friction;
		
		public Particle(Area shape, ArrayList<Drawable> solids) {
			super(shape, 0);
			
			setRotation(Math.random() * 2*Math.PI);
			double speed = Explosion.this.pSpeed;
			speed += Math.random()*speed - speed/2;
			
			this.spin = Math.random() * speed/4/Math.PI - speed/8/Math.PI;
			this.friction = Explosion.this.pFriction;
			this.velocity = new Vector2D(Math.cos(getRotation()) * speed, 
									  Math.sin(getRotation()) * speed);
		}
		
		public void update(ArrayList<Drawable> solids) {

			translate(velocity.getX(), velocity.getY());
			rotate(spin * velocity.getLength());
			
			for(Drawable d : solids) {
				
				Area intersect = (Area) getShape();
				intersect.intersect((Area) d.getShape());
				
				if(!intersect.isEmpty()) {
					
					translate(-velocity.getX(), -velocity.getY());
					rotate(-spin * velocity.getLength());
					
					Point2D center = new Point2D.Double(intersect.getBounds2D().getCenterX(),
														intersect.getBounds2D().getCenterY());
					
					double impactAngle = getAngleBetween(getAngleTo(getPos(), center));
					double radius = getPos().distance(center);
					
					velocity.multiply(Math.cos(impactAngle));
					velocity.negate().rotate(2*impactAngle);

					spin += Math.sin(impactAngle) / radius;
				}
			}
			
			velocity.multiply(1.0/friction);
		}
		
		private double getAngleTo(Point2D p, Point2D p2) {
			double deltaX = p2.getX() - p.getX();
			double deltaY = p2.getY() - p.getY();
			double angle = Math.atan(deltaY / deltaX);
			
			if(deltaX < 0)
				angle += Math.PI;
			else if(deltaY < 0)
				angle += 2*Math.PI;
			
			return angle;
		}
		
		private double getAngleBetween(double theta) {
			double angle = theta - velocity.getAngle();
			
			if(Math.abs(angle) > Math.PI)
				angle -= Math.signum(angle) * 2*Math.PI;

			return angle;
		}
	}
	
	public static Area createShape() {
		
		int[] x = {0, (int) (100 * Math.cos(Math.PI/6)), 
					 (int) (-100 * Math.cos(Math.PI/6))};
		int[] y = {(int) (-100), (int) (100 * Math.sin(Math.PI/6)),
								 (int) (100 * Math.sin(Math.PI/6))};
		return new Area(new Polygon(x, y, 3));
	}
}