import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

	private static final Dimension WindowSize = new Dimension(600,600);
	private static boolean isGraphicsInitialised = false;
	private static final int NUMALIENS = 30;
	private Alien[] AliensArray = new Alien[NUMALIENS];
	private Spaceship PlayerShip;
	private Image invaderImg;
	private Image invaderImg2;
	private Image playerImg;
	private static String workingDirectory;
	private BufferStrategy strategy;
	private Image bulletImage;
	private ArrayList bulletList = new ArrayList();
	//private PlayerBullet b;
	private static int score;
	private static boolean isGameInProgress = false;
	private static boolean gameOver = false;
	private static int yourScore;

public InvadersApplication() {
	
	this.setTitle("Brian Meaney 16315941");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//Display the window, centered on the screen
	Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int x = screensize.width/2 - WindowSize.width/2;
	int y = screensize.height/2 - WindowSize.height/2; 
	setBounds(x, y, WindowSize.width, WindowSize.height);
	setVisible(true);
	
	ImageIcon icon = new ImageIcon(workingDirectory + "//alien_ship_1.png");
	invaderImg = icon.getImage();
	ImageIcon icon3 = new ImageIcon(workingDirectory + "//alien_ship_2.png");
	invaderImg2 = icon3.getImage();
	
	
	
	for(int i=0; i<NUMALIENS; i++) {
		
		AliensArray[i] = new Alien(invaderImg, invaderImg2); //creates alien object
		double xx = (i%5)*80 + 70;
		double yy = (i/5)*40 + 50;
		AliensArray[i].setPosition(xx, yy);
			
		}
	Alien.setFleetXSpeed(2);
		
	
	
	
	ImageIcon icon2 = new ImageIcon(workingDirectory + "//SpacePigeon.png");
	playerImg= icon2.getImage();
	PlayerShip = new Spaceship(playerImg, playerImg);
	PlayerShip.setPosition(300,530);
	
	
	Sprite2D.setWinWidth(WindowSize.width);
	
	Thread t = new Thread(this);
	t.start();
	
	addKeyListener(this);
	
	createBufferStrategy(2);
	strategy = getBufferStrategy();
	
	isGraphicsInitialised = true;
	
}

public void run() {
	
	while(true) {
		try {
			
				Thread.sleep(10);
				
			}
			
            catch (InterruptedException e) {
            
        }

		boolean alienDirectionReversalNeeded = false;
		for(int i = 0; i<NUMALIENS; i++) {
			
			if(AliensArray[i].move())
				alienDirectionReversalNeeded = true;
			
			
		}
		if (alienDirectionReversalNeeded) {
			Alien.reverseDirection();
			for(int i=0; i<NUMALIENS; i++)
				AliensArray[i].jumpDownwards();
		}
		
		
		
		
		Iterator iterator = bulletList.iterator();
		
		while(iterator.hasNext()) {
			PlayerBullet b = (PlayerBullet) iterator.next();
			
			for(int i = 0; i<NUMALIENS; i++) {
				
				double x1 = AliensArray[i].x;
				double y1 = AliensArray[i].y;
				double w1 = 45;
				double h1 = 28;
				
				double x2 = b.x;
				double y2 = b.y;
				double w2 = 6;
				double h2 = 16;
				
					
					if(((x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)) && ((y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1))) {
					if(AliensArray[i].isAlive) {
						AliensArray[i].isAlive= false;
					iterator.remove();
					
					score++;
					
					}
		
					
				}
			}
			b.move();
			
			
		}
		
		PlayerShip.move();
		this.repaint(); 
		}
	
}

@Override
public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()){
        case KeyEvent.VK_RIGHT :
            PlayerShip.setXSpeed(4);
            break;
        case KeyEvent.VK_LEFT :
        	PlayerShip.setXSpeed(-4);
            break;
        case KeyEvent.VK_SPACE :
        	shootBullet();
        	break;
        case KeyEvent.VK_UP : 
        	isGameInProgress = true;
        case KeyEvent.VK_BACK_SPACE :
        	startNewGame();
        

    }
	
}

@Override
public void keyReleased(KeyEvent e) {
	
	if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT) {
		PlayerShip.setXSpeed(0);
	}
}

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

public void shootBullet() {
	
	ImageIcon iconB = new ImageIcon(workingDirectory + "//bullet.png");
	bulletImage = iconB.getImage();
	
	PlayerBullet b = new PlayerBullet(bulletImage, bulletImage);
	b.setPosition(PlayerShip.x+54/2, PlayerShip.y);
	bulletList.add(b);
	//b.move();
}

public void paint(Graphics g) {
	if(!isGameInProgress) {
		g.fillRect(0, 0, 600, 600);
		g.setColor(Color.BLACK);
		g.setColor(Color.WHITE);
		g.drawString("START NEW GAME", 250, 300);
		g.drawString("PRESS UP ARROW KEY", 250, 400);
	}else
	
	if(isGraphicsInitialised) {
	g=strategy.getDrawGraphics();
	
	g.fillRect(0, 0, 600, 600);
	g.setColor(Color.BLACK);
	for(int i = 0; i<NUMALIENS; i++) {
		
		if(AliensArray[i].isAlive) {
		AliensArray[i].paint(g); //paints aliens
		}
		if(AliensArray[i].y>500) {
			
			g.setColor(Color.BLACK);
			g.setColor(Color.WHITE);
			g.drawString("GAME OVER", 250, 300);
			PlayerShip.setXSpeed(0);
			g.drawString("PRESS BACKSPACE TO START AGAIN", 200, 350);
			
		}
		
	}
	PlayerShip.paint(g); //paints players
	
	Iterator iterator = bulletList.iterator();
	while(iterator.hasNext()) {
		PlayerBullet b = (PlayerBullet) iterator.next();
		b.paint(g);
	}
	
	g.setColor(Color.WHITE);
	g.drawString("Score: " +score, 300,50 );
	
	if(score >=30 && score%30 == 0) {
		
		g.setColor(Color.WHITE);
		g.drawString("YOU WON", 300, 300);
		startNewGame();
		
	}
	
	
	
	g.dispose();
	strategy.show();
}
}

public void startNewGame() {
	int j = 2;
for(int i=0; i<NUMALIENS; i++) {
		
		AliensArray[i] = new Alien(invaderImg, invaderImg2); //creates alien object
		double xx = (i%5)*80 + 70;
		double yy = (i/5)*40 + 50;
		AliensArray[i].setPosition(xx, yy);
		}
	j++;
	Alien.setFleetXSpeed(j);
}

public static void main(String[] args) {
	workingDirectory = System.getProperty("user.dir");
	InvadersApplication a = new InvadersApplication();
}


}
