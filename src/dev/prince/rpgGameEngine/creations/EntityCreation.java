package dev.prince.rpgGameEngine.creations;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.Door;
import dev.prince.rpgGameEngine.entities.Entity;
import dev.prince.rpgGameEngine.entities.creatures.Creature;
import dev.prince.rpgGameEngine.entities.creatures.NPC;
import dev.prince.rpgGameEngine.entities.statics.StaticEntity;
import dev.prince.rpgGameEngine.features.Command;
import dev.prince.rpgGameEngine.gfx.Assets;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.inputs.EventManager;
import dev.prince.rpgGameEngine.states.GameState;
import dev.prince.rpgGameEngine.utils.Utils;
import dev.prince.rpgGameEngine.worlds.loadAndSave.WorldSave;

public class EntityCreation extends Creation {

	private boolean place = false, hold = false;
//	mouse x,mousey are world coordinated where mouse pointing;
	private int mouseX, mouseY, sWidth = 50, sHeight = 50, staticEntityX, staticEntityY;
//	private int doorLength = 50;
	private Entity e = null;

	private boolean W_PRESSED, S_PRESSED, A_PRESSED, D_PRESSED;
	private final int ALTER_CONST = 5 * 2;
	private int xc, yc;

	boolean move = false;
	boolean inputCompleted = false;
	boolean allowCommand = false;
	String initialCommand = "";
	boolean rightClick=false;

	private void initCommandsInfo() {
		Command.addCommand(new Command("/use staticentity"));
		Command.addCommand(new Command("/use npc"));
		Command.addCommand(new Command("/use door"));
		Command.addCommand(new Command("/move entity"));
		Command.addCommand(new Command("/delete entity"));

	}

	public EntityCreation(Handler handler) {
		super(handler);
		initCommandsInfo();
	}

	public void tick() {
		getEvents();
		mouseX = (int) ((handler.getGameCamera().getxOffset() + Mouse.getX()));
		mouseY = (int) (((handler.getHeight() - Mouse.getY()) + handler.getGameCamera().getyOffset()));


	}

	public void getEvents() {
		W_PRESSED = S_PRESSED = A_PRESSED = D_PRESSED = false;
		xc = yc = 0;
		rightClick=false;
		
		String key = EventManager.getInput(true);
		
		if (key.contains("w")) {
			W_PRESSED = true;
			yc = 1;
		} else if (key.contains("s")) {
			S_PRESSED = true;
			yc = -1;
		} else if (key.contains("a")) {
			A_PRESSED = true;
			xc = -1;
		} else if (key.contains("d")) {
			D_PRESSED = true;
			xc = 1;
		} else if (key.contains("/")) {
			inputCompleted = true;
		}
		
		if(EventManager.mouse_button==1) {//right
			rightClick=true;
			EventManager.mouse_button=0;
		}
	}

	private void alterSize(int xc0, int yc0) {
		sWidth += xc0 * ALTER_CONST;
		sHeight += yc0 * ALTER_CONST;
	}

	private Command checkForValidCommand(String input) {
		String input0 = input.toLowerCase();
		for (int i = 0; i < Command.commands.size(); i++) {
			Command cc = Command.commands.get(i);
			if (input0.contains(cc.command)) {
				cc.value = true;
				System.out.println("command: " + cc.command);

				if (!allowCommand) {
					initialCommand = cc.command;
					allowCommand = true;
				}

				return cc;
			}
		}
		return null;

	}

	private String getPromptText() {
		return handler.getGameState().prompt.getPromptText().toLowerCase();
	}

	private int getPromptLength() {
		return handler.getGameState().prompt.getPromptText().length();
	}

