package control;

import java.awt.geom.Point2D;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import control.listener.KeyInput;
import control.listener.WindowInput;
import view.drawables.Background;
import view.drawables.Ball;
import view.drawables.DashedRing;
import view.drawables.Rope;
import view.drawables.Tail;
import view.drawables.Wall;
import view.window.Camera;
import view.window.Window;


@SuppressWarnings("unused")
public class GameObject {

	private Window frame;			//JFrame
	private Camera camera;
	private KeyInput keyInput;
	private WindowInput windowInput;
	
	private Ball ball;				//Geschoss, mit dem gespielt wird
	private Tail tail;
	private Rope rope;
	private DashedRing ring;
	private Wall wall;
	private Background background;
	private int fWidth, fHeight;
	
	public GameObject(Window frame) {
		this.frame = frame;
		camera = frame.getCamera();
		
		fWidth = frame.getContentPane().getWidth();
		fHeight = frame.getContentPane().getHeight();
	}
	
	public void start() {
		
		focus(new Point2D.Double(fWidth/2, fHeight/2), 1f, 1500);
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
			}
		}, 0, 1000/frame.getFps());
	}

	private void focus(Point2D center, float zoom, long duration) {
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0; i < duration/16; i++) {
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
	

}
