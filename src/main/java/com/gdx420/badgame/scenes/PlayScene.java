package com.gdx420.badgame.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.bg.badgame.core.Assets;
import com.bg.badgame.core.BadGame;
import com.bg.badgame.core.Bullet;
import com.bg.badgame.core.Enemy;
import com.bg.badgame.core.Map;
import com.bg.badgame.core.Player;
import com.bg.badgame.core.Prop;
import com.bg.bearplane.engine.BearTool;
import com.bg.bearplane.engine.Log;
import com.bg.bearplane.gui.Button;
import com.bg.bearplane.gui.Frame;
import com.bg.bearplane.gui.Scene;

public class PlayScene extends Scene {

	public static boolean tutorial = false;
	public int dir = 0; // n,ne,e,se,s,sw,w,nw
	public static int character = 0; // 0 = hillary 1 = hank 2 = thomas
	public float rad = 16;
	public int map = 0;
	public long attacktimer = 0;
	public long attacktime = 1500;

	public Player player = new Player();

	public String[] characternames = { "Artillery Clinton", "Tank Hill", "Thomas the Tank" };

	public static boolean dialog = true;
	public static String dialogStr = "Hello and welcome to Shoot shoot: the game that tanks. Each level you will have to help some memes out but watch out for violent memes";
	public static Frame f;
	public int[] quest = new int[3];
	public int[] key = new int[3];
	public int[] lock = new int[3];
	public static int level = 0;
	public String[] mapnames;

	public static PlayScene play;
	
	public void play(int c) {
		Player.hp = 100;
		character = c;
		level = 0;
		reset();
	}

	public Map[] maps = new Map[16];

	public PlayScene() {
		play = this;
		String itemtext = BearTool.readFile("item.txt");
		itemnames = itemtext.split("\\r?\\n");
		String maptext = BearTool.readFile("map.txt");
		mapnames = maptext.split("\\r?\\n");
		reset();
	}

