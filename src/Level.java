import ch.aplu.xboxcontroller.XboxController;
import processing.core.*;

public class Level {
	
	static String level = "level_selection";
	
	static Character player1;
	static Character player2;
	static PApplet applet;
	
	static float
					startupTimer = -1,
					creditsTimer = -1;
	
	static float
					healthbar1,
					healthbar2;
	
	static boolean first = true;
	
	static Button
			BStart,
			BCredits,
			BEnd;
	
	static Button
			BCharacter1,
			BCharacter2,
			BCharacter3,
			BCharacter4,
			BCharacter5,
			BFight,
			BBack;
	
	static Button
	 		BStage1,
	 		BStage2,
	 		BStage3;
	
	static int
				menuPosition,
				menuPositionMax;
	
	static String lastLevel = "";
	
	static boolean skipImpulse = false;
	
	static boolean transitionRunning = false;
	static float transitionTime;
	static int transitionColumn = 0;

	static float prefightTime = 0;
	static float tempXpos1, tempXpos2, VSTextPos;

	static int choosenLevel;
	
	static float 	vibTime1,
					vibTime2;
	static int	vibStrenght1,
				vibStrenght2;
	static int	vibDuration1,
				vibDuration2;

	
	static void setupLevels(PApplet p) {
		applet = p;
		player1 = new Character(1, p);
		player2 = new Character(2, p);
		
		
		BStart = new Button(
				applet,
				applet.width / 2,
				applet.height / 2 + applet.height / 8,
				applet.height / 3, applet.height / 10,
				1,
				0, 0, 255,
				"Start");

		BEnd = new Button(
				applet,
				applet.width / 2,
				applet.height / 2 + applet.height / 4,
				applet.height / 3, applet.height / 10,
				2,
				0, 0, 255,
				"Ende");

		BCredits = new Button(
				applet,
				applet.width / 8 * 7,
				applet.height - applet.height / 16,
				applet.height / 5, applet.height / 10,
				3,
				0, 255, 255,
				"Credits");

		
		BCharacter1 = new Button(
				applet,
				applet.width / 2 - applet.width / 8,
				applet.height / 2 + applet.height / 16,
				100, 100,
				1,
				255, 255, 255);

		BCharacter2 = new Button(
				applet,
				applet.width / 2,
				applet.height / 2 + applet.height / 16,
				100, 100,
				2,
				0, 0, 0);

		BCharacter3 = new Button(
				applet,
				applet.width / 2 + applet.width / 8,
				applet.height / 2 + applet.height / 16,
				100, 100,
				3,
				255, 255, 255);

		BCharacter4 = new Button(
				applet,
				applet.width / 2 - applet.width / 16,
				applet.height / 2 + applet.height / 4,
				100, 100,
				4,
				0, 0, 0);

		BCharacter5 = new Button(
				applet,
				applet.width / 2 + applet.width / 16,
				applet.height / 2 + applet.height / 4,
				100, 100,
				5,
				255, 255, 255);

		BFight = new Button(
				applet,
				applet.width / 8 * 7,
				applet.height - applet.height / 16,
				applet.height / 5, applet.height / 10,
				3,
				0, 255, 255,
				"Fight");

		BBack = new Button(
				applet,
				applet.width / 8,
				applet.height - applet.height / 16,
				150, 50,
				6,
				255, 0, 0,
				"Zurueck");
		
		
		BStage1 = new Button(
				applet,
				applet.width / 2 - applet.height / 4,
				applet.height / 2,
				Game.background[0].width / (applet.height / 200f),
				Game.background[0].height / (applet.height / 200f),
				1,
				Game.background[0]);
		BStage2 = new Button(
				applet,
				applet.width / 2 + applet.height / 4,
				applet.height / 2,
				Game.background[1].width / (applet.height / 200f),
				Game.background[1].height / (applet.height / 200f),
				2,
				Game.background[1]);
		BStage3 = new Button(
				applet,
				applet.width / 2,
				applet.height / 4 * 3,
				Game.background[2].width / (applet.height / 200f),
				Game.background[2].height / (applet.height / 200f),
				3,
				Game.background[2]);
		
		tempXpos1 = - applet.width;
		tempXpos2 = applet.width * 2;
		VSTextPos = - applet.height;
	}
	
