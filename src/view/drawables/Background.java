package view.drawables;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

import view.window.Camera;

public class Background extends Drawable{

	@SuppressWarnings("unused")
	private Area grid;
	
	private final int width = 16;
	private final int height = 42;
	
	private int fWidth, fHeight;
	private Camera camera;
	
	public Background(int layer, int fWidth, int fHeight, Camera camera) {
		super(initTiles2(), layer);
		
		this.fWidth = fWidth;
		this.fHeight = fHeight;
		this.camera = camera;
		
		setScale((double) fWidth/width, (double) fWidth/width);
	}
	
	@Override
	public void fill(Graphics g) {
		
		long time = System.currentTimeMillis();

		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Drawable.DARK_GRAY);
		g2.fillRect(0, (int) (camera.getY() - fHeight/2/camera.getZoom()), fWidth, (int) (fHeight/camera.getZoom()));
		
		//fHeight, bloss auf den camera-zoom angepasst
		int sHeight = (int) (fHeight / camera.getZoom());
		
		//Auslenkung des Hintergrunds bei Bewegung weil der soll ja perspektivisch wirken
		double shiftX = camera.x/4 - fWidth/8;							//einfch 1/4 Camera.x
		double shiftY = camera.y/4 % (height * getScale().getX());		//1/4 Camera.y; Reset(durch %) nach 1x Shapelaenge
		
		//Wert, bei dem Tiles angefangen werden zu malen; das wird immer x TilesLaenge genommen
		double tilesY = Math.floor((camera.y - sHeight/2 - shiftY) / (height * getScale().getX()));
		
		double gridX = Math.ceil(-shiftX / getScale().getX());
		double gridY = Math.ceil((camera.y - sHeight/2 - shiftY) / getScale().getX());

		//Hintergrund nicht ueber Raender malen
		g2.clipRect(0, (int) (camera.y - sHeight/2), fWidth, sHeight);
		g2.setColor(Drawable.MIDDLE_GRAY);
		
		//das Muster
		while(tilesY * height*getScale().getX() + shiftY < camera.y + sHeight/2) {
			g2.translate( fWidth/2 + shiftX,  tilesY * height*getScale().getX() + shiftY);
			g2.fill(getShape());
			g2.translate(-fWidth/2 - shiftX, -tilesY * height*getScale().getX() - shiftY);

			tilesY++;
		}
		
		g2.setColor(Drawable.GRAY);
		
		//Laengsstreifen
		while(gridX * getScale().getX() + shiftX < fWidth) {
			g2.draw(new Line2D.Double(gridX * getScale().getX() + shiftX, camera.y - sHeight/2, 
									  gridX * getScale().getX() + shiftX, camera.y + sHeight/2));
			gridX++;
		}
		
		//Querstreifen
		while(gridY * getScale().getX() + shiftY < camera.y + sHeight/2) {
			g2.draw(new Line2D.Double(0, gridY * getScale().getX() + shiftY, 
								 fWidth, gridY * getScale().getX() + shiftY));
			gridY++;
		}
		
		System.out.println(System.currentTimeMillis() - time);
		g2.setClip(null);
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