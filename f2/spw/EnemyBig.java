package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class EnemyBig extends Enemy{
	
	public EnemyBig(int x, int y) {
		super(x, y, 20, 20);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.fillRect(x, y, width, height);
	}

	public int getScored(){
		return -500;
	}
}