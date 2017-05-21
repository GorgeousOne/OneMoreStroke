package insertClass;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;

public class Explosion extends Drawable{

	private long duration;
	private Particle[] particles;
	private boolean isLaunched = false;
	
	public Explosion(Point2D center, double range, int pieces, long duration) {
		super(null, 0);

		this.duration = duration;
		
		this.particles = new Particle[pieces];
		
		for(int i = 0; i < pieces; i++)
			this.particles[i] = new Particle(center, 10, range/2);
	}

	public void launch() {
		
		if(isLaunched)
			return;
		
		isLaunched = true;
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				
				for(int i = 0; i < duration/16; i++) {
					
					if(i < particles.length)
						particles[i].update();
					
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
		for(Particle p : particles)
			p.fill(g);
	}
	
	private class Particle extends Drawable {
		
		@SuppressWarnings("unused")
		private double spin;
		private double speed;
		
		public Particle(Point2D center, double radius, double speed) {
			super(createTriangle(radius), 0);
			
			setPos(center);
			setRotation(Math.random() * 2*Math.PI);
			
			this.speed = Math.random() * speed;
			//this.spin = Math.random() * Math.PI/8 - Math.PI/16;
		}
		
		public void update() {
			//rotate(spin);
			//spin *= 0.9;
			
			translate(Math.cos(getRotation()) * speed, Math.sin(getRotation()) * speed);
			speed /= 2;
			//speed = speed.multiply(0.5);
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
