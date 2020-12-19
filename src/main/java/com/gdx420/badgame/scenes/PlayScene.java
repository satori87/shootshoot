package com.gdx420.badgame.scenes;

import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.bg.badgame.core.Assets;
import com.bg.badgame.core.BadGame;
import com.bg.badgame.core.Bullet;
import com.bg.badgame.core.DirectionUtility;
import com.bg.badgame.core.Enemy;
import com.bg.badgame.core.Map;
import com.bg.badgame.core.Player;
import com.bg.badgame.core.Position;
import com.bg.badgame.core.Prop;
import com.bg.bearplane.engine.BearTool;
import com.bg.bearplane.engine.Log;
import com.bg.bearplane.gui.Button;
import com.bg.bearplane.gui.Frame;
import com.bg.bearplane.gui.Scene;
import com.bg.badgame.core.DirectionUtility.CardinalDirection;

public class PlayScene extends Scene {	
	public enum Characters { HILLARY, HANK, THOMAS }	
	public static Characters character = Characters.HANK;
	public static int level = 0;	
	
	public void play(Characters characterToPlay) {
		character = characterToPlay;
		resetLevel();
	}

	public String[] mapnames;
	
	public PlayScene() {
		String itemtext = BearTool.readFile("item.txt");
		itemnames = itemtext.split("\\r?\\n");
		String maptext = BearTool.readFile("map.txt");
		mapnames = maptext.split("\\r?\\n");
		resetLevel();
	}

	public int[] quest = new int[3];
	public int[] key = new int[3];
	public int[] lock = new int[3];
	public Map[] maps = new Map[16];
	public Player player = Player.getInstance();;	
	
	public void resetLevel() {
		for (int i = 0; i < 16; i++) {
			maps[i] = new Map(i, mapnames[BearTool.randInt(0, mapnames.length - 1)]);
		}
		for (int i = 0; i < 3; i++) {
			Player.key[i] = false;
			Player.lock[i] = false;
		}
		for (int timesRandomized = 0; timesRandomized < 6; timesRandomized++)
		{
			RandomizeMap();
		}
		for (int i = 0; i < 16; i++) {
			if (maps[i].key >= 0) {				
				maps[i].props.add(getProp(maps[i].key));
			}
			if (maps[i].lock >= 0) {
				maps[i].props.add(getProp(maps[i].lock + 3));
			}
		}
		int randomNumber;
		for (int i = 0; i < 3; i++) {
			randomNumber = BearTool.randInt(0, itemnames.length - 1);
			key[i] = randomNumber;
			lock[i] = randomNumber;
			randomNumber = BearTool.randInt(0, 5);
			quest[i] = randomNumber;
		}

		
	}
	
