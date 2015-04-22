package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class SpeedUpItem extends Enemy{
	
	public SpeedUpItem(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRect(x, y, width, height);
	}
}