	static void setLevel(String new_level) {
		level = new_level;
	}
	
	static void drawLevel() {
		
		if(level == "startup") {
			applet.noCursor();
			applet.background(0);
			if(Game.loaded) { drawUI("startup"); }
			Sound.playSound(Sound.sounds[0]);
			
			if(skip()) { level = "press_start"; Sound.stopSound(Sound.sounds[0]);}
		}
		
		if(level == "press_start") {
			applet.cursor();
			applet.background(0);
			drawUI("press_start");
			Sound.controlMusic(Sound.music[0], "play");
			
			if(skip()) { level = "main_menu"; }
		}
		
		if(level == "credits") {
			applet.background(0);
			drawUI("credits");
			if(skip()) { level = "main_menu"; }
		}
		
		if(level == "main_menu") {
			applet.background(0);
			drawUI("UI.main_menu");
//			controlMenu(3);
		}
		
		if(level == "character_selection") {
			applet.background(0);
			drawUI("UI.character_selection");
//			controlMenu(6);
		}
		
		if(level == "level_selection") {
			applet.background(0);
			drawUI("UI.level_selection");
		}
		
		if(level == "fight") {
			applet.noCursor();
			
			applet.imageMode(PConstants.CENTER);
			applet.image(
							Game.background[choosenLevel],
							applet.width / 2,
							applet.height / 2);
			
		
			
			if(prefightTime == 0) {
				
				prefightTime = Clock.curTime;
				
			}
			
			if(Clock.curTime - prefightTime < 7500) {
				drawUI("UI.pre_fight");
			} else {
				controlCharacters();
				vibrate();
				player1.drawCharacter();
				player2.drawCharacter();
				drawUI("UI.fight");
			}
			
		}
		
	}
	
