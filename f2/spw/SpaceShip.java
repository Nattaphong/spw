package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public class SpaceShip extends Sprite{

	int step = 8;
	private int life = 5;
	
	private boolean colorCheck = true;

	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(colorCheck){
			g.setColor(Color.GREEN);
		}
		else if(!colorCheck){
			g.setColor(Color.GRAY);
		}
		g.fillRect(x, y, width, height);
		
	}

	public void move(int direction){
		x += (step * direction);
		if(x < 0)
			x = 0;
		if(x > 400 - width)
			x = 400 - width;
	}

	public void spaceShipDie(){
		life--;
	}

	public int getLife(){
		return life;
	}

	public void addLife(){
		if(life < 10){
			life++;
		}
	}

	public void setColorCheck(){
		if(colorCheck){
			colorCheck = false;
		}
	}

	public void setColorDefault(){
		if(!colorCheck){
			colorCheck = true;
		}
	}
}
