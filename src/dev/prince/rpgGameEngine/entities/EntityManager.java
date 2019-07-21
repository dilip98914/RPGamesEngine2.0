package dev.prince.rpgGameEngine.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.creatures.Player;
import dev.prince.rpgGameEngine.features.InventoryItem;
import dev.prince.rpgGameEngine.tiles.Tile;

public class EntityManager {

	@SuppressWarnings("unused")
	private Handler handler;

	private Player player;
	private Pokemon bulbasur;

	private ArrayList<Entity> entities;
	private ArrayList<InventoryItem> items;

	private Comparator<Entity> renderSorter = new Comparator<Entity>() {
		public int compare(Entity a, Entity b) {

			if (a.zIndex < b.zIndex)
				return -1;
			if (a.zIndex > b.zIndex)
				return 1;
			if (a.getY() + a.getHeight() < b.getY() + b.getHeight()) {
				return -1;
			} else {
				return 1;
			}
		}

	};
	private boolean added = false;

	public boolean playerTouchesItem(Entity e, int xOffset, int yOffset) {
		if (this.player.getCollisionBounds(0f, 0f).intersects(e.getCollisionBounds(xOffset, yOffset))) {
			return true;
		}
		return false;
	}

	public EntityManager(Handler handler, Player player) {
		this.handler = handler;

		this.player = player;
		bulbasur = new Pokemon(handler, 629, 189, 40, 40, "idk", 0, 0);

		entities = new ArrayList<Entity>();
		items = this.player.getInventory().items;

	}

	public void initEntities() {
		entities.add(player);
		entities.add(bulbasur);
		items.add(new InventoryItem(bulbasur, 1));

	}
//
//	public void collectPokemonByPlayer(Pokemon pokemon0) {
//		if (pokemon0.interacted) {
////			System.out.println(pokemon0.interacted);
//			if (!added) {
//				this.inventoryIterator.add(new InventoryItem(pokemon0, 1));
////				this.player.addToInventory(pokemon0);
//				added = true;
//			}
//			this.pokeIterator.remove();
////			pokemon0.interacted=false;
//		}
//	}

//	public void throwPokemonByPlayer() {
//		Pokemon inventoryPokemon = (Pokemon) player.getInventory().currentItem.item;
//
//		while (inventoryIterator.hasNext()) {
//			InventoryItem e = inventoryIterator.next();
//			if (e.item.throwIt) {
//				System.out.println("calling");
////				this.player.removeFromInventory(inventoryPokemon);
//				inventoryIterator.remove();
//				e.item.throwIt = false;
//			}
//		}
//
//	}

	private void checkEntityIsActive(Entity e, int xStart, int xEnd, int yStart, int yEnd) {
		if ((e.getX() + e.getWidth() > xStart * Tile.TILEWIDTH) && (e.getX() < xEnd * Tile.TILEWIDTH)
				&& (e.getY() + e.getHeight() > yStart * Tile.TILEHEIGHT) && (e.getY() < yEnd * Tile.TILEHEIGHT)) {
			e.isActive = true;
		} else {
			e.isActive = false;
		}

	}

	public void tick(int xStart, int xEnd, int yStart, int yEnd) {

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			/*
			 * if(e instanceof Pokemon) { Pokemon ee=(Pokemon)e; collectPokemonByPlayer(ee);
			 * }else { }
			 */
			checkEntityIsActive(e, xStart, xEnd, yStart, yEnd);

			if (e.equals(player) || e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")) {
				e.tick();
				continue;
			}

			if (e.isActive) {
				e.tick();
			}
		}

		entities.sort(renderSorter);
	}

	public void render() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			
			if (e.equals(player) || e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")) {
				e.render();
				continue;
			}
			if (e.isActive) {
				e.render();
			}

		}

	}

	/// HEPLER METHODS///
	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public Entity getEntity(float x, float y) {// used in removing entities
		for (Entity e : entities) {
			if (e.getX() <= x && e.getY() <= y && (e.getX() + e.getWidth()) >= x && (e.getY() + e.getHeight()) >= y) {
				return e;
			}
		}
		return null;
	}

	/// GETTERS AND SETTERS///

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
