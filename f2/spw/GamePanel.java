package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	private Image gInfo = null;
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	public GamePanel() {
		bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		big.setBackground(Color.BLACK);

		try{
			gInfo = ImageIO.read(new File("./f2/spw/images/spw.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void updateGameUI(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		big.drawImage(gInfo, 400, 0, 200, 600, null);
		big.setColor(Color.WHITE);		
		big.drawString(String.format("Score : %08d", reporter.getScore()), 280, 20);		//show score on game
		big.drawString(String.format("Life : %d", reporter.getLife()), 5, 20);				//show amount life on game
		big.drawString(String.format("Level : %d", reporter.getLevel()), 55, 20);			//show level of your game

		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
