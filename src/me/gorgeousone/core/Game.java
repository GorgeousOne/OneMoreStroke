package me.gorgeousone.core;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Game {

	private JFrame frame;
	private Canvas canvas;
	
	private boolean exitRequested;
	
	private int fps;
	private double frameInterval;
	private long accumulator;
	
	private Ball ball;
	private Wall wall;
	private Background background;
	
	public Game() {
		
		setFPS(100);
		setupCanvas();
		setupFrame();
		
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		Camera camera = canvas.getCamera();
		
		ball = new Ball();
		ball.setScale(1.5, 1.5);
		ball.setRotation(-Math.PI/2);
		ball.setSpeed(2);
		
		wall = new Wall(canvasWidth, canvasHeight, camera);
		background = new Background(canvasWidth, canvasHeight, camera);
		
//		camera.setZoom(0.5f);
		canvas.addDrawable(background);
		canvas.addDrawable(wall);
		canvas.addDrawable(ball);
		runGame();
	}
	
	public void setFPS(int newFPS) {
		fps = newFPS;
		frameInterval = 1000d / fps;
	}
	
	private void setupFrame() {
		
		frame = new JFrame("One More Stroke");

		frame.setContentPane(canvas);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		frame.addWindowListener(new WindowAdapter() {
//			
//			@Override
//			public void windowClosing(WindowEvent e) {
//				//save anything important or so
//			}
//		});
		
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
		Rectangle screenBounds = frame.getGraphicsConfiguration().getBounds();
		
		int fHeight = (int) screenBounds.getHeight() - screenInsets.top - screenInsets.bottom;
		int fWidth = (int) (fHeight * 5/9);
		
		frame.setPreferredSize(new Dimension(fWidth, fHeight));
		frame.setSize(fWidth, fHeight);
		frame.setLocation((int) (screenBounds.getWidth()/2 - fWidth/2), screenInsets.top);
		frame.setVisible(true);
	}
	
	private void setupCanvas() {
	
		canvas = new Canvas();
//		canvas.setBackground(Color.BLACK);
	}
	
	private void runGame() {
		
		long lastDisplayUpdate = System.currentTimeMillis();
				
		while(!exitRequested) {
			
			accumulator = System.currentTimeMillis() - lastDisplayUpdate;
			
			if(accumulator < frameInterval)
				continue;
			
			while(accumulator >= frameInterval) {
				
				makeGameLogic();
				accumulator -= frameInterval;
			}
			
			lastDisplayUpdate = System.currentTimeMillis();
			canvas.repaint();
		}
	}
	
	private void makeGameLogic() {
		
		ball.move();
		ball.setPos(100 * Math.sin(canvas.getCamera().getY() / 100), ball.getPos().getY());
		
		canvas.getCamera().setLocation(ball.getPos().getX()/2, ball.getPos().getY());
	}
}