	static void drawUI(String UIID) {
		
		if(UIID == "startup") {
			
			if(first) {
				first = false;
			}
			else {
				startupTimer += Clock.elapTime;
				
				applet.textSize(applet.height / 35);
				applet.fill(255);
				applet.textAlign(PConstants.CENTER, PConstants.CENTER);
				applet.imageMode(PConstants.CENTER);
				
				if(startupTimer >= 0) {
					if(startupTimer < 3) {
						applet.image(Game.logos[1], applet.width / 2, applet.height / 2);
					}
					else if(startupTimer < 6) {
						applet.text("Powered by",applet.width / 2, applet.height / 4);
						applet.image(Game.logos[2], applet.width / 2, applet.height / 2);
						applet.text("Processing",applet.width / 2, applet.height / 4 * 3);
					}
					else if(startupTimer < 9) {
						applet.text("Made with love by nerds", 0, 0, applet.width, applet.height);
					}
					else {
						level = "press_start";
					}
				}
			}
		}
		
		if(UIID == "press_start") {

			applet.pushMatrix();
			applet.translate(applet.width / 2, applet.height / 4);
			applet.scale(PApplet.map((float) Math.sin(Clock.prevTime / 1000), -1f, 1f, 0.9f, 1.1f));
			applet.translate(-(applet.width / 2), -(applet.height / 4));
			applet.imageMode(PConstants.CENTER);
			applet.image(Game.logos[0], applet.width / 2, applet.height / 4);
			applet.popMatrix();
			
			if((int) (Clock.curTime / 1000) % 2 == 0) {
				applet.textAlign(PConstants.CENTER);
				applet.textSize(30);
				applet.fill(255);
				applet.text("Press Start", applet.width / 2, applet.height / 4 * 3);
			}
			
		}
		
		if(UIID == "credits") {
			creditsTimer += Clock.elapTime;
			
			applet.textSize(30);
			applet.fill(255);
			applet.textAlign(PConstants.CENTER, PConstants.CENTER);
			
			if(creditsTimer >= 0) {
				if(creditsTimer < 3) {
					applet.text("Super MUKI Fighters\n\nA beat 'em up game project made for university", 0, 0, applet.width, applet.height);
				}
				else if(creditsTimer < 6) {
					applet.text("Programming\n\nFabian Fritzsche\nDustin Koschmann", 0, 0, applet.width, applet.height);
				}
				else if(creditsTimer < 9) {
					applet.text("Character Design\n\nEntoni [Nachname]", 0, 0, applet.width, applet.height);
				}
				else if(creditsTimer < 12) {
					applet.text("Level- & Logodesign\n\nSemi Kefeli", 0, 0, applet.width, applet.height);
				}
				else if(creditsTimer < 15) {
					applet.text("Mac User\n\nPaolo Bonossi", 0, 0, applet.width, applet.height);
				}
				else {
					creditsTimer = -1;
					level = "main_menu";
				}
			
			}
			
		}
		
		if(UIID == "UI.main_menu") {
			
			applet.textSize(50);
			applet.fill(0);
			applet.textAlign(PConstants.CENTER, PConstants.CENTER);
			
			applet.pushMatrix();
			applet.imageMode(PConstants.CENTER);
			applet.translate(applet.width / 2, applet.height / 4);
			applet.scale(PApplet.map((float) Math.sin(Clock.prevTime / 1000), -1f, 1f, 0.9f, 1.1f));
			applet.translate(-(applet.width / 2), -(applet.height / 4));
			
			applet.image(Game.logos[0], applet.width / 2, applet.height / 4);
			
			applet.popMatrix();
			
			BStart.show();
			BCredits.show();
			BEnd.show();
			
			if(BStart.clicked()) {
				level = "level_selection";
			}
			
			if(BCredits.clicked()) {
				level = "credits";
			}
			
			if(BEnd.clicked()) {
				Sound.stopSoundEngine();
				applet.exit();
				System.exit(1);
			}
			
		}
		
		if(UIID == "UI.character_selection") {
			applet.textSize(50);
			applet.fill(0);
			applet.textLeading(70);
			applet.textAlign(PConstants.CENTER);
			applet.text("Choose your\ncharacter", applet.width / 2, applet.height / 8);
			
			BCharacter1.show();
			BCharacter2.show();
			BCharacter3.show();
			BCharacter4.show();
			BCharacter5.show();
			BFight.show();
			BBack.show();
			
			if(BCharacter1.clicked()) {
				if(player1.charID == null) 				{ player1.setCharacter("Entoni"); 	BCharacter1.toggle(true); }
				else if(player2.charID == null) 		{ player2.setCharacter("Entoni"); 	BCharacter1.toggle(true); }
				else if(player1.charID == "Entoni") 	{ player1.setCharacter(""); 		BCharacter1.toggle(false); }
				else if(player2.charID == "Entoni") 	{ player2.setCharacter(""); 		BCharacter1.toggle(false); }
			}
			if(BCharacter2.clicked()) {
				if(player1.charID == null) 				{ player1.setCharacter("Entoni2"); 	BCharacter1.toggle(true); }
				else if(player2.charID == null) 		{ player2.setCharacter("Entoni2"); 	BCharacter1.toggle(true); }
				else if(player1.charID == "Entoni2") 	{ player1.setCharacter(""); 		BCharacter1.toggle(false); }
				else if(player2.charID == "Entoni2") 	{ player2.setCharacter(""); 		BCharacter1.toggle(false); }
			}
//			if(BCharacter3.clicked()) {
//				BCharacter3.toggle();
//			}
//			if(BCharacter4.clicked()) {
//				BCharacter4.toggle();
//			}
//			if(BCharacter5.clicked()) {
//				BCharacter5.toggle();
//			}
			
			if(BBack.clicked()) {
				level = "main_menu";
			}
			
		}
		
		if(UIID == "UI.level_selection") {
			
			applet.textSize(50);
			applet.fill(255);
			applet.textLeading(70);
			applet.textAlign(PConstants.CENTER);
			applet.text("Choose a\nstage", applet.width / 2, applet.height / 8);
			
			applet.imageMode(PConstants.CENTER);
			
			BStage1.show();
			BStage2.show();
			BStage3.show();
			
			BFight.show();
			
			if(BStage1.clicked()) {
				
				healthbar1 = 1;
				healthbar2 = 1;
				choosenLevel = 0;
				level = "fight";

				player1.setCharacter("Entoni");;
				player2.setCharacter("Entoni");;
					
				
			}
			if(BStage2.clicked()) {
				
				healthbar1 = 1;
				healthbar2 = 1;
				choosenLevel = 1;
				level = "fight";
				
				player1.setCharacter("Entoni");
				player2.setCharacter("Entoni");
			
			}
			if(BStage3.clicked()) {
				
				healthbar1 = 1;
				healthbar2 = 1;
				choosenLevel = 2;
				level = "fight";
				
				player1.setCharacter("Entoni");
				player2.setCharacter("Entoni");
			
			}
			
		}
		
		if(UIID == "UI.pre_fight") {
			
			if(Clock.curTime - prefightTime < 3000) {
				tempXpos1 = tempXpos1 + (applet.width / 4 - tempXpos1) * 2f * Clock.elapTime;
				tempXpos2 = tempXpos2 + (applet.width / 4 * 3 - tempXpos2) * 2f * Clock.elapTime;
				VSTextPos = VSTextPos + (applet.height / 2 - VSTextPos) * 2f * Clock.elapTime;
			} else if(Clock.curTime - prefightTime < 4000) {
				tempXpos1 = tempXpos1 + (-applet.width - tempXpos1) * 2f * Clock.elapTime;
				tempXpos2 = tempXpos2 + (applet.width * 2 - tempXpos2) * 2f * Clock.elapTime;
				VSTextPos = VSTextPos + (-applet.height - VSTextPos) * 2f * Clock.elapTime;
			} else if(Clock.curTime - prefightTime < 4500) {
				applet.textSize(applet.height / 4);
				applet.textAlign(PConstants.CENTER, PConstants.CENTER);
				applet.fill(255, 0, 0);
				applet.text("3", applet.width / 2, applet.height / 2);
			} else if(Clock.curTime - prefightTime > 5000 && Clock.curTime - prefightTime < 5500) {
				applet.textSize(applet.height / 4);
				applet.textAlign(PConstants.CENTER, PConstants.CENTER);
				applet.fill(255, 0, 0);
				applet.text("2", applet.width / 2, applet.height / 2);
			} else if(Clock.curTime - prefightTime > 6000 && Clock.curTime - prefightTime < 6500) {
				applet.textSize(applet.height / 4);
				applet.textAlign(PConstants.CENTER, PConstants.CENTER);
				applet.fill(255, 0, 0);
				applet.text("1", applet.width / 2, applet.height / 2);
			} else if(Clock.curTime - prefightTime > 7000 && Clock.curTime - prefightTime < 7500) {
				applet.textSize(applet.height / 4);
				applet.textAlign(PConstants.CENTER, PConstants.CENTER);
				applet.fill(255, 0, 0);
				applet.text("Start", applet.width / 2, applet.height / 2);
			}
			
			applet.imageMode(PConstants.CENTER);
			
			applet.image(player1.poses[0], tempXpos1, applet.height / 2, player1.poses[0].width * 5, player1.poses[0].height * 5);
			
			applet.pushMatrix();
			applet.translate(tempXpos2, applet.height / 2);
			applet.scale(-1, 1);
			applet.translate(-tempXpos2, -(applet.height / 2));
			applet.image(player2.poses[0], tempXpos2, applet.height / 2, player2.poses[0].width * 5, player2.poses[0].height * 5);
			applet.popMatrix();
			
			applet.textSize(applet.height / 4);
			applet.textAlign(PConstants.CENTER, PConstants.CENTER);
			applet.fill(255, 0, 0);
			applet.text("VS", applet.width / 2, VSTextPos);
			
		}
		
		if(UIID == "UI.fight") {
			healthBars();
		}
		
	}
	
