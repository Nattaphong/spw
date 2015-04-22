package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();	

	private SpaceShip v;	
	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.1;
	private int level = 0;

	private int count = 10000;													// 10 s

	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void generateBonusItem(){
		BonusItem b = new BonusItem((int)(Math.random()*390), 30);
		gp.sprites.add(b);
		enemies.add(b);
	}

	private void generateMinusEnemy(){
		EnemyMinus em = new EnemyMinus((int)(Math.random()*390), 30);
		gp.sprites.add(em);
		enemies.add(em);
	}

	private void generateLifeItem(){
		LifeItem l = new LifeItem((int)(Math.random()*390), 30);
		gp.sprites.add(l);
		enemies.add(l);
	}

	private void generateSpeedUpItem(){
		SpeedUpItem sp = new SpeedUpItem((int)(Math.random()*390), 30);
		gp.sprites.add(sp);
		enemies.add(sp);
	}

	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}

		if(Math.random() < difficulty/10){
			generateBonusItem();
		}
		
		if(Math.random() < difficulty/20){
			generateLifeItem();
		}

		if(level==1){
			if(Math.random() < difficulty/2){
				generateMinusEnemy();
			}
			timer.setDelay(40);
		}

		//if(level==2){
			if(Math.random() < difficulty){
				generateSpeedUpItem();
			}	
		//}		

		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();

			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 1;
				level();
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				if(e instanceof SpeedUpItem){
					e.goToHell();
					timer.setDelay(10);
					speedUp();
					gp.updateGameUI(this);
				}
				else if(e instanceof LifeItem){
					v.addLife();
					//die();
					e.goToHell();
				}else if(e instanceof BonusItem){
					//score+=100;
					score+=e.getScored();
					e.goToHell();
				}
				else if(e instanceof Enemy){
					die();
					e.goToHell();
					gp.updateGameUI(this);
				}
				else if(e instanceof EnemyMinus){
					score+=e.getScored();
					e.goToHell();
				}
				return;
			}
		}
	}

	public void die(){
		v.die();
		if(v.getLife() < 1){
			timer.stop();
		}	
	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			v.move(-1);
			break;
		case KeyEvent.VK_RIGHT:
			v.move(1);
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		}
	}

	public void speedUp(){
		if(count < 0){
			count--;
		}
		else if(count == 0){
			timer.setDelay(40);
		}
	}

	public void level(){									//test level
		if(score > 100){
			level = 1;
		}
		else if(score > 200){
			level = 2;
		}
		else if(score > 300){
			level = 3;
		}
	}

	public long getScore(){
		return score;
	}
	
	public int getLife(){
		return v.getLife();
	}

	public int getLevel(){
		return level;
	}

	public int getSpeedTime(){
		if(count == 10000){
			return 10;
		}
		else if(count < 10000 && count > 9000){
			return 9;
		}
		else if(count < 9000 && count > 8000){
			return 8;
		}
		else if(count < 8000 && count > 7000){
			return 7;
		}
		else if(count < 7000 && count > 6000){
			return 6;
		}
		else if(count < 6000 && count > 5000){
			return 5;
		}
		else if(count < 5000 && count > 4000){
			return 4;
		}
		else if(count < 4000 && count > 3000){
			return 3;
		}
		else if(count < 3000 && count > 2000){
			return 2;
		}
		else if(count < 2000 && count > 1000){
			return 1;
		}
		else
			return 0;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
