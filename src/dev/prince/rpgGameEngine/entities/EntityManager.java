package dev.prince.rpgGameEngine.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.creatures.Player;
import dev.prince.rpgGameEngine.features.InventoryItem;
import dev.prince.rpgGameEngine.tiles.Tile;

public class EntityManager {
	
	@SuppressWarnings("unused")
	private Handler handler;

	private Player player;
	private Pokemon bulbasur;

	public Iterator<Pokemon> pokeIterator;
	public Iterator<Entity> entityIterator;
	private ListIterator <InventoryItem> inventoryIterator;	
	private ArrayList<Entity> entities;
	private ArrayList<Pokemon> pokemons;
	private Comparator<Entity> renderSorter = new Comparator<Entity>(){
		public int compare(Entity a , Entity b){
				
				if(a.zIndex < b.zIndex)
					return -1;
				if(a.zIndex > b.zIndex)
					return 1;
				if(a.getY()+a.getHeight()<b.getY()+b.getHeight()){
					return -1;
				}else{
					return 1;
				}
			}
		
	};
	private boolean added=false;
	
	public boolean playerTouchesItem(Entity e,int xOffset,int yOffset) {
		if(this.player.getCollisionBounds(0f, 0f).intersects(e.getCollisionBounds(xOffset,yOffset))) {
			return true;
		}
		return false;
	}
	
	public EntityManager(Handler handler,Player player){
		this.handler=handler;
		this.player=player;
		bulbasur=new Pokemon(handler, 629, 189, 40, 40, "idk", 0,0);
		entities = new ArrayList<Entity>();
		pokemons = new ArrayList<Pokemon>();
	}
	
	public void initEntities() {
		entities.add(player);
		pokemons.add(bulbasur);
		entityIterator = entities.iterator();
		pokeIterator= pokemons.iterator();
		inventoryIterator=player.iterator;
	}
	
	public void collectPokemonByPlayer(Pokemon pokemon0) {
		if(pokemon0.interacted) {
//			System.out.println(pokemon0.interacted);
			if(!added) {
				this.inventoryIterator.add(new InventoryItem(pokemon0, 1));
//				this.player.addToInventory(pokemon0);
				added=true;
			}
			this.pokeIterator.remove();
//			pokemon0.interacted=false;
		}
	}
	
	public void throwPokemonByPlayer() {
		Pokemon inventoryPokemon=(Pokemon)player.getInventory().currentItem.item;
		
		while(inventoryIterator.hasNext()){
			InventoryItem e = inventoryIterator.next();
			if(e.item.throwIt) {
				System.out.println("calling");
//				this.player.removeFromInventory(inventoryPokemon);
				inventoryIterator.remove();
				e.item.throwIt=false;
			}
		}
		
	}

	
	public void tick(int xStart,int xEnd,int yStart,int yEnd){
		entityIterator = entities.iterator();
		pokeIterator=pokemons.iterator();
		while(entityIterator.hasNext()){
			Entity e = entityIterator.next();
			
//			if(e instanceof Pokemon) {
//				Pokemon ee=(Pokemon)e;
//				collectPokemonByPlayer(ee);
////				System.out.println("tryinh but can't");
//			}else {
////				System.out.println("cant find");
//			}
			if((e.getX() + e.getWidth() > xStart*Tile.TILEWIDTH) && (e.getX() < xEnd * Tile.TILEWIDTH) &&
				(e.getY() + e.getHeight() > yStart*Tile.TILEHEIGHT) && (e.getY() < yEnd*Tile.TILEHEIGHT)	
				){
				e.isActive=true;
			}else{
				e.isActive=false;
			}

			if(e.equals(player) || e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")){
				e.tick();
				continue;
			}
			
			if(e.isActive ) {
				e.tick();
			}
			
			
		}
//
		
		while(pokeIterator.hasNext()){
			Pokemon p = pokeIterator.next();
			
//			if((e.getX() + e.getWidth() > xStart*Tile.TILEWIDTH) && (e.getX() < xEnd * Tile.TILEWIDTH) &&
//				(e.getY() + e.getHeight() > yStart*Tile.TILEHEIGHT) && (e.getY() < yEnd*Tile.TILEHEIGHT)	
//				){
//				e.isActive=true;
//			}else{
//				e.isActive=false;
//			}
			p.isActive=true;
			if(p.isActive ) {
				collectPokemonByPlayer(p);
				p.tick();
			}
		}
//		throwPokemonByPlayer();

		
//		for(Pokemon p:pokemons) {
//			p.tick();
//		}
		entities.sort(renderSorter);
	} 
	
	public void render(){
		entityIterator = entities.iterator();
		pokeIterator = pokemons.iterator();
		while(entityIterator.hasNext()){
			Entity e = entityIterator.next();	
			
		if(e.equals(player) || e.getClass().getSimpleName().equalsIgnoreCase("PlayerMP")){
				e.render();
				continue;
			}
		if(e.isActive){
				e.render();
			}
			
		}

		while(pokeIterator.hasNext()){
			Pokemon p = pokeIterator.next();
			if(p.isActive ) {
				p.render();
			}
		}
	}
	
	///HEPLER METHODS///
	public void addEntity(Entity e){
		entities.add(e);
		entityIterator = entities.iterator();
	}
	
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
	public Entity getEntity(float x,float y){//used in removing entities
		for(Entity e:entities){
			if(e.getX()<=x && e.getY()<=y && (e.getX()+e.getWidth())>=x && (e.getY()+e.getHeight())>=y){
				return e;
			}
		}
		return null;
	}
	
	///GETTERS AND SETTERS///
	
	public Player getPlayer() {
		return player;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	
	
	
	
}
