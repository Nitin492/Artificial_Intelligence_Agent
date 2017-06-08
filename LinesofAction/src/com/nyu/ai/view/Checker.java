package com.nyu.ai.view;
/**
*
* @author Nitin
*/
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
/**
 * 
 * @author Nitin
 *This class is used to instantiate the checker pieces
 *in form of icons
 */
public class Checker implements Icon{
	public Color color;
	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 80;
	}

	public Checker(Color color) {
		this.color = color;
	}
	public Checker() {
		this.color = Color.red;;
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 80;
	}

	@Override
	public void paintIcon(Component arg0, Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		//System.out.println("helloe");
	    g.setColor(color);
	    g.fillOval(5, 5, 100, 100);
	    g.drawOval(5, 5, 100, 100);
	}
	
}