	public void render() {
		boolean makeDoor = false, makeStatic = false;
		int length = 50;
		
		Command localCommand = checkForValidCommand(getPromptText());
		if (allowCommand) {
			renderIt(localCommand,rightClick);
		}
		
		
		if (getPromptText().startsWith("/move entity")) {
			move = true;
		}

		// ADD DOOR
	

//		checkForValidCommand(GameState.prompt.getPromptText());

		// PRE_STAGE********************************************************
		if (GameState.prompt.getPromptText().toLowerCase().startsWith("/use door")) {// DOOR

//			System.out.println(GameState.prompt.getPromptText().length());

			if ((GameState.prompt.getPromptText().length() > 10))
				length = Utils.parseInt(GameState.prompt.getPromptText().split("\\s+")[2]);
			makeDoor = true;
		}
		if (makeDoor) {// DOOR
			Renderer.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			Renderer.renderQuad(mouseX - handler.getGameCamera().getxOffset(),
					mouseY - handler.getGameCamera().getyOffset(), length, 20);
		}
		if (GameState.prompt.getPromptText().equalsIgnoreCase("/USE NPC")) {// NPC
			Renderer.setColor(1f, 1f, 1f, 0.5f);
			Renderer.renderSubImage(Assets.NPCsheet, mouseX - handler.getGameCamera().getxOffset(),
					mouseY - handler.getGameCamera().getyOffset(), Creature.DEFAULT_WIDTH, Creature.DEFAULT_HEIGHT,
					Assets.characterDown[0], 0.4f);
		}

		if (makeStatic) {
			alterSize(xc, yc);
		}
		while (Mouse.next()) {// MOUSE.NEXT()
			if (Mouse.getEventButtonState()) {

				// MAKING ENTITIES
				// NPC
				if (Mouse.getEventButton() == 1 && GameState.prompt.getPromptText().equalsIgnoreCase("/USE NPC")) {
					handler.getWorld().getEntityManager().addEntity(new NPC(handler, mouseX, mouseY, "test"));

				}

				// DOOR
				if (makeDoor && Mouse.getEventButton() == 1) {
					String[] doorData = null;
					if (GameState.prompt.getPromptText().length() >= 11)
						doorData = GameState.prompt.getPromptText().split("\\s+");
					if (doorData.length > 5) {
						Door d = ((doorData[5].equalsIgnoreCase("true"))
								? new Door(handler, mouseX, mouseY, Utils.parseInt(doorData[1 + 2]),
										Utils.parseInt(doorData[2 + 1]), Utils.parseInt(doorData[0 + 2]), 20,
										GameState.currentLocation,
										GameState.currentLevel.substring(0, GameState.currentLevel.length() - 6),
										doorData[3 + 2], doorData[4 + 2],
										handler.getWorld().getEntityManager().getPlayer(), true, "testDoor")
								: new Door(handler, mouseX, mouseY, Utils.parseInt(doorData[1 + 2]),
										Utils.parseInt(doorData[2 + 2]), 20, Utils.parseInt(doorData[0 + 2]),
										GameState.currentLocation,
										GameState.currentLevel.substring(0, GameState.currentLevel.length() - 6),
										doorData[3 + 2], doorData[4 + 2],
										handler.getWorld().getEntityManager().getPlayer(), false, "testDoor"));
						handler.getWorld().getEntityManager().addEntity(d);
						makeDoor = false;
						WorldSave.saveDoor(d);

					}
				}

//				if (makeStatic && Mouse.getEventButton() == 1) {
//					String tokens[] = GameState.prompt.getPromptText().split("\\s+");
//					handler.getWorld().getEntityManager().addEntity(
//							new StaticEntity(handler, mouseX, mouseY, sWidth, sHeight, tokens[tokens.length - 1]));
//					makeStatic = false;
//				}

				// ///
				// MOVE
				if (!hold || !place) {
					if (move) {
						if (Mouse.getEventButton() == 1) {
							if (!(handler.getWorld().getEntityManager().getEntity(mouseX, mouseY) == null)) {
								e = handler.getWorld().getEntityManager().getEntity(mouseX, mouseY);
								System.out.println("GOT THE ENTITY");
								this.hold = true;
								move = false;
								if (e instanceof StaticEntity) {
									staticEntityX = mouseX;
									staticEntityY = mouseY;
								}
							}
						}

					}
				}

				// PLACE
				if (place) {

					if (e.getClass().getSimpleName().equalsIgnoreCase("DOOR")) {
						Door d = (Door) e;
						// PREVIOUS DATA
						float tx = handler.getWorld().getEntityManager().getPlayer().getX();
						float ty = handler.getWorld().getEntityManager().getPlayer().getY();
						String oldPath = handler.getWorld().getWorldPath();

						if (handler.getWorld().getWorldPath().equalsIgnoreCase(d.worlds[0])) {// BRING CHANGE
																								// IN
																								// CURRENT
																								// FILE
							d.x1 = mouseX;
							d.y1 = mouseY;
							d.params.set(0, mouseX + "");
							d.params.set(1, mouseY + "");
							System.out.println("YEPPPPPPPP");
							// THEN SAVE IT
						}

						if (handler.getWorld().getWorldPath().equalsIgnoreCase(d.worlds[1])) {// BRING CHANGE
																								// IN
																								// CURRENT
																								// FILE
							d.x2 = mouseX;
							d.y2 = mouseY;
							d.params.set(2, mouseX + "");
							d.params.set(3, mouseY + "");
							// THEN SAVE IT
						}
						WorldSave.entitySave();

						// DEAL WITH THE NEXT FILE

						for (int i = 0; i < d.worlds.length; i++) {// GET THE
																	// FILE TO
																	// DEAL WITH
							if (d.worlds[i].equalsIgnoreCase(handler.getWorld().getWorldPath())) {
								handler.getWorld().setWorldPath(d.worlds[((i == 0) ? 1 : 0)]);
								handler.getWorld().loadWorld(d.worlds[((i == 0) ? 1 : 0)]);// new
																							// entity
																							// loaded
								break;
							}
						}

						for (Entity entity : handler.getWorld().getEntityManager().entities) {
							if ((entity.params.get(2).equalsIgnoreCase(e.params.get(2))
									&& entity.params.get(3).equalsIgnoreCase(e.params.get(3)))
									|| (entity.params.get(0).equalsIgnoreCase(e.params.get(0))
											&& entity.params.get(1).equalsIgnoreCase(e.params.get(1)))
											&& (entity.params.get(4).equals(e.params.get(4))
													&& entity.params.get(5).equals(e.params.get(5))
													&& entity.params.get(7).equals(e.params.get(7))
													&& entity.params.get(6).equals(e.params.get(6)))) {
								entity.params = e.params;
							}
						}
						WorldSave.entitySave();
						handler.getWorld().loadWorld(oldPath);
						handler.getWorld().getEntityManager().getPlayer().setX(tx);
						handler.getWorld().getEntityManager().getPlayer().setY(ty);
						place = false;
						hold = false;
					}

					else {// OTHER ENTITIES
						e.params.set(0, mouseX + "");
						e.params.set(1, mouseY + "");
						e.setX(mouseX);
						e.setY(mouseY);
						place = false;
						hold = false;
					}

				}

				// DELETE
				if (Mouse.getEventButton() == 1
						&& GameState.prompt.getPromptText().equalsIgnoreCase("/DELETE entity")) {
					Entity e1 = (handler.getWorld().getEntityManager().getEntity(mouseX, mouseY));
					if (e1 == null) {
						return;
					}
					if (e1.equals(handler.getWorld().getEntityManager().getPlayer())) {
						return;
					}
					float tx = handler.getWorld().getEntityManager().getPlayer().getX();
					float ty = handler.getWorld().getEntityManager().getPlayer().getY();
					String oldPath = handler.getWorld().getWorldPath();

					handler.getWorld().getEntityManager().entities.remove(e1);// DOOR IN PRESENT FILE REMOVED

					// REMOVE ENTITY DATA FROM OTHER FILE
					if (e1.getClass().getSimpleName().equalsIgnoreCase("Door")) {// REMOVE
																					// DOOR
																					// IN
																					// THE
																					// LINKED
																					// FILE
						Door d = (Door) e1;
						WorldSave.entitySave();

						// LOAD ENTITIES OF THAT FILE
						for (int i = 0; i < d.worlds.length; i++) {
							if (d.worlds[i].equalsIgnoreCase(handler.getWorld().getWorldPath())) {
								handler.getWorld().setWorldPath(d.worlds[((i == 0) ? 1 : 0)]);
								handler.getWorld().loadWorld(d.worlds[((i == 0) ? 1 : 0)]);
								break;
							}
						} // MAIN STUFF
						for (Entity e11 : handler.getWorld().getEntityManager().entities) {
							if (e11.params.equals(e1.params)) {
								handler.getWorld().getEntityManager().entities.remove(e11);
								break;
							}
						}
						// SAVE FILE
						WorldSave.entitySave();
						handler.getWorld().loadWorld(oldPath);
						handler.getWorld().getEntityManager().getPlayer().setX(tx);
						handler.getWorld().getEntityManager().getPlayer().setY(ty);
					}
				}
			}
		} // MOUSE.NEXT() OVER

		// HOLD
		if (this.hold) {

			if (e.getClass().getSimpleName().equalsIgnoreCase("NPC")) {
				Renderer.renderSubImage(Assets.NPCsheet, mouseX - handler.getGameCamera().getxOffset(),
						mouseY - handler.getGameCamera().getyOffset(), Creature.DEFAULT_WIDTH, Creature.DEFAULT_HEIGHT,
						Assets.characterDown[0], 0.6f);
				place = true;
				return;
			}

			if (e.getClass().getSimpleName().equalsIgnoreCase("DOOR")) {
				Renderer.setColor(0.8f, 0.8f, 0.8f, 0.5f);
				Renderer.renderQuad(mouseX - handler.getGameCamera().getxOffset(),
						mouseY - handler.getGameCamera().getyOffset(), (int) e.getWidth(), (int) e.getHeight());
				place = true;
				return;

			}
			if (e.getClass().getSimpleName().equalsIgnoreCase("StaticEntity")) {
				Entity e11 = handler.getWorld().getEntityManager().getEntity(staticEntityX, staticEntityY);
				StaticEntity s = null;
				if (e11 instanceof StaticEntity)
					s = (StaticEntity) e11;
				sWidth = (int) s.getWidth();
				sHeight = (int) s.getHeight();
				Renderer.renderSubImage(Assets.statics, mouseX - handler.getGameCamera().getxOffset(),
						mouseY - handler.getGameCamera().getyOffset(), sWidth, sHeight, s.imageData, 0.6f);
				place = true;
				return;

			}
		}

	}

	private void addEntity(Entity e0) {
		handler.getWorld().getEntityManager().addEntity(e0);
	}
	
	
	private boolean renderIt(Command comm0,boolean rightClicked0) {
		for (int i = 0; i < Assets.names.size(); i++) {
			if (inputCompleted) {
				String newCommand = getPromptText().replace(initialCommand, "");
				newCommand = newCommand.replace("/", "");
				newCommand=newCommand.trim();
				
				if(rightClicked0) {
					addEntity(new StaticEntity(handler, mouseX, mouseY, sWidth, sHeight,newCommand));
				}else {
					if(newCommand.equalsIgnoreCase(Assets.names.get(i))){
						Renderer.setColor(1f, 1f, 1f, 0.5f);
						Renderer.renderSubImage(Assets.statics, mouseX - handler.getGameCamera().getxOffset(),
								mouseY - handler.getGameCamera().getyOffset(), sWidth, sHeight, Assets.staticEntities.get(i),
								0.4f);
						return true;
					}
					
				}
			}

		}
		return false;
	}

}
