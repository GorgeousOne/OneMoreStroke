package control;

import view.window.Panel;
import view.window.Window;

public class Menu {

	@SuppressWarnings("unused")
	private Window frame;
	@SuppressWarnings("unused")
	private Panel panel;
	private int fWidth, fHeight;
	
	public Menu(Window w) {
	
		fWidth = w.getContentPane().getWidth();
		fHeight = w.getContentPane().getHeight();
		
		System.out.println(fWidth + ", " + fHeight);
		this.frame = w;
		 
		panel = new Panel(fWidth, fHeight);
	}
}
