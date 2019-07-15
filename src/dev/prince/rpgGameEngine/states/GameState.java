package dev.prince.rpgGameEngine.states;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.Game;
import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.Item;
import dev.prince.rpgGameEngine.features.Clock;
import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.inputs.KeyManager;
import dev.prince.rpgGameEngine.ui.Chat;
import dev.prince.rpgGameEngine.ui.UIPrompt;
import dev.prince.rpgGameEngine.utils.Utils;
import dev.prince.rpgGameEngine.worlds.World;

public class GameState extends State {

	private World world;
	private WorldCreationState gameCreationState;
	private Color c = Color.darkGray.darker();
	public static boolean createWorld = false, toMenu = false, save = false,
			shouldDialogue;
	
	public static UIPrompt prompt;
	public static Chat chat;

	private float fadeValue = 1.0f, fadeMagnitude = 0f;

	public static String[] locations;

	public static String currentLocation, currentLevel, path, root,
			dialogue = "..";
	private Clock clock;

	public Item flint;

	public GameState(Handler handler) {
		super(handler);

		locations = Utils.loadFileAsString("res/worlds/saveFile.sav").split(
				"\\s+");

		currentLocation = locations[0];
		currentLevel = locations[1];

		root = "res/worlds/";
		path = root + currentLocation + "/";

		world = new World(handler, path + currentLevel);
		handler.setWorld(world);
		gameCreationState = new WorldCreationState(handler);
		prompt = new UIPrompt(handler, 20, handler.getHeight() - 40);

		flint = new Item(handler, 100, 100, 16, 16, "flint");
		clock = new Clock(handler);
		
		chat = new Chat(handler, 20, handler.getHeight() - 50 - 300, 400, 300, 10, 2, 5, 30);
		
	}

	@Override
	public void tick() {
		clock.tick();

		world.tick();

		// COMMANDS
		promptCommands();
		keyCommands();

		if (createWorld) {
			gameCreationState.tick();
		}

		flint.tick();
		prompt.tick();
		chat.tick();

	}

	@Override
	public void render() {
		world.render();
		if (createWorld) {
			gameCreationState.render();
		}
		clock.render();

		// SAVING..
		if (save) {
			gameCreationState.giveMessage(c, "PROGRESS SAVED");
			c = c.brighter(1f - fadeValue);
			fadeValue -= 0.001f;
			fadeMagnitude += (1f - fadeValue);
		}
		if (fadeMagnitude >= 1) {
			fadeValue = 1f;
			save = false;
			c = Color.darkGray.darker();
			fadeMagnitude = 0f;
		}

		flint.render();
		
		//inventory
		if (handler.getWorld().getEntityManager().getPlayer().isUseInventory()) {
			handler.getWorld().getEntityManager().getPlayer().getInventory()
					.render();
		}
		//dialouge box
		if (shouldDialogue) {
			renderDialogue(dialogue);
		}
		
		
		prompt.render();
		chat.render();
		
		
		
		//if no commands recognized
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			if(prompt.getPromptText().startsWith("/")){
				
				boolean noMatch = true;
				
				for(int i =0;i<gameCreationState.getCommands().length;i++){
					if(gameCreationState.getCommands()[i].equalsIgnoreCase(prompt.getPromptText())){
						noMatch = false;
						chat.addText("<" + gameCreationState.getExecutions()[i] + ">");
					}
				
				}
				if(noMatch)
					chat.addText("<Can't recognize the command! Did u try right clicking after it?>");
			}else{
				if(!prompt.getPromptText().equalsIgnoreCase(""))
					chat.addText("[Player]" + handler.getPlayer().getUsername() + ">> " +prompt.getPromptText());
				
			}
			
			
			
		}
		
		
		
		
	}

	public void renderDialogue(String message) {
		// System.out.println("RENDERING DIALOGUE");
		int xOffset = 25, yOffset = 5;
		int width = xOffset + (handler.getWidth() - (3 * xOffset));
		int height = yOffset + (4 * Fonts.font.getHeight(message)) + yOffset;

		// RENDER QUAD
		Renderer.setColor(1, 1, 1, 1);
		Renderer.renderQuad(xOffset, handler.getHeight() - height - yOffset,
				width, height);
		// RENDER OULINE
		Renderer.setColor(0, 0, 0, 1);
		Renderer.renderOutlineOfQuad(xOffset, handler.getHeight() - height
				- yOffset, width, height);

		// RENDER FONT

		Renderer.renderString(2 * xOffset, handler.getHeight() - height,
				message, Color.black, false);
	}

	// DIFFERENT COMMANDS
	public void promptCommands() {

		if (((GameState.prompt.getPromptText().equalsIgnoreCase("/save")) && KeyManager.value == Keyboard.KEY_RETURN)) {
			save = true;
			gameCreationState.saveGame();

		}
		if (!createWorld) {

			if (prompt.getPromptText().equalsIgnoreCase("/debug")
					&& KeyManager.value == Keyboard.KEY_RETURN) {
				createWorld = true;
				prompt.setPromptText("_");
				prompt.pointerPos = 0;
				System.out.println("Entered creation state");
			}

		}

		if (prompt.getPromptText().equalsIgnoreCase("/exit")
				&& Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			Game.closeProgram();
		}

		if (prompt.getPromptText().equalsIgnoreCase("/menu")
				&& Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			toMenu = true;
		}

	}

	public void keyCommands() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			toMenu = true;
		}
		if (toMenu) {
			State.setState(handler.getGame().getMenuState());
			toMenu = false;
		}

	}

	// GETTERS..
	public WorldCreationState getGameCreationSate() {
		return gameCreationState;
	}

	public Clock getClock() {
		return clock;
	}
	
	public Chat getChat(){
		return chat;
	}
	
}
