package classPackage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import graphicsPackage.*;

public class Game {

	private Window frame;		//JFrame
	private Panel panel;		//JPanel zum Malen
	private Ball ball;		//Geschoss, mit dem gespielt wird
	private Wall wall;
	private Counter counter;
	private Trail trail;
	private DashedRing ring;
	private Rope rope;
	
	private int fWidth;		//definiert Komponenten(JPanel)groesse
	private int fHeight;
	
	private int fps;			//legt Updates aller Objekte/sec fest
	private Point2D camera;
	
	private ArrayList<Pivot> pivots;
	
	public Game() {

		fps = 60;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		fHeight = (int) screenSize.getHeight();
		fWidth = (int) (fHeight * 5/8);
		
		ball = new Ball(0, fWidth, fHeight);
		ball.setSpeed(400.0 / fps);
		
		camera = ball.getCamera();
		
		panel = new Panel(fWidth, fHeight);
		panel.setCamera(camera);
		
		frame = new Window(fWidth, fHeight);
		frame.setLocation((int) (screenSize.getWidth()/2 - fWidth/2) , 0);


		frame.add(panel);	//das bleibt genau hinter der Deklarierung

		counter = new Counter(0);	//wenn Ball gerade fliegt, dauert ein Score-Punkt 1/2s
		counter.setPos(new Point2D.Double(30, 60));
		counter.setPointDistance(ball.getSpeed() * fps/3);
		
		trail = new Trail(1, ball, 200);
		
		rope = new Rope(3, ball.getPos(), fWidth/150);
		ring = new DashedRing(4, 64, fWidth/200);
		wall = new Wall(5, fWidth, fHeight);
		
		panel.addDrawable(counter);
		panel.addDrawable(ball);
		panel.addDrawable(trail);
		panel.addDrawable(rope);
		panel.addDrawable(ring);
		panel.addDrawable(wall);
		panel.update();
		
		pivots = new ArrayList<>();
		spawnPivot();
		
		rope.connectTo(pivots.get(0));
		
		loop();
	}
	
	private void loop() {
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				//Spiek beenden bei crash;
				if(ball.hasCrashed())
					timer.cancel();;
				
				//Spiel schliessen per Esc
				if(frame.isKeyPressed(KeyEvent.VK_ESCAPE)) {
					frame.dispose();
					return;
				}
				
				//laesst Ball um Pivot kreisen, wenn SPACE gedrueckt
				if(frame.isKeyPressed(KeyEvent.VK_SPACE) && !ball.isInOrbit())
					ball.enterOrbit(getVisPivots());
					//ball.rotate(0.1);
				else if(!frame.isKeyPressed(KeyEvent.VK_SPACE) && ball.isInOrbit()) {
					ball.leaveOrbit(getSolids());
					ring.setVisibility(false);
				}
				
				if(ball.isSpinning() && !ring.isVisible()) {
					//mache neuen gestrichelten Ring auf Radius von Ball um Balls Pivot
					ring.setPos(ball.getPivot().getPos());
					ring.setScale(ball.getSpinRadius() / 100);
					ring.setVisibility(true);
				}
				
				if(ball.isInOrbit() && !rope.isConnected())
					rope.connectTo(ball.getPivot());
				else if(!ball.isInOrbit() && rope.isConnected())
					rope.disconnect();
				
				//spawn immer neue Pivots
				if(getVisPivots().contains(pivots.get(pivots.size()-1)))
					spawnPivot();
				
				for(Pivot p : pivots)
					//mache Pivot sichtbar, wenn er im Fenster ist
					if(getVisPivots().contains(p)) {
						if(!p.isVisible()) {
							p.setVisibility(true);
						}
						p.update(fps);
					}else
						if(p.isVisible())
							p.setVisibility(false);
				
				ball.update(getSolids());
				counter.update(ball.getPos());
				trail.update(fps);
				rope.update(fps);
				panel.update();
				//Ende
				
			}
		}, 0, 1000/fps);
			
	}
	
	//erzeugt neuen Pivot
	public void spawnPivot() {

		Pivot p;
		double posX, posY, scale;
		
		//ganz zu Anfang
		if(pivots.isEmpty()) {
			posX = fWidth/6 + (int) (Math.random() + 0.5) * 2/3;	//das ist halt links oder rechts, nicht Mitte
			posY = -4*counter.getPointDistance();
			scale = 10; //10 + Math.random() * Math.sqrt(counter.getScore());
			
			p = new Pivot(2, Drawable.NEON_BLUE);
			p.setScale(0.1);
			p.setPos(posX, posY);
		
		//irgendwo random
		}else {
			Pivot lastP = pivots.get(pivots.size()-1);
			//begrenztes Wachstum: Pivot ist mind. (2*)10 gross, max. (2*)50, dem Maximum wird sich angenaehert; das noch ein bisl random
			scale = 0.1 + Math.random() * (0.5 - (0.4) * Math.pow(Math.E, -0.005 * counter.getScore()));
			posX = fWidth*1/6 + Math.random() * fWidth*2/3; //das ist 1/12 Rand zu beiden Seiten
			posY = lastP.getPos().getY() - Math.random() * 100 - 200;
			
			p = new Pivot(2, Drawable.nextNeonColor(lastP.getColor()));
			p.setScale(scale);
			p.setPos(posX, posY);
		}
		pivots.add(p);
		panel.addDrawable(p);
	}
	
	//gibt Liste aller Pivots im Bildschirm zurück
	public ArrayList<Pivot> getVisPivots() {
		ArrayList<Pivot> visPivots = new ArrayList<>();
		
		for(Pivot p : pivots)
			if(camera.distance(p.getPos()) + p.getRadius() < Point2D.distance(0, 0, fWidth/2, fHeight/2))
				visPivots.add(p);
		return visPivots;
	}
	
	public ArrayList<Drawable> getSolids() {
		ArrayList<Drawable> solids = new ArrayList<>();
		solids.add(wall);
		solids.addAll(getVisPivots());
		return solids;
	}
}
