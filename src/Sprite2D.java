import java.awt.*;
import javax.swing.*;

public class Sprite2D {

	protected double x;
	protected double y;
	protected Image myImage, myImage2;
	int framesDrawn=0;
	protected static int winWidth;
	
	public Sprite2D(Image i, Image i2) {

		myImage = i;
		myImage2 = i2;
	}
	
	
	public void setPosition(double xx, double yy) {
		x=xx;
		y=yy;
	}
	
	
	public void paint(Graphics g) {
		framesDrawn++;
		if(framesDrawn%100<50) {
			g.drawImage(myImage, (int)x, (int)y, null);
		}
		else {
			g.drawImage(myImage2, (int)x, (int)y, null);
		}
	}
	
	public static void setWinWidth(int w) {
		winWidth = w;
	}
}
