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
	
	private long score = 0;														//score
	private double difficulty = 0.1;
	private int level = 0;

	// set immortal item step & active
	private int immortal_step = 3;
	private Boolean immortal_active = false;
	private long immortal_duration = 0;											 

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

	private void setEnemyStep(int step){
		for(Enemy e : enemies){
			e.setStep(step);
		}
	}
	
	// Enemy
	private void generateEnemy(){							//add "Enemy" to ArrayList<Enemy> enemies
		Enemy e = new Enemy((int)(Math.random()*390), 30);
		if (immortal_active)								//> check if item immortal it's work to add more step to enemy
			e.setStep(immortal_step);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void generateMinusEnemy(){						//add "MinusEnemy" to ArrayList<Enemy> enemies
		EnemyMinus em = new EnemyMinus((int)(Math.random()*390), 30);
		gp.sprites.add(em);
		enemies.add(em);
	}

	private void generateBigEnemy(){						//add "EnemyBig" to ArrayList<Enemy> enemies
		EnemyBig eb = new EnemyBig((int)(Math.random()*390), 30);
		gp.sprites.add(eb);
		enemies.add(eb);
	}

	// Item
	private void generateItemBonus(){						//add "ItemBonus" to ArrayList<Enemy> enemies
		ItemBonus b = new ItemBonus((int)(Math.random()*390), 30);
		gp.sprites.add(b);
		enemies.add(b);
	}

	private void generateItemLife(){						//add "ItemLife" to ArrayList<Enemy> enemies
		ItemLife l = new ItemLife((int)(Math.random()*390), 30);
		gp.sprites.add(l);
		enemies.add(l);
	}

	private void generateItemImmortal(){					//add "ItemImmortal" to ArrayList<Enemy> enemies
		ItemImmortal im = new ItemImmortal((int)(Math.random()*390), 30);
		gp.sprites.add(im);
		enemies.add(im);
	}

	/*private void generateItemSpeedUp(){						//add "ItemSpeedUp" to ArrayList<Enemy> enemies
		ItemSpeedUp sp = new ItemSpeedUp((int)(Math.random()*390), 30);
		gp.sprites.add(sp);
		enemies.add(sp);
	}*/

	private void process(){

		if(((immortal_duration - System.currentTimeMillis()) < 0) && immortal_active){   //function of Immortal Item
			v.setColorDefault();							//set normal color to SpaceShip
			setEnemyStep(-1*immortal_step);					//set enemy step to normal
			immortal_active = false;
		}

		if(Math.random() < difficulty){							//to create my Item and Enemy
			generateEnemy();
		}

		if(Math.random() < difficulty/10){
			generateItemBonus();
		}
		
		if(Math.random() < difficulty/20){
			generateItemLife();
		}

		if(level==1){											//level 1 >>> Minus Enemy + Time
			if(Math.random() < difficulty/2){
				generateMinusEnemy();
			}
			//timer.setDelay(40);
		}
/*
		//if(level==2){											//level 2 >>> Speed Up Item + Immortal Item
			if(Math.random() < difficulty){
				generateItemSpeedUp();
			}	
		//}		
*/
		if(level==2){
			if(Math.random() < difficulty/2){
				generateItemImmortal();
			}	
		}		

		if(level==3){											//level 3 >>> Big Enemy
			if(Math.random() < difficulty/2){
				generateBigEnemy();
			}	
		}

		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();

			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 10;
				level();
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				if(e instanceof ItemBonus){								//Bonus Item : YELLO
					score+=e.scoreBonus();
					e.goToHell();
				}
				else if(e instanceof ItemLife){							//Life Item : BLUE
					v.addLife();
					e.goToHell();
				}
				else if(e instanceof ItemImmortal){						//Immortal Item : GRAY
					v.setColorCheck();										//Set new color
					e.goToHell();
					if(!immortal_active){
						setEnemyStep(immortal_step);
						immortal_duration = System.currentTimeMillis() + 10000;
						immortal_active = true;
					}
				}
				/*else if(e instanceof ItemSpeedUp){					//Spped Up Item : CYAN
					e.goToHell();
					timer.setDelay(10);
					speedUp();
					gp.updateGameUI(this);
				}*/

				// Enemy
				else if(e instanceof EnemyBig){							//Big Enermy : PINK
					die();
					die();
					e.goToHell();
					if(score > 500)
						score+=e.scoreBonus();
					else if(score <500)
						score = 0;
				}
				
				else if(e instanceof EnemyMinus){						//Minus Enemy : ORANGE
					if(score > 100)
						score+=e.scoreBonus();
					if(score < 100)
						score = 0;
					e.goToHell();
				}
				
				else if(e instanceof Enemy){							//Enemy : RED
					die();
					e.goToHell();
					gp.updateGameUI(this);
				}
				return;
			}
		}
	}

	public void die(){
		if(!immortal_active)									//check it when immortal item on work
			v.spaceShipDie();									//call die method in SpaceShip
		if(v.getLife() < 1){									//check life of SpaceShip and set life of SpaceShip to die
			timer.stop();
		}	
	}

	void controlVehicle(KeyEvent e) {							//Keyboard games control
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

	/*public void speedUp(){									//SpeedUp function
		if(count < 0){
			count--;
		}
		else if(count == 0){
			timer.setDelay(40);
		}
	}*/

	public void level(){										//test level
		if(score > 100){										//level 1 : score > 1000	
			level = 1;
		}
		else if(score > 200){									//level 2 : score > 2000	
			level = 2;
		}
		else if(score > 300){									//level 3 : score > 3000	
			level = 3;
		}
	}

	public long getScore(){										//return score to show on GamePanel
		return score;
	}
	
	public int getLife(){										//return life to show on GamePanel										
		return v.getLife();
	}

	public int getLevel(){										//return level to show on GamePanel
		return level;
	}

	/*public int getSpeedTime(){
		return count;
	}*/

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
