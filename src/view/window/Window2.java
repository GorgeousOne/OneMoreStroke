package view.window;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import view.components.Button;
import view.drawables.Drawable;

public class Window2 extends JFrame{

	private static final long serialVersionUID = 1L;
	private Panel2 panel;
	private int fps;
	
	public Window2(String title) {
		super(title);
		
		panel = new Panel2();
		getContentPane().add(panel);
		
		setFps(60);
	}
	
	public void loadIconImage(String path) {
		try {
			setIconImage(ImageIO.read(getClass().getResource(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Panel2 getPanel() {return panel;}
	
	public int getFps() {return fps;}
	public void setFps(int fps) {this.fps = Math.max(1, fps);}
	
	public void addCamera(Camera c) {
		panel.addCamera(c);
	}
	
	public void addButton(Button b) {
		panel.add(b);
	}
	
	public ArrayList<Drawable> getShapes() {
		return panel.getDrawables();
	}
	
	public void addDrawable(Drawable d) {
		panel.addDrawable(d);
		System.out.println("add");
	}
	
	public void removeDrawable(Drawable d) {
		panel.removeDrawable(d);
	}
	
	public void clear() {
		panel.clear();
	}
	
	public void activate() {
		pack();
		panel.setSize(getContentPane().getSize());
		setVisible(true);
	}
}