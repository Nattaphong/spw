package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class EnemyMinus extends Enemy{
	
	public EnemyMinus(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, width, height);
	}

	public int scoreBonus(){
		return -100;
	}
}