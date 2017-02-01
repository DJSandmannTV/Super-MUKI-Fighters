import processing.core.*;

public class Character {

	PApplet applet;
	PImage[] poses = new PImage[11];

	float xpos, ypos;
	float velocityX = 0, velocityY = 0;
	int pose = 0, rotation = 0;
	String charID;
	boolean charSet;
	
	float old_velocityX;
	int punchPose = 4;

	static float ground;
	boolean grounded = true;

	float health = 100;
	
	float hitTime;
	
	float punchTime;
	int lastPunchPose = 5;
	
	boolean kicked;
	float kickTime;
	
	float blockTime;
	boolean block;
	
	int bounceCount;
	boolean healthDead;
	boolean dead;
	

	Character(int n, PApplet p) {

		this.applet = p;
		
		switch(n) {
		case 1:
	
				this.xpos = applet.width / 2 - applet.height / 2;
				this.ypos = ground;
				this.rotation = 1;
				break;
				
		case 2:
			
				this.xpos = applet.width / 2 + applet.height / 2;
				this.ypos = ground;
				this.rotation = -1;
				break;
				
		}

	}

	void setCharacter(String id) {

		charID = id;
		
		velocityX = 0;
		velocityY = 0;
		pose = 0;

		grounded = true;

		health = 100;
		dead = false;
		healthDead = false;
		bounceCount = 0;
		
		Level.deadTime = 0;
		Level.prefightTime = 0;
		
		if(Level.choosenLevel != 2) {
			ground = applet.height / 8 * 6;
		} else {
			ground = applet.height / 32 * 27;
		}

		switch (charID) {
			
		case "Entoni":
			poses = Game.entoni; break;
		
		case "Fabian":
			poses = Game.fabian; break;
	
		}
			
		charSet = true;

	}
	


	int setPose(int p) {
		
		if(healthDead) {
			return pose = 10;
		}
		
		if(Clock.curTime - blockTime <= 1000) {
			return pose;
		}
		
		if(Clock.curTime - hitTime <= 500) {
			return pose;
		}
		
		if(Clock.curTime - punchTime <= 300) {
			return pose;
		}
		
		if(Clock.curTime - kickTime <= 300) {
			return pose;
		}
		
		return pose = p;

	}

	void drawCharacter() {
		
		xpos += velocityX * Clock.elapTime;
		ypos -= velocityY * Clock.elapTime;

		if (velocityX > 0 && velocityX - old_velocityX <= 0) {
			velocityX -= applet.height * 3 * Clock.elapTime;
			if (velocityX <= 0) {
				velocityX = 0;
			}
		} else if (velocityX < 0 && velocityX - old_velocityX >= 0) {
			velocityX += applet.height * 3 * Clock.elapTime;
			if (velocityX >= 0) {
				velocityX = 0;
			}
		}
		
		old_velocityX = velocityX;
		
		velocityY -= applet.height * 2 * Clock.elapTime;
		
		if (charSet) {
			
			applet.pushMatrix();
			applet.translate(xpos, ypos);
			applet.scale(rotation, 1);
			applet.translate(-xpos, -ypos);
			applet.image(poses[pose], xpos, ypos);
			applet.popMatrix();

		}
		
		rules();

	}
	
	void rotate(String direction) {
		
		switch(direction) {
		case "left":
			rotation = -1; break;
		case "right":
			rotation = 1;
		}
		
	}

	void rules() {
		
		//Ground Boundaries
		if(!healthDead) {
			
			if (ypos >= ground) {
	
				velocityY = 0;
				ypos = ground;
				
				grounded = true;
	
			}
			
		}
		
		else {
			
			//Death
			
			setPose(10);
			
			if (ypos >= ground) {
				
				ypos = ground + 1;
				velocityY = -velocityY / 2;
				bounceCount++;
				
				Sound.playSound(Sound.sounds[9]);
				
				if(bounceCount > 5) {
					velocityY = 0;
					if(Level.deadTime == 0) {
						Level.deadTime = Clock.curTime;
					}
					dead = true;
				}
	
			}
			
		}
		
		//Side boundaries
		if (xpos < applet.width / 2 - applet.height / 2) {
			xpos = applet.width / 2 - applet.height / 2;
			velocityX = 0;
		}
		if (xpos > applet.width / 2 + applet.height / 2) {
			xpos = applet.width / 2 + applet.height / 2;
			velocityX = 0;
			
		}
		
		//No negative health
		if(health <= 0 && !healthDead) {
			health = 0;
			healthDead = true;
			velocityY = applet.height;
		}
		
		//Reset block
		if(Clock.curTime - blockTime > 1000 && block) {
			block = false;
		}
		
		if(velocityX == 0) {
			setPose(0);
		}
		
	}

	void jump() {
		
		if(grounded) {
			velocityY = (float) (applet.height * 1.3);
			ypos--;
			grounded = false;
		}

	}
	
	void moveRight() {
		
		if(rotation > 0) {
			setPose(1);
		} else {
			setPose(2);
		}
			
		if(velocityX < applet.height) {
			velocityX += applet.height * 2 * Clock.elapTime;
		} else {
			velocityX = applet.height;
		}
			
	}
	
	void moveLeft() {
		
		if(rotation > 0) {
			setPose(2);
		} else {
			setPose(1);
		}
		
		if(velocityX > -applet.height) {
			velocityX += -applet.height * 2 * Clock.elapTime;
		} else {
			velocityX = -applet.height;
		}
				
	}
	
	void punch(int player) {
		
		if(Clock.curTime - punchTime > 300) {
			
			if(lastPunchPose == 5) {
				setPose(4); }
			else {
				setPose(5);
			}
			
			lastPunchPose = pose;
			punchTime = Clock.curTime;
			
			if(Level.hitDetection(player, "punch")) {
				
				if(rotation > 0) { velocityX = -applet.width / 2; }
				else { velocityX = applet.height; }
			
			}
			
		}
		
	}
	
	void kick(int player) {
		
		if(Clock.curTime - kickTime > 1000) {
			
			setPose(6);
			
			kickTime = Clock.curTime;
			
			if(Level.hitDetection(player, "kick")) {
				
				if(rotation > 0) { velocityX = -applet.width / 2; }
				else { velocityX = applet.height; }
			
			}
			
		}
		
	}
	
	void hit(float damage) {
		
		if(rotation > 0) { velocityX = -applet.height * (damage / 6); }
		else { velocityX = applet.height * (damage / 6); }
		
		setPose(9);
		
		velocityY += applet.height * (damage / 8);
		
		if(!block) {
			health -= damage;
		}
		
		hitTime = Clock.curTime;
		
	}
	
	void block() {
		
		if(Clock.curTime - blockTime > 2000 && Clock.curTime - hitTime > 250) {
			setPose(3);
			blockTime = Clock.curTime;
			block = true;
		}
		
	}

}