	static void healthBars() {
		
		if(healthbar1 > player1.health / 100 && Clock.curTime - player1.hitTime > 1000) {
			healthbar1 -= Clock.elapTime / 4; 
		}
		else if(healthbar1 < player1.health / 100) {
			healthbar1 = player1.health / 100;
		}
		
		if(healthbar2 > player2.health / 100 && Clock.curTime - player2.hitTime > 1000) {
			healthbar2 -= Clock.elapTime / 4; 
		}
		else if(healthbar2 < player2.health / 100) {
			healthbar2 = player2.health / 100;
		}
		
		applet.rectMode(PConstants.CORNERS);
		applet.textSize(applet.height / 32);
				
		applet.shape(Game.PShealthbar1_bg);
		
		applet.fill(255, 0, 0);
		applet.rect(
					applet.height / 16,
					applet.height / 32 * 2,
					applet.height / 16 + (applet.width / 16 * 6 - applet.height / 16) * healthbar1,
					applet.height / 32 * 3);
		applet.fill(0, 255, 0);
		applet.rect(
					applet.height / 16,
					applet.height / 32 * 2,
					applet.height / 16 + (applet.width / 16 * 6 - applet.height / 16) * (player1.health / 100),
					applet.height / 32 * 3);
		applet.fill(0);
		applet.triangle(
						applet.width / 16 * 6,
						applet.height / 32 * 2,
						applet.width / 16 * 6,
						applet.height / 32 * 3,
						applet.width / 32 * 11,
						applet.height / 32 * 3);

		applet.textAlign(PConstants.LEFT);
		applet.fill(255);
		applet.text(player1.charID, applet.height / 32 * 2, applet.height / 64 * 9);
		
		applet.shape(Game.PShealthbar2_bg);
		
		applet.fill(255, 0, 0);
		applet.rect(
					applet.width - applet.height / 16,
					applet.height / 32 * 2,
					applet.width - applet.height / 16 - (applet.width / 16 * 6 - applet.height / 16) * healthbar2,
					applet.height / 32 * 3);
		applet.fill(0, 255, 0);
		applet.rect(
					applet.width - applet.height / 16,
					applet.height / 32 * 2,
					applet.width - applet.height / 16 - (applet.width / 16 * 6 - applet.height / 16) * (player2.health / 100),
					applet.height / 32 * 3);
		applet.fill(0);
		applet.triangle(
						applet.width - applet.width / 16 * 6,
						applet.height / 32 * 2,
						applet.width - applet.width / 16 * 6,
						applet.height / 32 * 3,
						applet.width - applet.width / 32 * 11,
						applet.height / 32 * 3);

		applet.textAlign(PConstants.RIGHT);
		applet.fill(255);
		applet.text(player2.charID, applet.width - applet.height / 32 * 2, applet.height / 64 * 9);
		
	}
	
