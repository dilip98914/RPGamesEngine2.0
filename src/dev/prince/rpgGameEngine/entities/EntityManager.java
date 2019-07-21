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

	public ArrayList<Pokemon> pokemons;
	public ArrayList<Entity> entities;
	public ArrayList<InventoryItem> items;

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

	public boolean playerTouchesItem(Entity e, int xOffset, int yOffset) {
		if (this.player.getCollisionBounds(0f, 0f).intersects(e.getCollisionBounds(xOffset, yOffset))) {
			return true;
		}
		return false;
	}

	public EntityManager(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		bulbasur = new Pokemon(handler, 629, 225, 40, 40, "idk", 0, 0);
		entities = new ArrayList<Entity>();
		pokemons = new ArrayList<Pokemon>();
		items = this.player.getInventory().items;

	}

	public void initEntities() {
		entities.add(player);
		pokemons.add(bulbasur);
		items.add(new InventoryItem(new Pokemon(handler, 529, 289, 40, 40, "idk2", 4, 6), 1));
		items.add(new InventoryItem(new Pokemon(handler, 529, 189, 40, 40, "idk2", 5, 6), 3));

	}

	public void collectPokemonByPlayer(Pokemon pp) {
		if (pp.interacted) {
			if (!pp.addedToInventory) {
				this.items.add(new InventoryItem(pp, 1));
				pp.addedToInventory = true;
				pp.addedToMap = false;

			}
//			pp.interacted = false;
		}

	}

//	private void deleteAllEntities(ArrayList<Entity> entities) {
//		for (int i = 0; i < entities.size(); i++) {
//			Entity e = entities.get(i);
//			if (e.removed) {
//				entities.remove(i--);
//			}
//		}
//
//	}

	private void deleteAllPokemon(ArrayList<Pokemon> pokemons) {
		for (int i = 0; i < pokemons.size(); i++) {
			Pokemon e = pokemons.get(i);
			if (e.addedToInventory) {
				pokemons.remove(i--);
			}
		}

	}

	private void deleteAllItems(ArrayList<InventoryItem> items) {
//		System.out.println("size: "+items.size());
		for (int i = 0; i < items.size(); i++) {
			InventoryItem e = items.get(i);
			if (e.item.addedToMap) {
//				System.out.println("i beofer"+i);
				items.remove(i--);
//				System.out.println("i after"+i);
//				this.player.getInventory().index=i;
//				this.player.getInventory().currentItem=items.get(i);
//				if(this.player.getInventory().index>=1) {
//					this.player.getInventory().index--;	
//				}
			}
		}

	}

	public void throwPokemonByPlayer() {
		if (player.getInventory().throwEvent) {
			Pokemon currPokemon = (Pokemon) player.getInventory().currentItem.item;
			
			for (int i = 0; i < items.size(); i++) {
				
				InventoryItem e = items.get(i);
				Pokemon p = (Pokemon) e.item;
				System.out.println("reached");


				if (e.item == currPokemon && !p.addedToMap) {
					p.addedToMap=true;
					pokemons.add(p);
					p.addedToInventory=false;
				}
				player.getInventory().throwEvent=false;
				return;
			}
		}
	}

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
			checkEntityIsActive(e, xStart, xEnd, yStart, yEnd);
//			if (e.equals(player) || e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")) {
//				e.tick();
//				continue;
//			}

			if (e.isActive) {
				e.tick();
			}
		}

		for (int i = 0; i < pokemons.size(); i++) {
			Pokemon e = pokemons.get(i);
			checkEntityIsActive(e, xStart, xEnd, yStart, yEnd);
			if (e.isActive) {
				collectPokemonByPlayer(e);
				e.tick();
			}
		}
		throwPokemonByPlayer();

//		deleteAllEntities(entities);
		deleteAllPokemon(pokemons);
		deleteAllItems(items);
//		entities.sort(renderSorter);
	}

	public void render() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);

//			if (e.equals(player) || e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")) {
//				e.render();
//				continue;
//			}
			if (e.isActive) {
				e.render();
			}

		}

		for (int i = 0; i < pokemons.size(); i++) {
			Pokemon e = pokemons.get(i);

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

}
