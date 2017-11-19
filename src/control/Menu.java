package control;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import view.components.Button;
import view.drawables.Background;
import view.drawables.Drawable;
import view.drawables.Wall;
import view.window.Camera;
import view.window.Window;

public class Menu {

	private Window frame;
	private Button start;
	
	private int fWidth, fHeight;
	private boolean isStopped = false;
	
	private Background bg;
	private Wall wall;
	private Camera camera;
	
	public Menu(Window w) {

		frame = w;
		frame.clear();
		
		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();

		camera = frame.getCamera();
		camera.setLocation(fWidth/2, 0);
		
		wall = new Wall(0, fWidth, fHeight, camera);		
		bg = new Background(1, fWidth, fHeight, camera);
		
		frame.addDrawable(wall);
		frame.addDrawable(bg);

		initStartButton();
		loop();
	}
	
	private void initStartButton() {
		
		start = new Button();
		frame.add(start);

		start.setBackground(Drawable.RED);
		
		start.setPreferredSize(new Dimension(fWidth/6, fWidth/6));
		start.setBounds(0, 0, fWidth/6, fWidth/6);
		start.setLocation(fWidth*5/6, fWidth/6);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stop();
				new Game(frame);
			}
		});
	}
	
	public void stop() {
		isStopped = true;
	}
	
	private void loop() {
		
		Timer t = new Timer(true);
		t.schedule(	new TimerTask() {
			@Override
			public void run() {
				
				if(isStopped)
					t.cancel();
				
				frame.repaint();
			}
		}, 0, 1000/frame.getFps());
	}
}
