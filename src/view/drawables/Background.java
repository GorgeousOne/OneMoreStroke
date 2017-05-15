package view.drawables;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import view.window.Camera;

public class Background extends Drawable{

	@SuppressWarnings("unused")
	private Area grid;
	
	private Polygon[] tiles;
	private final double width = 16;
	private final double height = 42;
	
	Rectangle screen;
	private Camera camera;
	
	public Background(int layer, Rectangle screen, Camera camera) {
		super(createShape(), layer);
		
		this.screen = screen;
		this.camera = camera;
		
		setScale(screen.getWidth()/width, screen.getWidth()/width);
		initTiles();
	}

	private static Area createShape() {
		Area shape = new Area(new Rectangle2D.Double());
		return shape;
	}
	
	public void update() {
	}
	
	@Override 
	public void fill(Graphics g) {
		
//		System.out.println((int) (screen.getHeight() - camera.getY()));
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		g2.setColor(Drawable.DARK_GRAY);
		g2.fillRect(0, (int) (camera.getY() - screen.getHeight()/2), (int) screen.getWidth(), (int) screen.getHeight());
		
		AffineTransform old = g2.getTransform();
		
		g2.setColor(Drawable.GRAY);
		g2.translate(screen.getWidth()/2 + (camera.getX() - screen.getWidth()/2)/4, 0);
		g2.scale(screen.getWidth() / width, screen.getWidth() / width);

		for(Polygon p : tiles) {
			
			if(p.getBounds().getMinY() * g2.getTransform().getScaleX() > camera.getY() + screen.getHeight()/2)
				p.translate(0, (int) -height);
			else if(p.getBounds().getMaxY() * g2.getTransform().getScaleX() < camera.getY() - screen.getHeight()/2)
				p.translate(0, (int) height);

			g2.fill(p);
		}
		
		g2.setTransform(old);
	}
	
	private void initTiles() {
		
		tiles = new Polygon[13];
		int[] pX, pY;
		
		pX = new int[] {-4, -2, -2, -4, -6, -6};
		pY = new int[] { 0,  2,  6,  8,  6,  2};
		tiles[0] = new Polygon(pX, pY, 6);
		
		pX = new int[] {6,  6,  4,  2, 2};
		pY = new int[] {2, 10, 12, 10, 6};
		tiles[1] = new Polygon(pX, pY, 5);
		
		pX = new int[] {-6, -6, -8, -8};
		pY = new int[] { 8, 16, 14, 10};
		tiles[2] = new Polygon(pX, pY, 4);
		
		pX = new int[] {0,   0, -2, -2};
		pY = new int[] {12, 18, 16, 14};
		tiles[3] = new Polygon(pX, pY, 4);
		
		pX = new int[] { 3,  5,  5,  3,  1,  1};
		pY = new int[] {14, 16, 20, 22, 20, 16};
		tiles[4] = new Polygon(pX, pY, 6);
		
		pX = new int[] { 0,  0, -2, -2};
		pY = new int[] {18, 22, 24, 20};
		tiles[5] = new Polygon(pX, pY, 4);
		
		pX = new int[] {-8, -6, -6, -8};
		pY = new int[] {20, 22, 26, 28};
		tiles[6] = new Polygon(pX, pY, 4);
	
		pX = new int[] { 8,  8,  6,  6};
		pY = new int[] {20, 28, 30, 22};
		tiles[7] = new Polygon(pX, pY, 4);
		
		//TODO
		pX = new int[] {2, 4, 4, 2};
		pY = new int[] {26, 28, 32, 30};
		tiles[8] = new Polygon(pX, pY, 4);
		
		pX = new int[] {0, 0, -2, -4, -4};
		pY = new int[] {28, 36, 38, 36, 32};
		tiles[9] = new Polygon(pX, pY, 5);
		
		
		pX = new int[] {-6, -6, -8, -8};
		pY = new int[] {32, 40, 42, 34};
		tiles[10] = new Polygon(pX, pY, 4);
		
		pX = new int[] {4, 4, 2};
		pY = new int[] {32, 36, 34};
		tiles[11] = new Polygon(pX, pY, 3);
		
		pX = new int[] {2, 4, 4, 2};
		pY = new int[] {38, 40, 44, 42};
		tiles[12] = new Polygon(pX, pY, 4);
	}
}
