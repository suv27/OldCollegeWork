package main;

import java.awt.Color;
import java.awt.Graphics;

//Heads Up Display Class
public class HUD 
{
	public  static float HEALTH = 100;
	private float greenValue = 255;
	private int score = 0;
	private int level = 1;
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void tick()
	{
		HEALTH = Game.clamp(HEALTH, 0, 100);
		
		greenValue = (int) Game.clamp(greenValue, 0, 255);
		greenValue = HEALTH * 2;
		score++;
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.gray);
		g.fillRect(15, 15, 200, 32);
		g.setColor(new Color(100, (int) greenValue, 0));
		g.fillRect(15, 15, (int) (HEALTH * 2), 32);
		g.setColor(Color.white);
		g.drawRect(15, 15, 200, 32);
		
		g.drawString("Score: " + score, 15, 64);
		g.drawString("Level: " + level, 15, 80);
		
	}

}