	public void reset() {
		level++;
		for (int i = 0; i < 16; i++) {
			maps[i] = new Map(i, mapnames[BearTool.randInt(0, mapnames.length - 1)]);
		}
		for (int i = 0; i < 3; i++) {
			Player.key[i] = false;
			Player.lock[i] = false;
		}
		boolean found = false;
		int n;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].key = 0;
				found = true;
			}
		} while (!found);
		found = false;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].key = 1;
				found = true;
			}
		} while (!found);
		found = false;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].key = 2;
				found = true;
			}
		} while (!found);
		found = false;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].lock = 0;
				found = true;
			}
		} while (!found);
		found = false;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].lock = 1;
				found = true;
			}
		} while (!found);
		found = false;
		do {
			n = BearTool.randInt(0, 15);
			if (maps[n].key < 0 && maps[n].lock < 0) {
				maps[n].lock = 2;
				found = true;
			}
		} while (!found);
		for (int i = 0; i < 16; i++) {
			if (maps[i].key >= 0) {
				Prop p = new Prop(0);
				p.npc = true;
				p.type = maps[i].key;
				p.x = 400;
				p.y = 400;
				maps[i].props.add(p);
			}
			if (maps[i].lock >= 0) {
				Prop p = new Prop(0);
				p.npc = true;
				p.type = maps[i].lock + 3;
				p.x = 400;
				p.y = 400;
				maps[i].props.add(p);
			}
		}
		for (int i = 0; i < 3; i++) {
			n = BearTool.randInt(0, itemnames.length - 1);
			key[i] = n;
			lock[i] = n;
			n = BearTool.randInt(0, 5);
			quest[i] = n;
		}

		player = new Player();
	}

	String[] itemnames;

	public void start() {
		super.start();
		f = new Frame(this, "dialog", 683, 384, 600, 600, true, true);
		// f.visible = false;
		// f.addButton("dialogbtn", 683, 600, 200, 32, "Ok I guess");
		Button b = new Button(this, "dialogbtn", 683, 630, 200, 32, "Ok I guess", false);
		f.buttons.put("dialogbtn", b);
		frames.put("dialog", f);
		Assets.sounds.get("mus0").stop();
		Assets.sounds.get("mus1").loop();

	}

	public void update() {
		if (!dialog) {
			checkKeys();
			checkLevel();
		}
	}

	public Object collides(Object owner, float x, float y, float rad) {
		for (Prop p : maps[map].props) {
			if (BearTool.distance(x, y, p.x, p.y) < p.rad + rad) {
				return p;
			}
		}
		if (owner instanceof Player) {
			for (Enemy p : maps[map].enemies) {
				if (p.active && BearTool.distance(x, y, p.x, p.y) < p.rad + rad) {
					return p;
				}
			}
		} else {
			if (BearTool.distance(x, y, Player.x, Player.y) < this.rad + rad) {
				return BadGame.playScene.player;
			}
		}
		return null;
	}

	public static void showDialog(String s) {
		dialog = true;
		dialogStr = s;
	}

	void move(int d) {
		float mx = 0;
		float my = 0;
		float dist = 6.5f;
		// dir = d;
		switch (d) {
		case 0: // n
			my = -dist;
			break;
		case 1: // ne
			my = -dist;
			mx = dist;
			break;
		case 2: // e
			mx = dist;
			break;
		case 3: // se
			mx = dist;
			my = dist;
			break;
		case 4: // s
			my = dist;
			break;
		case 5: // sw
			my = dist;
			mx = -dist;
			break;
		case 6: // w
			mx = -dist;
			break;
		case 7: // nw
			my = -dist;
			mx = -dist;
			break;
		}
		if (collides(player, Player.x + mx, Player.y + my, rad) == null) {
			Player.x += mx;
			Player.y += my;
		} else {
			// play bonk sound?
		}
		if (Player.x < 0) {
			Player.x = BadGame.GAME_WIDTH - 10;
			map = getExit(map, 2);
		} else if (Player.x > BadGame.GAME_WIDTH) {
			Player.x = 10;
			map = getExit(map, 3);
		} else if (Player.y < 0) {
			Player.y = BadGame.GAME_HEIGHT - 10;
			map = getExit(map, 0);
		} else if (Player.y > BadGame.GAME_HEIGHT) {
			Player.y = 10;
			map = getExit(map, 1);
		}

	}

	int getExit(int map, int ed) {
		int m = map;
		switch (ed) {
		case 0:
			if (m < 4) {
				m += 12;
			} else {
				m -= 4;
			}
			break;
		case 1:
			if (m >= 12) {
				m -= 12;
			} else {
				m += 4;
			}
			break;
		case 2:
			if (m % 4 == 0) {
				m += 3;
			} else {
				m -= 1;
			}
			break;
		case 3:
			if (m % 4 == 3) {
				m -= 3;
			} else {
				m += 1;
			}
			break;
		}
		return m;
	}

	private void checkKeys() {
		int movedir = -1;
		if (input.keyDown[Keys.UP] && input.keyDown[Keys.LEFT]) {
			movedir = 7;
		} else if (input.keyDown[Keys.UP] && input.keyDown[Keys.RIGHT]) {
			movedir = 1;
		} else if (input.keyDown[Keys.DOWN] && input.keyDown[Keys.LEFT]) {
			movedir = 5;
		} else if (input.keyDown[Keys.DOWN] && input.keyDown[Keys.RIGHT]) {
			movedir = 3;
		} else if (input.keyDown[Keys.UP]) {
			movedir = 0;
		} else if (input.keyDown[Keys.DOWN]) {
			movedir = 4;
		} else if (input.keyDown[Keys.LEFT]) {
			movedir = 6;
		} else if (input.keyDown[Keys.RIGHT]) {
			movedir = 2;
		}
		if (movedir >= 0) {
			move(movedir);
		}
		if (input.keyDown[Keys.W] && input.keyDown[Keys.A]) { // upper left
			dir = 7;
		} else if (input.keyDown[Keys.W] && input.keyDown[Keys.D]) { // upper right
			dir = 1;
		} else if (input.keyDown[Keys.A] && input.keyDown[Keys.S]) { // lower left
			dir = 5;
		} else if (input.keyDown[Keys.S] && input.keyDown[Keys.D]) { // lower right
			dir = 3;
		} else if (input.keyDown[Keys.W]) { // up
			dir = 0;
		} else if (input.keyDown[Keys.A]) { // left
			dir = 6;
		} else if (input.keyDown[Keys.S]) { // down
			dir = 4;
		} else if (input.keyDown[Keys.D]) { // right
			dir = 2;
		}
		if (input.keyDown[Keys.ESCAPE]) {
			Scene.change("menu");
		}

		if (input.keyDown[Keys.SPACE]) {
			if (tick > attacktimer) {
				attacktimer = tick + attacktime;
				maps[map].bullets.add(new Bullet(0, Player.x, Player.y, getRot(dir), player));
			}

		}

	}

	float getRot(int d) {
		switch (d) {
		case 0:
			return 270;
		case 1:
			return 315;
		case 2:
			return 0;
		case 3:
			return 45;
		case 4:
			return 90;
		case 5:
			return 135;
		case 6:
			return 180;
		case 7:
			return 225;
		}
		return 0;
	}

	public void render() {
		String gg = "g" + maps[map].g;
		//Log.debug("gg:"+gg);
		//draw(Assets.textures.get("g4"),0,0,90,1f);
		draw(Assets.textures.get(gg), 0,0,0,0,1366,768);
		if (!dialog) {
			
			draw(Assets.textures.get("c" + character), Player.x - 64, Player.y - 64, getRot(dir), 1f);
			//draw(Assets.textures.get("c" + character + "a"), Player.x - 64, Player.y - 64 - 32, getRot(dir), 1f);
			
			for (Prop p : maps[map].props) {
				if (p.npc) {
					draw(Assets.textures.get("npc" + p.type), p.x - 48, p.y - 48, p.rot, 1f);
				} else {
					draw(Assets.textures.get("prop" + p.type), p.x - 48, p.y - 48, p.rot, 1f);
				}
			}
			for (Enemy p : maps[map].enemies) {
				p.update(this, tick);
				p.render(this);
			}
			for (Bullet b : maps[map].bullets) {
				b.update(this, tick);
				b.render(this);
			}
			if (tutorial) {
				drawFont(0, BadGame.GAME_WIDTH / 2, BadGame.GAME_HEIGHT / 2, "TUTORIAL", true, 6f);
			}
			f.visible = false;
		} else {
			f.visible = true;
			List<String> ss = BearTool.wrapText(2, 560, dialogStr);
			f.render();
			int i = 0;
			for (String s : ss) {
				drawFont(0, 393, 90 + i * 30, s, false, 2, Color.WHITE);
				i++;
			}
		}
		drawFont(0, BadGame.GAME_WIDTH / 2, 20, maps[map].name + "[LEVEL " + level + "]", true, 3f);
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
				reset();
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
		for (Prop p : maps[map].props) {
			if (p.npc) {
				if (BearTool.distance(x, y, p.x, p.y) < 48) {
					Log.debug("clicked npc");
					if (maps[map].key >= 0) {
						Log.debug("its giver " + maps[map].key);
						if (Player.key[maps[map].key]) {
							m = giverMsg(maps[map].key, true);
						} else {
							Player.key[maps[map].key] = true;
							m = giverMsg(maps[map].key, false);
						}
					}
					if (maps[map].lock >= 0) {
						// if (Player.lock[maps[map].lock]) {
						// m = getterMsg(maps[map].lock, true);
						// display already did it msg
						// } else {
						if (Player.lock[maps[map].lock]) { // player already completed lock
							m = "I'm good for now";
						} else { // player has not completed lock
							if (Player.key[maps[map].lock]) {
								Player.lock[maps[map].lock] = true;
								m = getterMsg(maps[map].lock, true);
								////checkLevel = true;
							} else {
								m = getterMsg(maps[map].lock, false);
							}
						}

						// }
						Log.debug("its getter " + maps[map].lock);
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

	public String getCharacterName() {
		return characternames[character];
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
