package view.window;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import view.drawables.Drawable;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	private Panel panel;
	private int fps;
	
	public Window(String title) {
		super(title);
		
		panel = new Panel();
		getContentPane().add(panel);
		loadIconImage("/res/images/Icon.png");
		
		fps = 60;
	}
	
	public void loadIconImage(String path) {
		try {
			setIconImage(ImageIO.read(getClass().getResource(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getFps() {
		return fps;
	}
	
	public void setFps(int fps) {
		this.fps = Math.max(1, fps);
	}
	
	public BufferedImage getScreenShot() {
		return panel.getScreenShot();
	}
	
	public void addCamera(Camera c) {
		panel.addCamera(c);
	}
	
	@Override
	public Component add(Component comp) {
		panel.add(comp);
		return comp;
	}
	
	public ArrayList<Drawable> getDrawables() {
		return panel.getDrawables();
	}
	
	public void addDrawable(Drawable d) {
		panel.addDrawable(d);
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
	
	@Override
	public void paint(Graphics g) {
		panel.repaint();
	}
}