	public void RandomizeMap()
	{
		boolean found = false;
		int n;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].key = 0;
				found = true;
			}
		} while (!found);
	}
	
	public Prop getProp(int type)	{
		Prop p = new Prop(0);
		p.npc = true;
		p.type = type;
		p.position.x = 400;
		p.position.y = 400;		
		return p;
	}

	public static boolean dialog = true;
	public static Frame currentFrame;
	public void start() {
		super.start();
		currentFrame = new Frame(this, "dialog", 683, 384, 600, 600, true, true);
		Button b = new Button(this, "dialogbtn", 683, 630, 200, 32, "Ok I guess", false);
		currentFrame.buttons.put("dialogbtn", b);
		frames.put("dialog", currentFrame);
		Assets.sounds.get("mus0").stop();
		Assets.sounds.get("mus1").loop();

	}

	public void update() {
		if (!dialog) {
			checkKeys();
			checkLevel();
		}
	}
			
	void tryMovePlayer(CardinalDirection directionToMove) {
		
		Position moveDelta = determineMovements(directionToMove);
		
		if (collides(player, player.position.x + moveDelta.x, player.position.y + moveDelta.y, collisionRadius) == null) {
			player.move(moveDelta);			
		} else {
			// TODO: play bonk sound?
		}
		
		tryMapChange();
	}
	
	void tryMapChange() {
		if (player.position.x < 0) {
			player.position.x = BadGame.GAME_WIDTH - 10;
			currentMap = getExit(currentMap, CardinalDirection.EAST);
		} else if (player.position.x > BadGame.GAME_WIDTH) {
			player.position.x = 10;
			currentMap = getExit(currentMap, CardinalDirection.WEST);
		} else if (player.position.y < 0) {
			player.position.y = BadGame.GAME_HEIGHT - 10;
			currentMap = getExit(currentMap, CardinalDirection.NORTH);
		} else if (player.position.y > BadGame.GAME_HEIGHT) {
			player.position.y = 10;
			currentMap = getExit(currentMap, CardinalDirection.SOUTH);
		}
	}
	
	Position determineMovements(CardinalDirection directionToMove) {
		Position moveDelta = new Position();
		float distanceToMove = 6.5f;
		
		switch (directionToMove) {
		case NORTHEAST: 
			moveDelta.x = -distanceToMove;
		case NORTH: 
			moveDelta.y = -distanceToMove;
			break;
		case SOUTHEAST:
			moveDelta.y = distanceToMove;
		case EAST: 
			moveDelta.x = -distanceToMove;
			break;
		case SOUTHWEST: 
			moveDelta.x = distanceToMove;
		case SOUTH: 
			moveDelta.y = distanceToMove;
			break;
		case NORTHWEST: 
			moveDelta.y = -distanceToMove;
		case WEST: 
			moveDelta.x = distanceToMove;
			break;
		default:
			break;
		}
		return moveDelta;
	}
	
	int getExit(int map, CardinalDirection exitDirection) {
		int mapIndex = map;
		switch (exitDirection) {
			case EAST:
				if (mapIndex < 4) {
					mapIndex += 12;
				} else {
					mapIndex -= 4;
				}
				break;
			case SOUTH:
				if (mapIndex >= 12) {
					mapIndex -= 12;
				} else {
					mapIndex += 4;
				}
				break;
			case NORTH:
				if (mapIndex % 4 == 0) {
					mapIndex += 3;
				} else {
					mapIndex -= 1;
				}
				break;
			case WEST:
				if (mapIndex % 4 == 3) {
					mapIndex -= 3;
				} else {
					mapIndex += 1;
				}
				break;
		default:
			break;
		}
		return mapIndex;
	}

	private void checkKeys() {
		checkMoveDirection();		
		checkLookDirection();		
		if (input.keyDown[Keys.ESCAPE])
			Scene.change("menu");
		if (input.keyDown[Keys.SPACE])
			fireBullet();
	}
	
	void checkMoveDirection() {
		CardinalDirection moveDirection = CardinalDirection.NO_DIRECTION;
		if (input.keyDown[Keys.UP] && input.keyDown[Keys.LEFT]) {
			moveDirection = CardinalDirection.NORTHEAST;
		} else if (input.keyDown[Keys.UP] && input.keyDown[Keys.RIGHT]) {
			moveDirection = CardinalDirection.NORTHWEST;
		} else if (input.keyDown[Keys.DOWN] && input.keyDown[Keys.LEFT]) {
			moveDirection = CardinalDirection.SOUTHEAST;
		} else if (input.keyDown[Keys.DOWN] && input.keyDown[Keys.RIGHT]) {
			moveDirection = CardinalDirection.SOUTHWEST;
		} else if (input.keyDown[Keys.UP]) {
			moveDirection = CardinalDirection.NORTH;
		} else if (input.keyDown[Keys.DOWN]) {
			moveDirection = CardinalDirection.SOUTH;
		} else if (input.keyDown[Keys.LEFT]) {
			moveDirection = CardinalDirection.EAST;
		} else if (input.keyDown[Keys.RIGHT]) {
			moveDirection = CardinalDirection.WEST;
		}
		if (moveDirection != CardinalDirection.NO_DIRECTION) {
			tryMovePlayer(moveDirection);
		}
	}
	
	void checkLookDirection() {
		if (input.keyDown[Keys.W] && input.keyDown[Keys.A]) {
			player.direction = CardinalDirection.NORTHEAST;
		} else if (input.keyDown[Keys.W] && input.keyDown[Keys.D]) { 
			player.direction = CardinalDirection.NORTHWEST;
		} else if (input.keyDown[Keys.A] && input.keyDown[Keys.S]) { 
			player.direction = CardinalDirection.SOUTHEAST;
		} else if (input.keyDown[Keys.S] && input.keyDown[Keys.D]) { 
			player.direction = CardinalDirection.SOUTHWEST;
		} else if (input.keyDown[Keys.W]) { 
			player.direction = CardinalDirection.NORTH;
		} else if (input.keyDown[Keys.A]) { 
			player.direction = CardinalDirection.EAST;
		} else if (input.keyDown[Keys.S]) { 
			player.direction = CardinalDirection.SOUTH;
		} else if (input.keyDown[Keys.D]) { 
			player.direction = CardinalDirection.WEST;
		}
	}
	
	public long attacktimer = 0;
	public long attacktime = 1500;
	void fireBullet() {
		if (tick > attacktimer) {
			attacktimer = tick + attacktime;
			maps[currentMap].bullets.add(new Bullet(0, player.position.x, player.position.y, DirectionUtility.getRotationAngle(player.direction), player));
		}
	}

	public int currentMap = 0;
	public float collisionRadius = 16;
	public Object collides(Object owner, float x, float y, float radius) {
		for (Prop p : maps[currentMap].props) {
			if (BearTool.distance(x, y, p.position.x, p.position.y) < p.radius + radius) {
				return p;
			}
		}
		if (owner instanceof Player) {
			for (Enemy p : maps[currentMap].enemies) {
				if (p.active && BearTool.distance(x, y, p.x, p.y) < p.rad + radius) {
					return p;
				}
			}
		} else {
			if (BearTool.distance(x, y, player.position.x, player.position.y) < this.collisionRadius + radius) {
				return BadGame.playScene.player;
			}
		}
		return null;
	}


	public static String dialogStr = "Hello and welcome to Shoot shoot: the game that tanks. Each level you will have to help some memes out but watch out for violent memes";
	public static void showDialog(String s) {
		dialog = true;
		dialogStr = s;
	}

	
	public static boolean tutorial = false;	
	public void render() {
		String gg = "g" + maps[currentMap].g;
		draw(Assets.textures.get(gg), 0,0,0,0,1366,768);
		if (!dialog) {
			
			draw(Assets.textures.get("c" + character.ordinal()), 
				player.position.x - 64, 
				player.position.y - 64, 
				DirectionUtility.getRotationAngle(player.direction), 
				1f);
			
			for (Prop p : maps[currentMap].props) {
				if (p.npc) {
					draw(Assets.textures.get("npc" + p.type), p.position.x - 48, p.position.y - 48, p.rotation, 1f);
				} else {
					draw(Assets.textures.get("prop" + p.type), p.position.x - 48, p.position.y - 48, p.rotation, 1f);
				}
			}
			for (Enemy p : maps[currentMap].enemies) {
				p.update(this, tick);
				p.render(this);
			}
			for (Bullet b : maps[currentMap].bullets) {
				b.update(this, tick);
				b.render(this);
			}
			if (tutorial) {
				drawFont(0, BadGame.GAME_WIDTH / 2, BadGame.GAME_HEIGHT / 2, "TUTORIAL", true, 6f);
			}
			currentFrame.visible = false;
		} else {
			currentFrame.visible = true;
			List<String> ss = BearTool.wrapText(2, 560, dialogStr);
			currentFrame.render();
			int i = 0;
			for (String s : ss) {
				drawFont(0, 393, 90 + i * 30, s, false, 2, Color.WHITE);
				i++;
			}
		}
		drawFont(0, BadGame.GAME_WIDTH / 2, 20, maps[currentMap].name + "[LEVEL " + level + "]", true, 3f);
	}

	@Override
	public void buttonPressed(String id) {
		Assets.sounds.get("click").play();
		if (id.equals("dialogbtn")) {
			dialog = false;
		}
	}

	@Override
	public void enterPressedInField(String id) {

	}

	@Override
	public void enterPressedInList(String id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void listChanged(String id, int sel) {
		// TODO Auto-generated method stub
	}

	//public boolean checkLevel = false;
	
	void checkLevel() {
		if(!dialog) { 
			if(Player.lock[0] && Player.lock[1] && Player.lock[2]) {
				showDialog("Level " + level + " win");
				resetLevel();
				level++;
				int a = BearTool.randInt(0, 2);
				Assets.sounds.get("win" + a).play();
				if(a == 2) {
					level--;
				}
			}			
		}
	}
	
	@Override
	public void mouseDown(int x, int y, int button) {
		String m = "";
		for (Prop p : maps[currentMap].props) {
			if (p.npc) {
				if (BearTool.distance(x, y, p.position.x, p.position.y) < 48) {
					Log.debug("clicked npc");
					if (maps[currentMap].key >= 0) {
						Log.debug("its giver " + maps[currentMap].key);
						if (Player.key[maps[currentMap].key]) {
							m = giverMsg(maps[currentMap].key, true);
						} else {
							Player.key[maps[currentMap].key] = true;
							m = giverMsg(maps[currentMap].key, false);
						}
					}
					if (maps[currentMap].lock >= 0) {
						// if (Player.lock[maps[map].lock]) {
						// m = getterMsg(maps[map].lock, true);
						// display already did it msg
						// } else {
						if (Player.lock[maps[currentMap].lock]) { // player already completed lock
							m = "I'm good for now";
						} else { // player has not completed lock
							if (Player.key[maps[currentMap].lock]) {
								Player.lock[maps[currentMap].lock] = true;
								m = getterMsg(maps[currentMap].lock, true);
								////checkLevel = true;
							} else {
								m = getterMsg(maps[currentMap].lock, false);
							}
						}

						// }
						Log.debug("its getter " + maps[currentMap].lock);
					}
					if (m.length() > 0) {
						PlayScene.showDialog(m);
					}
				}
			}
		}
		// TODO Auto-generated method stub

	}

	String[] lockmsg = {
			"The day is long and my thirst is great, I could really use some {X}? I remember seeing it last at '{M}'.",
			"Hehe, I remember in my youth being very fond of {X}. Haven't been to '{M}' in far too long.",
			"Is that you {P}? The years have treated you well... remember when we used to share {X} back at our summer home in '{M}'",
			"{X} makes ya feel good... want me to make you feel good? Journey to '{M}' for some and we can spend all night with it.",
			"The targeted ad said I really need {X} and I have HAVE to get it from '{M}'. Do you offer contactless delivery?",
			"Ancient scrolls tell of a place beyond the veil, a '{M}', if you will. The wisdom you seek lies with the {X}. Go in peace {P}." };

	String[] keymsg = { "If only I had more arms to carry this {X}... do you want my extra?",
			"{X} for sale! Get your {X} right here. Two {X} for the price of one {X}.",
			"This here is the last {X} in the known galaxy. Can you really handle it {P}?",
			"Listen {P}, just be careful. Moderation is important. I'll hook you up this time.",
			"Buy more {X} now! Limited time offer specially designed for {P}.",
			"Welcome to AncientScrolls.Com please pay attention as our menu has recently changed. For {X} press 1." };

	String[] lockmsga = { "What great fortune! It is very mighty of you to bring me this {X}.",
			"My word, I didn't think anyone knew I loved {X}. Please keep my secret.",
			"Flop my abs {P}, I can't believe you found {X}! What a treat to share it again.",
			"Oh yes {P}, let's boogie on down.",
			"Thank you {P}, please just leave it at the door. The tip is included.",
			"By the Gods! You are the prophesied one! For generations they shall exclaim {P} the magnificent has come!" };

	String[] keymsga = { "More {X}? Haha, that was all the extra I had. Maybe next time.",
			"Boy you sure love {X}, you freak. I never sold you that. Go on, get.",
			"This here is the last {X} in the known galaxy. What do you mean I sold you the last one already. Get out of here {P}?",
			"That's enough. You're cut-off {P}. I don't want to see you go down this road.",
			"We appreciate your interest in {X}. Our customer service is temporarily offline. Sorry, no refunds.",
			"404 ERROR! PAGE NOT FOUND" };

	String[] itemnames;
	
	public String getterMsg(int lock, boolean give) {
		String n = itemnames[this.lock[lock]];
		String m = "";
		String mapname = "";
		if (give) {
			m = lockmsga[quest[lock]].replace("{X}", n);
			m = m.replace("{P}", getCharacterName());
			// return "You already got " + n + " for me!";
		} else {
			m = lockmsg[quest[lock]].replace("{X}", n);
			for(int i = 0; i < 16; i++) {
				if(maps[i].key == lock) {
					m = m.replace("{M}", maps[i].name);
				}
			}
			
			m = m.replace("{P}", getCharacterName());
			// return "Can you get " + n + " for me?";
		}
		return m;
	}

	public String giverMsg(int key, boolean already) {
		String n = itemnames[this.key[key]];
		String m = "";
		if (already) {
			m = keymsga[quest[key]].replace("{X}", n);
			m = m.replace("{P}", getCharacterName());
			// return "I already gave you my last " + n + ".";
		} else {
			m = keymsg[quest[key]].replace("{X}", n);
			m = m.replace("{P}", getCharacterName());
			// return "Yo man here is an " + n + " lol";
		}
		return m;
	}

	public String[] characternames = { "Artillery Clinton", "Tank Hill", "Thomas the Tank" };
	public String getCharacterName() {
		return characternames[character.ordinal()];
	}

	@Override
	public void mouseUp(int x, int y, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkBox(String id) {
		// TODO Auto-generated method stub

	}

}
