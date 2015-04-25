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
	private int level = 0;														//level 

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
	
	private void generateEnemy(){												//add "Enemy" to ArrayList<Enemy> enemies
		Enemy e = new Enemy((int)(Math.random()*390), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void generateBonusItem(){											//add "BonusItem" to ArrayList<Enemy> enemies
		BonusItem b = new BonusItem((int)(Math.random()*390), 30);
		gp.sprites.add(b);
		enemies.add(b);
	}

	private void generateMinusEnemy(){											//add "MinusEnemy" to ArrayList<Enemy> enemies
		EnemyMinus em = new EnemyMinus((int)(Math.random()*390), 30);
		gp.sprites.add(em);
		enemies.add(em);
	}

	private void generateLifeItem(){											//add "LifeItem" to ArrayList<Enemy> enemies
		LifeItem l = new LifeItem((int)(Math.random()*390), 30);
		gp.sprites.add(l);
		enemies.add(l);
	}

	/*private void generateSpeedUpItem(){											//add "SpeedUpItem" to ArrayList<Enemy> enemies
		SpeedUpItem sp = new SpeedUpItem((int)(Math.random()*390), 30);
		gp.sprites.add(sp);
		enemies.add(sp);
	}*/

	private void generateImmortalItem(){										//add "ImmortalItem" to ArrayList<Enemy> enemies
		ImmortalItem im = new ImmortalItem((int)(Math.random()*390), 30);
		gp.sprites.add(im);
		enemies.add(im);
	}

	private void generateEnemyBig(){											//add "EnemyBig" to ArrayList<Enemy> enemies
		EnemyBig eb = new EnemyBig((int)(Math.random()*390), 30);
		gp.sprites.add(eb);
		enemies.add(eb);
	}
	
	private void process(){
		if(Math.random() < difficulty){											//to create may Item and Enemy
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
			//timer.setDelay(40);
		}
/*
		//if(level==2){
			if(Math.random() < difficulty){
				generateSpeedUpItem();
			}	
		//}		
*/
		if(level==2){
			if(Math.random() < difficulty/2){
				generateImmortalItem();
			}	
		}		

		//if(level==3){
			if(Math.random() < difficulty/2){
				generateEnemyBig();
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
				if(e instanceof EnemyBig){
					die2();
					if(score > 500)
						score+=e.getScored();
					else if(score <500)
						score = 0;
					e.goToHell();
				}

				if(e instanceof ImmortalItem){										//check ImmortalItem
					
				}
				/*if(e instanceof SpeedUpItem){										//check SppedUpItem
					e.goToHell();
					timer.setDelay(10);
					speedUp();
					gp.updateGameUI(this);
				}*/
				else if(e instanceof LifeItem){										//check Life Item
					v.addLife();
					//die();
					e.goToHell();
				}else if(e instanceof BonusItem){									//check BonusItem
					//score+=100;
					score+=e.getScored();
					e.goToHell();
				}
				else if(e instanceof Enemy){										//check Enemy
					die();
					e.goToHell();
					gp.updateGameUI(this);
				}
				else if(e instanceof EnemyMinus){									//check EnemyMinus
					if(score > 100)
						score+=e.getScored();
					if(score < 100)
						score = 0;
					e.goToHell();
				}
				return;
			}
		}
	}

	public void die(){																//die method
		v.die(1);																	//call die method in SpaceShip
		if(v.getLife() < 1){														//check life of SpaceShip and set life of SpaceShip to die
			timer.stop();
		}	
	}
	
	public void die2(){																//die2 method for EnemyBig
		v.die(2);																	
		if(v.getLife() < 1){														
			timer.stop();
		}	
	}

	void controlVehicle(KeyEvent e) {												//Keyboard games control
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

	/*public void speedUp(){															//SpeedUp function
		if(count < 0){
			count--;
		}
		else if(count == 0){
			timer.setDelay(40);
		}
	}*/

	public void level(){															//test level
		if(score > 100){															//level 1 : score > 1000	
			level = 1;
		}
		else if(score > 200){														//level 2 : score > 2000	
			level = 2;
		}
		else if(score > 300){														//level 3 : score > 3000	
			level = 3;
		}
	}

	public long getScore(){															//return score to show on GamePanel
		return score;
	}
	
	public int getLife(){
		return v.getLife();
	}

	public int getLevel(){
		return level;
	}

	public int getSpeedTime(){
		return count;
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
