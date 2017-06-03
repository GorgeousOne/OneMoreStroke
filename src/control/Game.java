package control;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import control.listener.KeyInput;
import control.listener.WindowInput;
import view.components.Counter;
import view.drawables.*;
import view.window.Camera;
import view.window.Window;

public class Game {

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

	private ArrayList<Node> nodes;

	private Counter counter;
	
	private Timer timer;
	private int fWidth, fHeight;	//Groesse des Frames fuer JPanel

	public Game(Window w) {

		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();
		
		frame = w;
		frame.clear();
		frame.setCamera(camera = new Camera());
		initKeyInput();
		initWindowStateInput();
		
		ball = new Ball(1, Color.WHITE, fWidth, fHeight, camera);
		ball.setSpeed(fWidth/frame.getFps() * 1.3);
		
		counter = new Counter(fWidth);
		counter.setLocation(fWidth/16, fWidth/40);
		counter.setPointDistance(ball.getSpeed() * frame.getFps()/3); //wenn Ball gerade fliegt, dauert ein Score-Punkt 1/3s

		tail = new Tail(2, Color.WHITE, ball, fHeight, camera);
		rope = new Rope(3, Color.WHITE, ball.getPos(), fWidth/150);
		ring = new DashedRing(4, Drawable.ORANGE, 64, fWidth/200);
		wall = new Wall(5, fWidth, fHeight, camera);
		background = new Background(6, fWidth, fHeight, camera);

		frame.addDrawable(ball);
		frame.addDrawable(tail);
		frame.addDrawable(rope);
		frame.addDrawable(ring);
		frame.addDrawable(wall);
		frame.addDrawable(background);
		frame.add(counter);
		
		nodes = new ArrayList<>();
		spawnNode();

		timer = new Timer(true);
		loop();
	}
	
	private void loop() {
		
		//Extra Runnable um Zeit zu sparen
		Runnable r = new Runnable() {
			@Override
			public void run() {

				//male gestrichelten Ring um umkreiste Nodes
				if(ball.isSpinning() && !ring.isVisible())
					ring.appear(ball.getNode().getPos(), ball.getSpinRadius());
				
				//ueberpruefe crashes und connecte mit Nodes
				ball.update(getSolids());
				//erhoehe score wenn noetig
				counter.update(ball.getPos());
				
				//male Rope zwischen Ball und Node
				if(ball.isInOrbit() && !rope.isConnected())
					rope.connectTo(ball.getNode());
				else if(!ball.isInOrbit() && rope.isConnected())
					rope.disconnect();
			}
		};
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
				Thread t = new Thread(r);
				t.start();
				
				//Spiel beenden bei crash;
				
				//spawn neue nodes, wenn die letzten sichtbar sind
				if(nodes.get(nodes.size()-1).isVisible())
					spawnNode();
				
				if(!ball.hasCrashed())
					ball.updateMove();
				else
					ball.explode(getSolids());
				//updated alle Drawables und damit passive Eigenschaften aka Hintergrund Bewegung... Node-Drehen
				frame.repaint();
			}
		}, 0, 1000/frame.getFps());
	}
	
	//erzeugt neuen Node
	public void spawnNode() {

		Node n;
		double posX, posY, scale;
		
		//ganz zu Anfang
		if(nodes.isEmpty()) {
			posX = fWidth/6 + (int) (Math.random() + 0.5) * fWidth*2/3;	//das ist halt links oder rechts, nicht Mitte
			posY = -4*counter.getPointDistance();						
			scale = fWidth/5000d;
			
			n = new Node(0, Drawable.BLUE, fHeight, camera);
			n.setScale(scale, scale);
			n.setPos(posX, posY);
		
		//irgendwo im Spiel random
		}else {
			Node lastNode = nodes.get(nodes.size()-1);
			
			//Nodes wachsen begrenzt
			double increasement = 0.4 - (0.4 - 0.1) * Math.pow(Math.E, -0.005 * counter.getScore());
			scale = fWidth/5000d;
			scale += fWidth/500d * Math.random() * increasement;
			
			posX = fWidth*1/6d + Math.random() * fWidth*2/3d; //das ist 1/6 Rand zu beiden Seiten
			//posX = fWidth/2;
			posY = lastNode.getPos().getY() - Math.random() * fWidth*2/5d - fWidth*2/5d;
			
			n = new Node(2, Drawable.nextColor(lastNode.getColor()), fHeight, camera);
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
			if(n.isVisible())
				visNodes.add(n);
			//if(Math.abs(camera.getY() - n.getPos().getY()) < fHeight/2/camera.getZoom() + n.getRadius())
			//	visNodes.add(n);
		return visNodes;
	}
	
	//alle Mauern, Nodes... ja
	public ArrayList<Drawable> getSolids() {
		ArrayList<Drawable> solids = new ArrayList<>();
		solids.add(wall);
		solids.addAll(getVisibleNodes());
		return solids;
	}
	
	public void initKeyInput() {
		frame.addKeyListener(keyInput = new KeyInput());
		keyInput.addKeyPressAction(KeyEvent.VK_SPACE, e -> onKeyPress(KeyEvent.VK_SPACE));
		keyInput.addKeyPressAction(KeyEvent.VK_ESCAPE, e -> onKeyPress(KeyEvent.VK_ESCAPE));
		keyInput.addKeyReleasedAction(KeyEvent.VK_SPACE, e -> onKeyRelease(KeyEvent.VK_SPACE));
	}
	
	private void onKeyPress(int key) {
		if(key == KeyEvent.VK_SPACE && !ball.isInOrbit()) {
			ball.enterOrbit(getVisibleNodes());
			wall.setNextColor(Drawable.LIGHT_BLUE);
		}
		if(key == KeyEvent.VK_ESCAPE)
			exit();
	}
	
	private void onKeyRelease(int key) {
		if(key == KeyEvent.VK_SPACE && ball.isInOrbit()) {
			ball.leaveOrbit(getSolids());
			ring.setVisible(false);
		}
 	}
	
	private void initWindowStateInput() {
		frame.addWindowStateListener(windowInput = new WindowInput());
		windowInput.addChangeStateAction(WindowEvent.WINDOW_ICONIFIED, e -> onWindowAction(WindowEvent.WINDOW_ICONIFIED));
		windowInput.addChangeStateAction(WindowEvent.WINDOW_CLOSING, e -> onWindowAction(WindowEvent.WINDOW_CLOSING));
	}

	private void onWindowAction(int state) {
		//TODO was ausdenken
		if(state == WindowEvent.WINDOW_ICONIFIED)
			this.exit();
		
		if(state == WindowEvent.WINDOW_CLOSING)
			System.out.println("closing");
	}
	
	@SuppressWarnings("unused")
	private void compareScore() {
	
		System.out.println(System.getProperty("user.dir"));
		File highscore = new File(System.getProperty("user.dir") + "/src/res/texts/highscore.txt");
		Scanner s = null;
		
		try {
			s = new Scanner(highscore);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int score = Integer.parseInt(s.nextLine());
		
		if(score < counter.getScore()) {
			try {
				PrintWriter pw = new PrintWriter("/res/texts/highscore.txt", "UTF-8");
				pw.write(Integer.toString(counter.getScore()));
				pw.close();
				System.out.println(counter.getScore() + " is new high");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		
		}else
			System.out.println("yo noob");
	}
	
	public void exit() {
//		compareScore();
		timer.cancel();
		new Menu(frame);
	}
}