	static void controlCharacters() {
			
		if(Controls.W || Controls.c1A) {					player1.jump(); }
		if(Controls.A || Controls.c1leftThumbLeft) { 		player1.moveLeft(); }
		if(Controls.D || Controls.c1leftThumbRight) { 		player1.moveRight(); }
		if(Controls.c1B) {									player1.punch(1); }
		if(Controls.c1X) {									player1.kick(1); }
		
		if(Controls.W || Controls.c2A) {					player2.jump(); }
		if(Controls.A || Controls.c2leftThumbLeft) { 		player2.moveLeft(); }
		if(Controls.D || Controls.c2leftThumbRight) { 		player2.moveRight(); }
		if(Controls.c2B) {									player2.punch(2); }
		if(Controls.c2X) {									player2.kick(2); }
		
		if(player1.xpos < player2.xpos) {
			player1.rotation = 1;
			player2.rotation = -1;
		} else {
			player1.rotation = -1;
			player2.rotation = 1;
			
		}
		
	}
	
//	static void controlMenu(int max) {
//		
//		if(level != lastLevel) {
//			
//			menuPosition = 0;
//			menuPositionMax = max;
//			lastLevel = level;
//			
//		}
//		
//		if(!controlMenuImpulse) {
//		
//			if(Controls.Up || Controls.c1leftThumbUp || (Controls.c1DpadPressed && Controls.c1DpadDirection == 0)) {
//				if(menuPosition > 0) { setMenuPos('-'); }
//			}
//			if(Controls.Down || Controls.c1leftThumbDown || (Controls.c1DpadPressed && Controls.c1DpadDirection == 4)) {
//				if(menuPosition < menuPositionMax) { setMenuPos('+'); }
//			}
//			if(Controls.Left || Controls.c1leftThumbLeft || (Controls.c1DpadPressed && Controls.c1DpadDirection == 6)) {
//				if(menuPosition > 0) { setMenuPos('-'); }
//			}
//			if(Controls.Right || Controls.c1leftThumbRight || (Controls.c1DpadPressed && Controls.c1DpadDirection == 2)) {
//				if(menuPosition < menuPositionMax) { setMenuPos('+'); }
//			}
//			
//			controlMenuImpulse = true;
//			
//		} else if(
//				!Controls.Up &&
//				!Controls.Down &&
//				!Controls.Left &&
//				!Controls.Right &&
//				!Controls.c1leftThumbUp &&
//				!Controls.c1leftThumbDown &&
//				!Controls.c1leftThumbLeft &&
//				!Controls.c1leftThumbRight &&
//				!(Controls.c1DpadPressed && Controls.c1DpadDirection == 0) &&
//				!(Controls.c1DpadPressed && Controls.c1DpadDirection == 4) &&
//				!(Controls.c1DpadPressed && Controls.c1DpadDirection == 6) &&
//				!(Controls.c1DpadPressed && Controls.c1DpadDirection == 2)) {
//			controlMenuImpulse = false;
//		}
//		
//	}
	
