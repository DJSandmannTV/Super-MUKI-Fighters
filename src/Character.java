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
	int punchPose = 10;

	static float ground;
	boolean grounded = true;

	float health = 100;
	
	float hitTime;
	
	float punchTime;
	int lastPunchPose = 10;
	
	boolean kicked;
	float kickTime;
	
	boolean shot;
	boolean shotTime;
	

	Character(int n, PApplet p) {

		this.applet = p;
		ground = applet.height / 8 * 7;
		
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

		this.charID = id;

		switch (charID) {
			
		case "Entoni":
			poses = Game.entoni; break;
		
		case "Fabian":
			poses = Game.fabian; break;
	
		}
			
		charSet = true;

	}
	


	int setPose(int p) {
		
		if(Clock.curTime - punchTime <= 200) {
			return pose;
		}
		
		if(Clock.curTime - kickTime <= 500) {
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
		if (ypos >= ground) {

			velocityY = 0;
			ypos = ground;
			
			setPose(4);
			grounded = true;

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
		
		//Reset pose when flying
		if(!grounded) {
			if(velocityX > -applet.height / 10 && velocityX < applet.height / 10) {
				setPose(3);
			}
		}
		
		//No negative health
		if(health < 0) { health = 0; }
		
	}

	void jump() {
		
		if(grounded) {
			setPose(3);
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
		
		if(Clock.curTime - punchTime > 200) {
			
			if(lastPunchPose == 10) {
				setPose(10); }
			else {
				setPose(11);
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
			
			setPose(12);
			
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
		
		velocityY += applet.height * (damage / 8);
		
		health -= damage;
		
		hitTime = Clock.curTime;
		
	}

}
