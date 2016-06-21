
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

public class Convas extends JPanel {
	Color color;
	
	public Convas () {
		
		this.color = Color.black;
		
	}
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
//		g.setColor(Color.black);
//		g.drawOval(0, 0, width, height);
//		g.drawString("3", 200,200);
	}

}
