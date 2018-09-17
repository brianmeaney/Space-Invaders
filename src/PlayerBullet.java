import java.awt.Image;

public class PlayerBullet extends Sprite2D {

	
	public PlayerBullet(Image i, Image i2) {
		super(i, i2);
	}
	
	public boolean move() {
		y-=4;
		return (y<0);
		
		
		
	}

}
