package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class ItemBonus extends Enemy{
	
	public ItemBonus(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillRect(x, y, width, height);
	}

	public int scoreBonus(){
		return 100;
	}
}