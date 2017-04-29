package control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import view.components.Counter;
import view.drawables.*;
import view.window.Camera;
import view.window.Panel;
import view.window.Window;

public class Game {

	private Window frame;		//JFrame
	private Panel panel;		//JPanel zum Malen
	private KeyInput keyInput;
	private Camera camera;
	
	private Ball ball;			//Geschoss, mit dem gespielt wird
	private Wall wall;
	private Tail tail;
	private Rope rope;
	private DashedRing ring;
	private ArrayList<Node> Nodes;

	private Counter counter;
	
	private int fWidth;			//Groesse des Frames fuer JPanel
	private int fHeight;
	
	private int fps;			//legt Updates aller Objekte/sec fest
	
	public Game() {

		fps = 60;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		fHeight = (int) screenSize.getHeight() - 30;
		//fHeight /= 0.5;
		fWidth = (int) (fHeight * 5/8);						//480, 768
		
		ball = new Ball(0, Color.WHITE, fWidth);
		ball.setSpeed(fWidth/fps);
		
		camera = new Camera();
		//camera.setZoom(0.75f);
		
		panel = new Panel(fWidth, fHeight);
		panel.setCamera(camera);
		
		frame = new Window(fWidth, fHeight);
		frame.setLocation((int) (screenSize.getWidth()/2 - fWidth/2) , 0);		//mittig im Bildschirm
		
		frame.add(panel);	//das bleibt genau hinter der Deklarierung
		frame.addKeyListener(keyInput = new KeyInput());

		counter = new Counter(fWidth, "/view/fonts/terminat.ttf", (float) fWidth/10);
		counter.setLocation(fWidth/16 , fWidth/8);
		counter.setPointDistance(ball.getSpeed() * fps/3);		//wenn Ball gerade fliegt, dauert ein Score-Punkt 1/2s
		
		tail = new Tail(1, Color.WHITE, ball, 200);
		
		rope = new Rope(3, Color.WHITE, ball.getPos(), fWidth/150);
		ring = new DashedRing(4, Drawable.NEON_ORANGE, 64, fWidth/200);
		wall = new Wall(5, Drawable.NEON_RED, fWidth, fHeight, camera);
		
		panel.addDrawable(ball);
		panel.addDrawable(tail);
		panel.addDrawable(rope);
		panel.addDrawable(ring);
		panel.addDrawable(wall);
		panel.add(counter);
		
		panel.update();

		Nodes = new ArrayList<>();
		spawnNode();
		
		//zoomIn();
		run();
	}
	
	
	private void run() {
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				//Spiel beenden bei crash;
				if(ball.hasCrashed())
					timer.cancel();;
				
				//Spiel schliessen per Esc
				if(keyInput.isPressed(KeyEvent.VK_ESCAPE)) {
					frame.dispose();
					return;
				}
				
				//laesst Ball um Node kreisen, wenn SPACE gedrueckt
				if(keyInput.isPressed(KeyEvent.VK_SPACE) && !ball.isInOrbit())
					ball.enterOrbit(getVisNodes());
					//ball.rotate(0.1);
				else if(!keyInput.isPressed(KeyEvent.VK_SPACE) && ball.isInOrbit()) {
					ball.leaveOrbit(getSolids());
					ring.setVisibility(false);
				}
				
				ball.update(getSolids());
				moveCamera();
				
				if(ball.isSpinning() && !ring.isVisible()) {
					//mache neuen gestrichelten Ring auf Radius von Ball um Balls Node
					ring.setPos(ball.getNode().getPos());
					ring.setScale(ball.getSpinRadius() / 100, ball.getSpinRadius() / 100);
					ring.setVisibility(true);
				}
				
				if(ball.isInOrbit() && !rope.isConnected())
					rope.connectTo(ball.getNode());
				else if(!ball.isInOrbit() && rope.isConnected())
					rope.disconnect();
				
				//spawn immer neue Nodes
				if(getVisNodes().contains(Nodes.get(Nodes.size()-1)))
					spawnNode();
				
				for(Node n : Nodes)
					//mache Node sichtbar, wenn er im Fenster ist
					if(getVisNodes().contains(n)) {
						if(!n.isVisible()) {
							n.setVisibility(true);
						}
						n.update(fps);
					}else
						if(n.isVisible())
							n.setVisibility(false);
				
				counter.update(ball.getPos());
				wall.update();
				tail.update(fps);
				rope.update(fps);
				panel.update();
				//Ende
				
			}
		}, 0, 1000/fps);
			
	}
	
	private void moveCamera() {
		double posX, posY;
		
		posX = ball.getPos().getX();
		posY = ball.getPos().getY() - fHeight/6;
		camera.setLocation(posX, posY);
	}
	
	//erzeugt neuen Node
	public void spawnNode() {

		Node n;
		double posX, posY, scale;
		
		//ganz zu Anfang
		if(Nodes.isEmpty()) {
			posX = fWidth/6 + (int) (Math.random() + 0.5) * fWidth*2/3;	//das ist halt links oder rechts, nicht Mitte
			posY = -4*counter.getPointDistance();						
			scale = fWidth/5000d;
			
			n = new Node(2, Drawable.NEON_BLUE);
			n.setScale(scale, scale);
			n.setPos(posX, posY);
		
		//irgendwo random
		}else {
			Node lastNode = Nodes.get(Nodes.size()-1);
			//besteht aus begrenztem Wachstum * Math.random()
			
			double increasement = 0.4 - (0.4 - 0.1) * Math.pow(Math.E, -0.005 * counter.getScore());
			scale = fWidth/5000d;
			scale += fWidth/500d * Math.random() * increasement;
			
			posX = fWidth*1/6d + Math.random() * fWidth*2/3d; //das ist 1/6 Rand zu beiden Seiten
			posY = lastNode.getPos().getY() - Math.random() * fWidth*2/5d - fWidth*2/5d;
			
			n = new Node(2, Drawable.nextNeonColor(lastNode.getColor()));
			n.setScale(scale, scale);
			n.setPos(posX, posY);
		}
		Nodes.add(n);
		panel.addDrawable(n);
	}
	
	//gibt Liste aller Nodes im Bildschirm zurück
	public ArrayList<Node> getVisNodes() {
		ArrayList<Node> visNodes = new ArrayList<>();
		
		for(Node n : Nodes)
			if(camera.distance(n.getPos()) + n.getRadius() < Point2D.distance(0, 0, fWidth/2/camera.getZoom(),
																					fHeight/2/camera.getZoom()))
				visNodes.add(n);
		return visNodes;
	}
	
	public ArrayList<Drawable> getSolids() {
		ArrayList<Drawable> solids = new ArrayList<>();
		solids.add(wall);
		solids.addAll(getVisNodes());
		return solids;
	}
}
