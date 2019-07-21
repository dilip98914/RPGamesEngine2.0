package dev.prince.rpgGameEngine.entities.creatures;

import java.util.ArrayList;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import dev.prince.rpgGameEngine.Game;
import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.EntityManager;
import dev.prince.rpgGameEngine.entities.Item;
import dev.prince.rpgGameEngine.entities.Pokemon;
import dev.prince.rpgGameEngine.features.Inventory;
import dev.prince.rpgGameEngine.features.InventoryItem;
import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.gfx.Assets;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.inputs.EventManager;
import dev.prince.rpgGameEngine.net.packets.Packet02Move;
import dev.prince.rpgGameEngine.states.GameState;

public class Player extends Creature {

	protected Texture sheet;
	private boolean useInventory = false;
	private Packet02Move movePacket = new Packet02Move((byte) 0, (byte) 0);
//	private float xOld,yOld;
	private String username;
	private byte countMP = 0;

	private Inventory inventory;
	private Item pokemon;

	public Player(Handler handler, float x, float y, String name,
			String username) {
		super(handler, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, name);
		bounds.setX(18);
		bounds.setY(38);
		bounds.setWidth(17);
		bounds.setHeight(12);
		this.username = username;
		sheet = Assets.playerSheet;
		inventory = new Inventory(handler);
	}

	@Override
	public void tick() {
		handler.getGameState();
		if (!GameState.prompt.focused)
			getInput();
//		interact();

		if (EventManager.value == Keyboard.KEY_E && !GameState.prompt.focused) {
			useInventory = !useInventory;
			EventManager.value = 0;
		}
		if (useInventory) {
			inventory.tick(EventManager.emitEvents());
//					return;
		}
		zIndex = 0;
		checkSwim();
		if (isSwimming) {
//					sheet = Assets.playerSwimSheet;
//					sheet = Assets.playerSheet;
		} else {
			isSwimming = false;
			sheet = Assets.playerSheet;
		}

		// RUN
		if (!isSwimming) {
			if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
				run();
				animDown.setAnimSpeed(animSpeed / 2 + 10);
				animUp.setAnimSpeed(animSpeed / 2 + 10);
				animLeft.setAnimSpeed(animSpeed / 2 + 10);
				animRight.setAnimSpeed(animSpeed / 2 + 10);
			} else {
				animDown.setAnimSpeed(animSpeed);
				animUp.setAnimSpeed(animSpeed);
				animLeft.setAnimSpeed(animSpeed);
				animRight.setAnimSpeed(animSpeed);
			}
		}

		if (!GameState.createWorld) {
			move();
			speed = DEFAULT_SPEED;
		} else {
			x += xMove;
			y += yMove;
			speed = (byte) (DEFAULT_SPEED * 3);
		}

		if (yMove > 0)
			animDown.animTick();
		if (xMove < 0)
			animLeft.animTick();
		if (xMove > 0)
			animRight.animTick();
		if (yMove < 0)
			animUp.animTick();

		checkOutOfBounds();
		playSound();
		handler.getGameCamera().centerOnEntity(handler.getPlayer());
		sendMoveData();
	}

	public void sendMoveData() {
		if (Game.joinServer) {
			countMP++;
			if (countMP >= 120) {
				countMP = 0;
				for (PlayerMP p : handler.getClient().getConnectedPlayers()) {
					handler.getClient().sendData(("4" + " " + (int) x + " " + (int) y + " ").getBytes(), p.ipAddress,
							p.port);
					// System.out.println(username+": "+"Connected Ports: " + p.port);
				}
				return;
			}
			movePacket.setX((byte) xMove);
			movePacket.setY((byte) yMove);
			// SEND TO OTHER CLIENTS
			for (PlayerMP p : handler.getClient().getConnectedPlayers()) {
				handler.getClient().sendData(movePacket.getData(), p.ipAddress, p.port);
			}
		} else if (Game.isServer) {
			countMP++;
			if (countMP >= 120) {
				countMP = 0;
				for (PlayerMP p : handler.getServer().getConnectedPlayers()) {
					handler.getServer().sendData(("4" + " " + (int) x + " " + (int) y + " ").getBytes(), p.ipAddress,
							p.port);
					// System.out.println(username+": "+"Connected Ports: " + p.port);
				}
				return;
			}
			movePacket.setX((byte) xMove);
			movePacket.setY((byte) yMove);
			// SEND TO OTHER CLIENTS
			for (PlayerMP p : handler.getServer().getConnectedPlayers()) {
				handler.getServer().sendData(movePacket.getData(), p.ipAddress, p.port);
			}
		}

	}

	@Override
	public void render() {
//			System.out.println("player rendeing");

		Renderer.renderString(x - handler.getGameCamera().getxOffset() - Fonts.font.getWidth(username) / 2 + width / 2,
				y - handler.getGameCamera().getyOffset() - 20, username, Color.black, false);
		Renderer.setColor(1, 1, 1, 1);
		Renderer.renderSubImage(sheet, (x - handler.getGameCamera().getxOffset()),
				(y - handler.getGameCamera().getyOffset()), width, height, getCurrentAnimationFrame(), 1f);

		if (useInventory) {
			getInventory().render();
		}

		/*
		 * Renderer.setColor(1, 1, 1, 0.51f);
		 * Renderer.renderQuad((x-handler.getGameCamera().getxOffset())+bounds.getX(),
		 * (y-handler.getGameCamera().getyOffset())+bounds.getY(), bounds.getWidth(),
		 * bounds.getHeight());
		 */

	}

	// HELPER METHODS//
	public void getInput() {
		xMove = 0;
		yMove = 0;

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			direction = 0;
			yMove = -speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			direction = 1;
			xMove = speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			direction = 2;
			yMove = speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			direction = 3;
			xMove = -speed;
		}

	}

//	public void interact() {
//		if (interact) {
//			if (Keyboard.isKeyDown(Keyboard.KEY_F) && this.direction == 0) {
//				System.out.println("INTERACT");
//				interact = false;
//			}
//		}
//	}

	public Inventory getInventory() {
		return inventory;
	}

	public boolean isUseInventory() {
		return useInventory;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getUsername() {
		return username;
	}

}
