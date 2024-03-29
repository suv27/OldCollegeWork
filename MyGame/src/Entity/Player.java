package Entity;

import GameState.TileMap;
import TileMap.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

// Player Class, it extends map object class so the player inherits the stuff done in map object 
public class Player extends MapObject
{
	//Player type variables
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	//Fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	//private ArrayList<FireBall> fireBalls
	
	//scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	//Gliding or flying
	private boolean gliding;
	
	//If we want the player to do more stuff
	
	//Animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = 
		{
			2, 8, 1, 2, 4, 2, 5
		};
	
	//Animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;	
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	
	//Constructor
	public Player(TileMap tm)
	{
		super(tm);
		
		//Setting the variables to a specific numbers
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		//Setting movement to a specific and final speed just for this player
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		//We want our player to start facing right from the beginning
		facingRight = true;
		
		
		health = maxHealth = 5;
		fire= maxFire = 2500;
		
		fireCost = 200;
		fireBallDamage = 5;
		//fireBall = new ArrayList<FireBall>();
		
		//scratchRange is how far the fire can reach so now is 40 pixels
		scratchDamage = 8;
		scratchRange = 40;
		
		//Loading the sprite
		try{
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
			
			for(int i = 0; i < 7; i++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				
				sprites = new ArrayList<BufferedImage[]>();
				
				for(int j = 0; j < numFrames[i]; j++)
				{
					if(i != 6)
					{
					   bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					}
					else
					{
					   //Here we are reading the last row in the sprite by two because the width there is 60
					   bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width, height);
					}
				}
				sprites.add(bi);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrame(sprites.get(IDLE));
		animation.setDelay(400);
		
	}
	
	public int getHealth(){ return health; };
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setFiring () { firing = true; }
	public void setScratching() { scratching = true; }
	public void setGliding(boolean b) { gliding = b; }
	
	private void getNextPosition()
	{
		//movement
		if(left)
		{
			dx -= moveSpeed;
			if(dx < -maxSpeed)
			{
				dx = -maxSpeed;
			}
			else if (right)
			{
				dx += moveSpeed;
				if(dx > maxSpeed)
				{
					dx = maxSpeed;
				}
			}
			else 
			{
				if(dx > 0)
				{
					dx -= stopSpeed;
					if(dx < 0)
					{
						dx = 0;
					}
				}else if (dx < 0)
				{
					dx += stopSpeed;
					if(dx > 0)
					{
						dx = 0;
					}
				}
			}
		}
		
		//cannot move while attacking except in anir 
		if((currentAction == SCRATCHING || currentAction == FIREBALL) && (!jumping || falling))
		{
			dx = 0;
		}
		
		//jumping
		if(jumping && !falling)
		{
			dy = jumpStart;
			falling = true;
		}
		
		//falling
		if(falling)
		{
			if(dy < 0 && gliding)
			{
				dy += fallSpeed = 0.1;
			}
			else dy += fallSpeed;
			
			if(dy > 0) jumping = true;
			if(dy < 0 && !jumping) dy += stopJumpSpeed; //the longer you hold the jump buttong the higher yyou jump
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void update()
	{
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//set the animation
		if(scratching)
		{
			if(currentAction != SCRATCHING)
			{
				currentAction = SCRATCHING;
				animation.setFrame(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}

		else if(firing)
		{
			if(currentAction != FIREBALL)
			{
				currentAction = FIREBALL;
				animation.setFrame(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0)
		{
			if(gliding)
			{
				if(currentAction != GLIDING)
				{
					currentAction = GLIDING;
					animation.setFrame(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrame(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
				
			}
		}
		else if(dy < 0)
		{
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrame(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right)
		{
			if(currentAction != WALKING)
			{
				currentAction = WALKING;
				animation.setFrame(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else 
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrame(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
	
		
		animation.update();
		
		//set the direction the player is facing
		if(currentAction != SCRATCHING && currentAction != FIREBALL)
		{
			if(right) facingRight = true;
			if(left) facingLeft = false;
		}
		
	}
	
	
	public void draw(Graphics2D g)
	{
		setMapPosition();
		
		//draw Player
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			
			if(elapsed / 100 % 2 == 0)
				return;
		}
		
		if(facingRight)
		{
			g.drawImage(animation.getImage(),  (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), null);
		}
		
		if(facingLeft)
		{
			g.drawImage(animation.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
		}
		
		
	}
	
}