	static void setMenuPos(char dir) {
		
		switch(dir) {
		
		case '+': menuPosition++; break;
		case '-': menuPosition--; break;
		default: return;
		
		}
		
	}
	
	static boolean hitDetection(int player, String action) {
		
		boolean result = false;
		
		if(PApplet.dist(player1.xpos, player1.ypos, player2.xpos, player2.ypos) <
				player1.poses[0].width / 1.5) {
		
			switch(player) {
			
			case 1:
				
				if(action == "punch") {
					float damage = applet.random(2, 5);
					player2.hit(damage);
					setVibrate(Controls.gamepad2, damage);
				}
				if(action == "kick") {
					float damage = applet.random(5, 10);
					player2.hit(damage);
					setVibrate(Controls.gamepad2, damage);
				}
				result = true;
				break;
				
			case 2:
				
				if(action == "punch") {
					player1.hit(applet.random(2, 5));
					float damage = applet.random(2, 5);
					player1.hit(damage);
					setVibrate(Controls.gamepad1, damage);
				}
				if(action == "kick") {
					player1.hit(applet.random(5, 10));
					float damage = applet.random(2, 5);
					player1.hit(damage);
					setVibrate(Controls.gamepad1, damage);
				}
				result = true;
				break;
				
			}
			
		}
		
		return result;
		
	}
	
	static boolean skip() {
		
		if(	(applet.keyPressed ||
			applet.mousePressed ||
			Controls.c1A ||
			Controls.c1B ||
			Controls.c1X ||
			Controls.c1Y ||
			Controls.c1Start ||
			Controls.c1Select) && !skipImpulse)
		{
			skipImpulse = true;
			return true;
			
		} else if(	(applet.keyPressed ||
					applet.mousePressed ||
					Controls.c1A ||
					Controls.c1B ||
					Controls.c1X ||
					Controls.c1Y ||
					Controls.c1Start ||
					Controls.c1Select) && skipImpulse) {
			
			return false;
		
		} else if(	(!applet.keyPressed &&
					!applet.mousePressed &&
					!Controls.c1A &&
					!Controls.c1B &&
					!Controls.c1X &&
					!Controls.c1Y &&
					!Controls.c1Start &&
					!Controls.c1Select) && skipImpulse) {
			
			skipImpulse = false;
			return false;
			
		} else { return false; }
		
	}
	
	
	
	static void vibrate() {

		if (Clock.curTime - vibTime1 >= vibDuration1){
			vibStrenght1 = 0;
		}
		
		if (Clock.curTime - vibTime2 >= vibDuration2){
			vibStrenght2 = 0;
		}
		
	}
	
	static void setVibrate(XboxController controller, float damage) {
		
		if(controller == Controls.gamepad1) {
			vibStrenght1 = (int) PApplet.map(damage, 0, 100, 0, 65535);
			vibDuration1 = (int) PApplet.map(damage, 0, 100, 0, 2000);;
			vibTime1 = Clock.curTime;
		}
		if(controller == Controls.gamepad2) {
			vibStrenght2 = (int) PApplet.map(damage, 0, 100, 0, 65535);
			vibDuration2 = (int) PApplet.map(damage, 0, 100, 0, 2000);;
			vibTime2 = Clock.curTime;
		}
		
	}
	
}
