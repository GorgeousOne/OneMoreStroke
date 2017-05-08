package control;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import view.components.Counter;
import view.drawables.*;
import view.window.Camera;
import view.window.Window2;

public class Game2 {

	private Window2 frame;			//JFrame
	private KeyInput keyInput;
	private Camera camera;
	
	private Ball ball;				//Geschoss, mit dem gespielt wird
	private Wall wall;
	private Tail tail;
	private Rope rope;
	private DashedRing ring;
	private ArrayList<Node> nodes;

	private Counter counter;
	
	private int fWidth, fHeight;	//Groesse des Frames fuer JPanel
	
	public Game2(Window2 w) {

		frame = w;
		frame.clear();
		frame.addKeyListener(keyInput = new KeyInput());

		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();
		
		camera = new Camera();
		frame.addCamera(camera);

		ball = new Ball(0, Color.WHITE, fWidth);
		ball.setSpeed(fWidth/frame.getFps() * 1.3);
		
		counter = new Counter(fWidth, "/res/fonts/terminat.ttf", (float) fWidth/10);
		counter.setLocation(fWidth/16, fWidth/40);
		counter.setPointDistance(ball.getSpeed() * frame.getFps()/3);		//wenn Ball gerade fliegt, dauert ein Score-Punkt 1/2s

		tail = new Tail(1, Color.WHITE, ball, 200);
		rope = new Rope(3, Color.WHITE, ball.getPos(), fWidth/150);
		ring = new DashedRing(4, Drawable.ORANGE, 64, fWidth/200);
		wall = new Wall(5, Drawable.RED, fWidth, fHeight, camera);

		frame.addDrawable(ball);
		frame.addDrawable(tail);
		frame.addDrawable(rope);
		frame.addDrawable(ring);
		frame.addDrawable(wall);
		frame.add(counter);
		
		nodes = new ArrayList<>();
		spawnNode();

		loop();
	}
	
	
	private void loop() {
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				
				//laesst Ball um Node kreisen, wenn SPACE gedrueckt
				if(keyInput.isPressed(KeyEvent.VK_SPACE) && !ball.isInOrbit())
					ball.enterOrbit(getVisibleNodes());
					//ball.rotate(0.1);
				else if(!keyInput.isPressed(KeyEvent.VK_SPACE) && ball.isInOrbit()) {
					ball.leaveOrbit(getSolids());
					ring.setVisibility(false);
				}
				
				if(ball.isSpinning() && !ring.isVisible()) {
					//mache neuen gestrichelten Ring auf Radius von Ball um Balls Node
					ring.setPos(ball.getNode().getPos());
					ring.setScale(ball.getSpinRadius() / 100, ball.getSpinRadius() / 100);
					ring.setVisibility(true);
				}

				ball.update(getSolids());
				counter.update(ball.getPos());
				
				if(ball.isInOrbit() && !rope.isConnected())
					rope.connectTo(ball.getNode());
				else if(!ball.isInOrbit() && rope.isConnected())
					rope.disconnect();
			}
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
				Thread t = new Thread(r);
				t.start();
				
				//Spiel beenden bei crash;
				if(ball.hasCrashed()) {
					t.interrupt();
					timer.cancel();
					return;
				}
				
				//Spiel schliessen per Esc
				if(keyInput.isPressed(KeyEvent.VK_ESCAPE)) {
					t.interrupt();
					frame.dispose();
					timer.cancel();
					return;
				}
				
				ball.move2();
				moveCamera();
				
				//spawn immer neue Nodes
				if(getVisibleNodes().contains(nodes.get(nodes.size()-1)))
					spawnNode();

				for(Node n : nodes)
					//mache Node sichtbar, wenn er im Fenster ist
					if(getVisibleNodes().contains(n)) {
						if(!n.isVisible()) {
							n.setVisibility(true);
						}
						n.update(frame.getFps());
					}else
						if(n.isVisible())
							n.setVisibility(false);
				
				rope.update(frame.getFps());
				wall.update();
				tail.update(frame.getFps());
				frame.repaint();
			}
		}, 0, 1000/frame.getFps());
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
		if(nodes.isEmpty()) {
			posX = fWidth/6 + (int) (Math.random() + 0.5) * fWidth*2/3;	//das ist halt links oder rechts, nicht Mitte
			//posX = fWidth/2;
			posY = -4*counter.getPointDistance();						
			scale = fWidth/5000d;
			
			n = new Node(2, Drawable.BLUE);
			n.setScale(scale, scale);
			n.setPos(posX, posY);
		
		//irgendwo random
		}else {
			Node lastNode = nodes.get(nodes.size()-1);
			//besteht aus begrenztem Wachstum * Math.random()
			
			double increasement = 0.4 - (0.4 - 0.1) * Math.pow(Math.E, -0.005 * counter.getScore());
			scale = fWidth/5000d;
			scale += fWidth/500d * Math.random() * increasement;
			
			posX = fWidth*1/6d + Math.random() * fWidth*2/3d; //das ist 1/6 Rand zu beiden Seiten
			//posX = fWidth/2;
			posY = lastNode.getPos().getY() - Math.random() * fWidth*2/5d - fWidth*2/5d;
			
			n = new Node(2, Drawable.nextNeonColor(lastNode.getColor()));
			n.setScale(scale, scale);
			n.setPos(posX, posY);
		}
		nodes.add(n);
		frame.addDrawable(n);
	}
	
	//gibt Liste aller Nodes im Bildschirm zurück
	public ArrayList<Node> getVisibleNodes() {
		ArrayList<Node> visNodes = new ArrayList<>();
		
		for(Node n : nodes)
			if(Math.abs(camera.getY() - n.getPos().getY()) < fHeight/2 + n.getRadius())
				visNodes.add(n);
		return visNodes;
	}
	
	public ArrayList<Drawable> getSolids() {
		ArrayList<Drawable> solids = new ArrayList<>();
		solids.add(wall);
		solids.addAll(getVisibleNodes());
		return solids;
	}
}