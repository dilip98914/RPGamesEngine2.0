package dev.prince.rpgGameEngine.states;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.creations.Creation;
import dev.prince.rpgGameEngine.creations.EffectsCreation;
import dev.prince.rpgGameEngine.creations.EntityCreation;
import dev.prince.rpgGameEngine.creations.MapCreation;
import dev.prince.rpgGameEngine.creations.TileCreation;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.inputs.EventManager;
import dev.prince.rpgGameEngine.tiles.Tile;
import dev.prince.rpgGameEngine.ui.TextArea;
import dev.prince.rpgGameEngine.utils.Utils;
import dev.prince.rpgGameEngine.worlds.loadAndSave.WorldSave;

public class WorldCreationState extends State{
	
	
//	private Color c=Color.darkGray.darker();
//	private float fadeValue=1.0f,fadeMagnitude=0f;	
//	private boolean save=false;
	
	private Creation[] modes;
	
	private String[] commands;
	private String[] executions;
	
	public WorldCreationState(Handler handler) {
		super(handler);
		
		modes = new Creation[6];
		modes[0] = new TileCreation(handler);
		modes[1] = new MapCreation(handler);
		modes[2] = new EntityCreation(handler);
		modes[3] = new EffectsCreation(handler);
		
		commands = Utils.loadFileAsString("res/commandsData/commands.info").split("\n");
		executions = Utils.loadFileAsString("res/commandsData/executions.info").split("\n");
		
	}
	
	@Override
	public void tick() {
		String promptText = GameState.prompt.getPromptText();

		if(promptText.equalsIgnoreCase("/debugEnd") ){
			GameState.createWorld=false;
			System.out.println("EXITING WORLD CREATION STATE");
		}
		
		
		//SET CREATIONS ACCORDINGLY
		 if(GameState.prompt.getPromptText().toLowerCase().startsWith("/enter") ){
			 for(int i=0;i<modes.length;i++){
				 if(modes[i] == null)
					 return;
//				 String some=modes[i].getClass().getSimpleName().substring(0, modes[i].getClass().getSimpleName().length()-8);
//				 System.out.println(some);
				 if(GameState.prompt.getPromptText().equalsIgnoreCase("/enter "+modes[i].getClass().getSimpleName().substring(0, modes[i].getClass().getSimpleName().length()-8))){
					 if(EventManager.value == Keyboard.KEY_RETURN ){
							Creation.setCreation(modes[i]);
					 }
				 }
			 }
		
		 }
		

		if(Creation.currentCreation!=null){
			Creation.currentCreation.tick();
		}
		
	}
	
	
	
	@Override
	public void render() {
		
		
		
		 //RENDER INFO----------------------------
		 TextArea.renderTextArea(7, 5, "DEBUG: TRUE", Color.black,-1,-1);
		 TextArea.renderTextArea(7, 30, "X: "+ (int)handler.getWorld().getEntityManager().getPlayer().getX(), Color.black,-1,-1);
		 TextArea.renderTextArea(7, 55, "Y: "+ (int)handler.getWorld().getEntityManager().getPlayer().getY(), Color.black,-1,-1);
		 TextArea.renderTextArea(7, 80, "Tile X: "+ (int)((handler.getWorld().getEntityManager().getPlayer().getX()+handler.getWorld().getEntityManager().getPlayer().getWidth())/Tile.TILEWIDTH), Color.black,-1,-1);
		 TextArea.renderTextArea(7, 105, "Tile Y: "+ (int)((handler.getWorld().getEntityManager().getPlayer().getY()+handler.getWorld().getEntityManager().getPlayer().getHeight())/Tile.TILEHEIGHT), Color.black,-1,-1);
		 TextArea.renderTextArea(7,  130, "Level: "+GameState.currentLevel, Color.black,-1,-1);

		 
		 if(Creation.currentCreation!=null){
			 TextArea.renderTextArea(7,  155, "MODE: "+Creation.currentCreation.getClass().getSimpleName(), Color.black,-1,-1);
				Creation.currentCreation.render();
			}
			
		

		
	}
	
	//SAVING CODE..
	
	public void saveWorld(){
		WorldSave.Save(handler,(int)handler.getWorld().getEntityManager().getPlayer().getX(),(int) handler.getWorld().getEntityManager().getPlayer().getY(), handler.getWorld().getWidth(), handler.getWorld().getHeight(), handler.getWorld().getTiles());
	}
	
	public void saveGame(){
		saveWorld();
		WorldSave.mainSave();
	}
	
	public void giveMessage(Color c,String message){
		Renderer.renderString(handler.getWidth()-(message.length()*25), 25f, message, c, true);
	
	}
	
	//GETTERS
	public EffectsCreation getEffects(){
		
		return (EffectsCreation) modes[3];
		
	}

	public String[] getCommands() {
		return commands;
	}

	public String[] getExecutions() {
		return executions;
	}
	
	
	
}
