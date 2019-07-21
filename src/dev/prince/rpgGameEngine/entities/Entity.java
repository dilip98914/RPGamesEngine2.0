package dev.prince.rpgGameEngine.entities;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.Rectangle;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.creatures.PlayerMP;
import dev.prince.rpgGameEngine.states.GameState;

public abstract class Entity {
	/* TextureLess Entity */
	protected Handler handler;
	protected boolean interact = false;
	protected Entity interactingWith = null;
	protected float x, y, width, height;
	protected Rectangle bounds;
	private ArrayList<Entity> entities;
	public int zIndex = 0;

	protected boolean isActive = false, isStatic = false, isItem = false;

	protected boolean luminous = false;

	protected float xMove = 0, yMove = 0;

	public ArrayList<String> params;

	public static ArrayList<String> classes;

	protected String name;
	public boolean added=false;

	public Entity(Handler handler, float x, float y, float width, float height, String name) {
		this.handler = handler;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bounds = new Rectangle((int) 0, (int) 0, (int) width, (int) height);
		this.name = name;
		params = new ArrayList<String>();
		classes = new ArrayList<String>();

		// add Paramters
		params.add((int) x + "");
		params.add((int) y + "");
		params.add((int) width + "");
		params.add((int) height + "");
		params.add(name);

		// fill classes
		classes.add(this.getClass().getSimpleName());

	}

	public abstract void tick();

	public abstract void render();

	///// CHECK ENTITY COLLISION///////

	public boolean getEntityCollision(float xOffset, float yOffset) {
		entities = handler.getWorld().getEntityManager().entities;

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.equals(this)) {
				continue;
			}
			if (e.isStatic) {

				if (e.getCollisionBounds(0f, 0f).intersects(this.getCollisionBounds(xOffset, yOffset))) {

					interact = true;
					interactingWith = e;
				}
				continue;
			}
			if (e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")) {
				PlayerMP p = (PlayerMP) e;
				if (!p.getLocation().equalsIgnoreCase(GameState.currentLocation)
						|| !p.getLevel().equalsIgnoreCase(GameState.currentLevel)) {
					continue;
				}
			}
			if (e.isActive) {
				if (e.getCollisionBounds(0f, 0f).intersects(this.getCollisionBounds(xOffset, yOffset))) {

					return true;

				}
			}
		}

		return false;
	}

	/// GET COLLISION BOUNDS////

	public Rectangle getCollisionBounds(float xOffset, float yOffset) {
		return new Rectangle((int) (x + bounds.getX() + xOffset), (int) (y + bounds.getY() + yOffset),
				bounds.getWidth(), bounds.getHeight());
	}

	////////////////////////////// GETTERS/////////////////////////

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public boolean isActive() {
		return isActive;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public float getxMove() {
		return xMove;
	}

	public float getyMove() {
		return yMove;
	}

	public void setxMove(float xMove) {
		this.xMove = xMove;
	}

	public void setyMove(float yMove) {
		this.yMove = yMove;
	}

}
