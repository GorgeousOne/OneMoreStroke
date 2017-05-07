package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import view.components.Button;
import view.drawables.Drawable;
import view.window.Panel;
import view.window.Window;

public class Menu {

	private Window frame;
	private Panel panel;
	private Button start;
	
	private int fWidth, fHeight;
	
	public Menu(Window w) {
	
		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();

		panel = new Panel(fWidth, fHeight);

		frame = w;
		frame.add(panel);
		frame.setVisible(true);
		
		initStartButton();

		loop();
	}
	
	private void loop() {
		
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				panel.repaint();
			}
		}, 0, 1000/60);
	}
	
	private void initStartButton() {
		start = new Button();
		start.setBackground(Drawable.RED);
		start.setBounds(0, 0, fWidth/6, fWidth/6);
		start.setLocation(fWidth*5/6, fWidth/6);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.clear();
				new Game();
			}
		});
		panel.add(start);
	}
	
}
