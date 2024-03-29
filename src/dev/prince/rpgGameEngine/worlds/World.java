package dev.prince.rpgGameEngine.worlds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dev.prince.rpgGameEngine.Game;
import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.Door;
import dev.prince.rpgGameEngine.entities.EntityManager;
import dev.prince.rpgGameEngine.entities.creatures.NPC;
import dev.prince.rpgGameEngine.entities.creatures.Player;
import dev.prince.rpgGameEngine.entities.creatures.PlayerMP;
import dev.prince.rpgGameEngine.entities.statics.StaticEntity;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.states.GameState;
import dev.prince.rpgGameEngine.tiles.Tile;
import dev.prince.rpgGameEngine.utils.Utils;
import dev.prince.rpgGameEngine.worlds.loadAndSave.WorldSave;

public class World {
	
	private Handler handler;
	
	private String worldPath;
	
	private Player player;
	float x=200 ,y=200;
	int count=0;
	private float spawnX,spawnY;
	private int width=0,height=0;//IN TERMS OF TILE NUMBER
	
    private int[][] tiles;
	private ArrayList<Integer[]> solidTiles;
	
	private EntityManager entityManager;
	
	public static boolean render=true;
	
	public World(Handler handler,String worldPath){
		this.handler=handler;
		player=new Player(handler,x,y,"Dilip","Dilip");
		entityManager = new EntityManager(handler,player);
		this.worldPath=worldPath;
		solidTiles = new ArrayList<Integer[]>();
		loadWorld(this.worldPath);	
		entityManager.initEntities();
		WorldSave.init(handler);
		
	}

	public void tick(){
		
		int xStart =(int) Math.max(0, handler.getGameCamera().getxOffset()/Tile.TILEWIDTH);
		int xEnd = (int) Math.min(width,(handler.getGameCamera().getxOffset()+handler.getWidth())/Tile.TILEWIDTH+1);
		int yStart =(int) Math.max(0, handler.getGameCamera().getyOffset()/Tile.TILEHEIGHT);
		int yEnd = (int) Math.min(height,(handler.getGameCamera().getyOffset()+handler.getHeight())/Tile.TILEHEIGHT+1);
		
		for(int y=yStart;y<yEnd;y++){
			for(int x=xStart;x<xEnd;x++){
				getTiles(x,y).tick();
				getTiles(x,y).isActive=true;
			}
		}
		entityManager.tick(xStart, xEnd, yStart, yEnd);
	}
	
