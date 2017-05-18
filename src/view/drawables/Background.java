package view.drawables;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import view.window.Camera;

public class Background extends Drawable{

	@SuppressWarnings("unused")
	private Area grid;
	
	private final double width = 16;
	private final double height = 42;
	
	private int fWidth, fHeight;
	private Camera camera;
	
	public Background(int layer, int fWidth, int fHeight, Camera camera) {
		super(initTiles2(), layer);
		
		this.fWidth = fWidth;
		this.fHeight = fHeight;
		this.camera = camera;
		
		setScale(fWidth/width, fWidth/width);
	}
	
	@Override
	public void fill(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform old = g2.getTransform();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		g2.setColor(Drawable.DARK_GRAY);
		g2.fillRect(0, (int) (camera.getY() - fHeight/2/camera.getZoom()), fWidth, (int) (fHeight/camera.getZoom()));

		g2.setColor(Drawable.GRAY);
		
		int sHeight = (int) (fHeight / camera.getZoom());
		double tilesY = Math.floor((camera.y - sHeight/2) / (height * getScale().getX()));

		while(tilesY * height*getScale().getX() < camera.y + sHeight/2) {
			
			g2.translate(fWidth/2,  tilesY  * height*getScale().getX());
			g2.fill(getShape());
			g2.translate(-fWidth/2, -tilesY  * height*getScale().getX());
			
			tilesY ++;
		}

		g2.setTransform(old);
	}
	
	public static Area initTiles2() {

		Area tiles = new Area();
		int[] pX, pY;
			
		pX = new int[] {-4, -2, -2, -4, -6, -6};
		pY = new int[] {0, 2, 6, 8, 6, 2};
		tiles.add(new Area(new Polygon(pX, pY, 6)));
		
		pX = new int[] {6, 6, 4, 2, 2};
		pY = new int[] {2, 10, 12, 10, 6};
		tiles.add(new Area(new Polygon(pX, pY, 5)));
		
		pX = new int[] {-6, -6, -8, -8};
		pY = new int[] {8, 16, 14, 10};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		pX = new int[] {0,  0, -2, -2};
		pY = new int[] {12, 18, 16, 14};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		pX = new int[] {3, 5, 5, 3, 1, 1};
		pY = new int[] {14, 16, 20, 22, 20, 16};
		tiles.add(new Area(new Polygon(pX, pY, 6)));
		
		pX = new int[] {0, 0, -2, -2};
		pY = new int[] {18, 22, 24, 20};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		pX = new int[] {-8, -6, -6, -8};
		pY = new int[] {20, 22, 26, 28};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
	
		pX = new int[] {8, 8, 6, 6};
		pY = new int[] {20, 28, 30, 22};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		pX = new int[] {2, 4, 4, 2};
		pY = new int[] {26, 28, 32, 30};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		pX = new int[] {0, 0, -2, -4, -4};
		pY = new int[] {28, 36, 38, 36, 32};
		tiles.add(new Area(new Polygon(pX, pY, 5)));
		
		
		pX = new int[] {-6, -6, -8, -8};
		pY = new int[] {32, 40, 42, 34};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		pX = new int[] {4, 4, 2};
		pY = new int[] {32, 36, 34};
		tiles.add(new Area(new Polygon(pX, pY, 3)));
		
		pX = new int[] {2, 4, 4, 2};
		pY = new int[] {38, 40, 44, 42};
		tiles.add(new Area(new Polygon(pX, pY, 4)));
		
		return tiles;
	}
}
