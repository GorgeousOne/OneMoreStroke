package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import view.components.Button;
import view.drawables.Drawable;
import view.window.Window2;

public class Menu2 {

	private Window2 frame;
	private Button start;
	
	@SuppressWarnings("unused")
	private int fWidth, fHeight;
	
	public Menu2(Window2 w) {
	
		frame = w;
		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();

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
				new Game2(frame);
			}
		});
		frame.addButton(start);
	}
	
	private void loop() {
		
		Timer t = new Timer(true);
		t.schedule(	new TimerTask() {
			@Override
			public void run() {
				frame.repaint();
			}
		}, 0, 1000/frame.getFps());
	}
}
