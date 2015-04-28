package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class ItemImmortal extends Enemy{
	
	public ItemImmortal(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);
	}
}