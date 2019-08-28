package me.gorgeousone.core;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Line2D;

import me.gorgeousone.drawing.Drawable;
import me.gorgeousone.util.NeonColor;

public class Background extends Drawable {

	private final int tilesWidth = 16;
	private final int tilesHeight = 42;
	
	private int canvasWidth;
	private int canvasHeight;
	private Camera camera;
	
	public Background(int canvasWidth, int canvasHeight, Camera camera) {
		super(initTiles2());
		
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.camera = camera;
		
		setScale((double) canvasWidth/tilesWidth, (double) canvasWidth/tilesWidth);
	}
	
	@Override
	public void paint(Graphics2D graphics) {

		//calculate the height/length of the background tiles relation to the scale of the background
		double backgroundLength = tilesHeight * getScale().getX();
				
		//calculate the height and ceiling and bottom coordinates of the visible part in the canvas on the basis of the camera zoom
		int sceneHeight = (int) (canvasHeight / camera.getZoom());
		int sceneMinY = (int) (camera.getY() - sceneHeight/2);
		int sceneMaxY = (int) (camera.getY() + sceneHeight/2);

		graphics.setColor(NeonColor.DARK_GRAY);
		graphics.fillRect(-canvasWidth/2, sceneMinY, canvasWidth, sceneHeight);
		
		//calculate a slight offset for the background to achieve a 3d effect
		//x = 1/4 of camera x movement
		//y = 1/4 of camera y movement, which reset after each cycle of all tiles
		double shiftX = camera.getX()/4;
		double shiftY = camera.getY()/4 % backgroundLength;
		
		//Wert, bei dem Tiles angefangen werden zu malen; das wird immer x TilesLaenge genommen
		//y coordinate at which the tile will be started drawn
		double tilesY = Math.floor((camera.getY() - sceneHeight/2 - shiftY) / backgroundLength);
		
		//clip the graphics to the space between walls, ceiling and bottom
		graphics.clipRect(-canvasWidth/2, sceneMinY, canvasWidth, sceneHeight);
		graphics.setColor(NeonColor.MIDDLE_GRAY);
		
		//draw the pattern in the visible area of the scene
		while(tilesY * backgroundLength + shiftY < sceneMaxY) {
			
			graphics.translate(shiftX,  tilesY * backgroundLength + shiftY);
			graphics.fill(getShape());
			graphics.translate(-shiftX, -tilesY * backgroundLength - shiftY); 

			tilesY++;
		}
		
		double gridX = Math.ceil(-shiftX / getScale().getX()) - canvasWidth/2;
		double gridY = Math.ceil((camera.getY() - sceneHeight/2 - shiftY) / getScale().getX());

		graphics.setColor(NeonColor.GRAY);
		
		//draw the vertical lines of the grid
		while(gridX * getScale().getX() + shiftX < canvasWidth) {
			graphics.draw(new Line2D.Double(
					gridX * getScale().getX() + shiftX, sceneMinY, 
					gridX * getScale().getX() + shiftX, sceneMaxY));
			gridX++;
		}
		
		//draw the horizonzal lines of the grid
		while(gridY * getScale().getX() + shiftY < camera.getY() + sceneHeight/2) {
			graphics.draw(new Line2D.Double(
					-canvasWidth/2, gridY * getScale().getX() + shiftY, 
					 canvasWidth/2, gridY * getScale().getX() + shiftY));
			gridY++;
		}
		
		graphics.setClip(null);
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