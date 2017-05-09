package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import view.components.Button;
import view.drawables.Drawable;
import view.window.Window;

public class Menu {

	private Window frame;
	private Button start;
	
	@SuppressWarnings("unused")
	private int fWidth, fHeight;
	private boolean hasStopped = false;
	
	public Menu(Window w) {

		frame = w;
		frame.activate();

		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();
//		System.out.println(fWidth + ", " + fHeight);

		initStartButton();
		loop();
	}
	
	private void initStartButton() {
		
		start = new Button();

		start.setBackground(Drawable.RED);
		start.setBounds(0, 0, fWidth/6, fWidth/6);
		start.setLocation(fWidth*5/6, fWidth/6);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Game(frame);
				stop();
			}
		});
		frame.add(start);
	}
	
	public void stop() {
		hasStopped = true;
	}
	
	private void loop() {
		
		Timer t = new Timer(true);
		t.schedule(	new TimerTask() {
			@Override
			public void run() {
				
				if(hasStopped)
					t.cancel();
				
				frame.repaint();
			}
		}, 0, 1000/frame.getFps());
	}
}