	public void render() {		
		int xStart =(int) Math.max(0, handler.getGameCamera().getxOffset()/Tile.TILEWIDTH);
		int xEnd = (int) Math.min(width,(handler.getGameCamera().getxOffset()+handler.getWidth())/Tile.TILEWIDTH+1);
		int yStart =(int) Math.max(0, handler.getGameCamera().getyOffset()/Tile.TILEHEIGHT);
		int yEnd = (int) Math.min(height,(handler.getGameCamera().getyOffset()+handler.getHeight())/Tile.TILEHEIGHT+1);
	

		for(int y=yStart;y<yEnd;y++){
			for(int x=xStart;x<xEnd;x++){
				if(getTiles(x,y).zIndex<=0)
					getTiles(x,y).render((float)(x*Tile.TILEWIDTH - handler.getGameCamera().getxOffset()),(float)(y*Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
			}
		}
		
		
		
		entityManager.render();
		
		
		//debug mode red quad rendering logic
		Renderer.setColor(1f, 0f, 0f, 0.5f);
		if(GameState.createWorld){
			for(int i=0;i<solidTiles.size();i++){
				if(solidTiles.get(i)[0]<=xEnd && solidTiles.get(i)[0] >=xStart){
					if(solidTiles.get(i)[1]<=yEnd && solidTiles.get(i)[1] >=yStart){
						Renderer.renderQuad(solidTiles.get(i)[0]*Tile.TILEWIDTH-handler.getGameCamera().getxOffset(), solidTiles.get(i)[1]*Tile.TILEHEIGHT-handler.getGameCamera().getyOffset(), (int)Tile.TILEWIDTH, (int)Tile.TILEHEIGHT);
						count++;
					}
				}
			}
		}
		count=0;

		for(int y=yStart;y<yEnd;y++){
			for(int x=xStart;x<xEnd;x++){
				
				if(getTiles(x,y).zIndex>0){
					
					getTiles(x,y).render((float)(x*Tile.TILEWIDTH - handler.getGameCamera().getxOffset()),(float)(y*Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
				}
			}
		}
		count=0;
		
	}
	
	public Tile getTiles(int x, int y){
		
		if(x<0||x>=width||y<0||y>=height)
			return Tile.grassTile;
		
		Tile t = Tile.tiles[tiles[y][x]];
		if(t==null)
			return Tile.grassTile;
		return t;
	}	
	
	public void setTilesValue(int x,int y,int value){
		tiles[y][x] = value;
	}
	
	public int  getTilesValue(int x,int y){
		if(x<0||x>=width||y<0||y>=height)
			return 0;
		return tiles[y][x];
	}
	
	//LOADER METHODS
//	TODO:fix world loading
	public void loadWorld(String path){
			this.worldPath=path;
					
					setWorldPath(path);
					String file = Utils.loadFileAsString(path);
					String[] tokens = file.split("\\s+");
					width = Utils.parseInt(tokens[0]);
					height = Utils.parseInt(tokens[1]);
					spawnX = Utils.parseInt(tokens[2]);
					spawnY = Utils.parseInt(tokens[3]);
					tiles = new int[height][width];
					
					//FILL TILE ARRAY
					for(int y=0;y<height;y++){
						for(int x=0;x<width;x++){
							tiles[y][x]  = Integer.parseInt(tokens[(x+y*width)+4]);
						}
					}
					if(!Game.joinServer){
						entityManager.getPlayer().setX(spawnX);
						entityManager.getPlayer().setY(spawnY);
					}
					GameState.currentLocation = new File(path).getParentFile().getName();
					GameState.currentLevel = tokens[width*height+4];
					
					loadSolidTileData();
					
					loadEntity();
					
	}
	
	public ArrayList<Integer[]> getSolidTiles() {
		return solidTiles;
	}

	//LOAD SOLID TILE DATA
	public void loadSolidTileData(){
			
			solidTiles.clear();
			
			String[] tokens = Utils.loadFileAsString(this.worldPath.substring(0, this.worldPath.length()-5)+"solids").split("\\s+");
			if(tokens[0]==" " )
				return;
			for(int i=0;i<tokens.length;i+=2){
				try{
					solidTiles.add(new Integer[]{Utils.parseInt(tokens[i]),Utils.parseInt(tokens[i+1])});
				}catch(NumberFormatException e){
					solidTiles.add(new Integer[] {1,1} );
				}
				
			}
			
		
	}
	
	public void addSolidTile(int x ,int y){
		for(int i=0;i<solidTiles.size();i++){
			if(solidTiles.get(i)[0] == x && solidTiles.get(i)[1]==y){
				return;
			}
		}
		solidTiles.add(new Integer[]{x,y});
	}
	public void removeSolidTile(int x,int y){
		for(int i=0;i<solidTiles.size();i++){
			if(x == solidTiles.get(i)[0] && y == solidTiles.get(i)[1]){
				solidTiles.remove(i);
				return;
			}
		}
	}
	public boolean getSolidTile(int x , int y){
		for(int i=0;i<solidTiles.size();i++){
			if(x == solidTiles.get(i)[0] && y == solidTiles.get(i)[1] ){
				return true;
			}
		}
		return false;
	}
	
	public void loadEntity(){	
		
		entityManager.entities.removeAll(entityManager.entities);
		entityManager.pokemons.removeAll(entityManager.pokemons);
		entityManager.items.removeAll(entityManager.items);

//		entityManager.addEntity(player);
		entityManager.initEntities();
		if(Game.isServer){
			for(PlayerMP p:handler.getServer().connectedPlayers){
				entityManager.addEntity(p);
			}
		}
		if(Game.joinServer){
			for(PlayerMP p:handler.getClient().getConnectedPlayers()){
				entityManager.addEntity(p);
			}
		}
		
		String line="";
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(this.worldPath.substring(0, this.worldPath.length()-5)+"entity"));
			while((line = br.readLine()) != null){
				String[] data = line.split("\\s+");
				if(data[0].equalsIgnoreCase("npc")){//NPC
					//WorldCreationState.npcCount++;
					entityManager.addEntity(new NPC(handler,Utils.parseInt(data[1]),Utils.parseInt(data[2]),"test"));
				}
				if(data[0].equalsIgnoreCase("door")){//DOOR
					entityManager.addEntity(new Door(
							handler,
							Utils.parseInt(data[1]),Utils.parseInt(data[2]),
							Utils.parseInt(data[3]),Utils.parseInt(data[4]),
							Utils.parseInt(data[5]),Utils.parseInt(data[6]),
							data[7],data[8],
							data[9],data[10],
							player,
							((data[12].equalsIgnoreCase("true")?true:false)),"test"
							));
				}
				if(data[0].equalsIgnoreCase("StaticEntity")){
					entityManager.addEntity(new StaticEntity(handler,Utils.parseInt(data[1]),Utils.parseInt(data[2]),
							Utils.parseInt(data[3]),
							Utils.parseInt(data[4]),
							data[5]));
				}
			}
			br.close();
			
		} catch (IOException | IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		
		
	}
	
	////GETTERS////

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setWidth(int a){
		this.width=a;
	}
	
	public void setHeight(int a){
		this.height=a;
	}

	public int[][] getTiles() {
		return tiles;
	}

	public String getWorldPath() {
		return worldPath;
	}



	public void setWorldPath(String worldPath) {
		this.worldPath = worldPath;
	}


	
	
}
