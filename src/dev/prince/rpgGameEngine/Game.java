package dev.prince.rpgGameEngine;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.SoundStore;

import dev.prince.rpgGameEngine.display.DisplayClass;
import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.gfx.Assets;
import dev.prince.rpgGameEngine.gfx.GameCamera;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.inputs.EventManager;
import dev.prince.rpgGameEngine.inputs.MouseManager;
import dev.prince.rpgGameEngine.net.GameClient;
import dev.prince.rpgGameEngine.net.GameServer;
import dev.prince.rpgGameEngine.net.packets.Packet00Login;
import dev.prince.rpgGameEngine.net.packets.Packet01Disconnect;
import dev.prince.rpgGameEngine.sounds.SoundEffects;
import dev.prince.rpgGameEngine.states.CreditState;
import dev.prince.rpgGameEngine.states.GameState;
import dev.prince.rpgGameEngine.states.MenuState;
import dev.prince.rpgGameEngine.states.State;
import dev.prince.rpgGameEngine.ui.TextArea;
import dev.prince.rpgGameEngine.utils.Utils;


public class Game{
	private String title;
	private int width,height;
	public static int UPS=60,FPS=0;
	public int Frames=0,Updates=0;
	public static boolean joinServer=false,isServer=false;
	public boolean vsync = false,start=false;

	private Handler handler;

	private GameState gameState;
	private MenuState menuState;
	private CreditState creditState;
	private MouseManager mouseManager;
	
	private GameCamera camera;

	Assets assets;
	private SoundEffects soundEffects;
	
	GameClient client;
	GameServer server;
	
	public Game(String title,int width,int height){
		this.title=title;
		this.width=width;
		this.height=height;
	}
	public void init(){
		handler=new Handler(this);
		assets = new Assets();
		assets.init();//BIG DEAL
		Renderer.init(handler);
		Fonts.init();
		Utils.init();
		soundEffects = new SoundEffects();;
		soundEffects.init();
		mouseManager = new MouseManager(handler);
		camera=new GameCamera(handler);
		gameState=new GameState(handler);
		menuState = new MenuState(handler);
		creditState = new CreditState(handler);
		State.setState(gameState);
	}

	public void start(){
		DisplayClass.initGL(title,width,height);
		init();

		//FPS SET UP
		long lastTime = System.nanoTime();
		long now;
		double delta=0;
		double nsPerTick = 1000000000/UPS;
		long timer=0;
		int frames=0,ticks=0;
		 
		server = new GameServer(handler);
		GameServer.serverName="server";
		server.start();
		isServer=true;
		Packet00Login login;
		client = new GameClient(handler,"127.0.0.1");
		client.start();
		login = new Packet00Login("client");
		login.sendToServer(client);
		joinServer=true;
		isServer=false;
		
		start=true;
		while(!Display.isCloseRequested() && start){
			now=System.nanoTime();
			delta+=(now-lastTime)/nsPerTick;
			timer+=(now-lastTime);
			lastTime=now;
			if(delta>=1){
				tick();
				ticks++;
				delta--;
			}

			render();
			frames++;

			if(timer >= 1000000000){
				Game.FPS= Frames;
				Display.setTitle(title + " @ "+frames+"-FPS");
				Frames=frames;
				Updates = ticks;
				timer=0;
				frames=0;
				ticks=0;
			}
			Display.update();
		}
		if(joinServer){
			disconnectMP();
		}
		closeProgram();
		
	}

	public void tick(){
		mouseManager.tick();
		EventManager.getEvents();
		if(State.getState() !=null){
			State.getState().tick();
		}
		EventManager.letter="";
		EventManager.value=0;
	}
	
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 0);
//		Color.white.bind();
		EventManager.getEvents();
		if(State.getState() !=null){
			State.getState().render();
		}		
//		TextArea.renderTextArea(handler.getWidth()-200, 33-25,"FPS: "+Frames+" UPS: "+Updates, Color.black,-1,-1);
//		TextArea.renderTextArea(handler.getWidth()-200, 33,"Vsync: "+vsync, Color.black,-1,-1);
		SoundStore.get().poll(0);
			
	}
	public static void closeProgram(){	
		Renderer.destroy();
		SoundEffects.destroy();
		Display.destroy();
		System.exit(0);
	}
	
	public  void disconnectMP(){
		Packet01Disconnect disconnect = new Packet01Disconnect(handler.getPlayerX(),handler.getPlayerY(),handler.getPlayer().getUsername());
		disconnect.sendToServer(client);
		disconnect=null;
	}
	///////////////GETTERS AND SETTERS///////////////////
	public int getWidth() {
		return width;
	}
	public void setVsync(boolean vsync) {
		this.vsync = vsync;
	}
	public int getHeight() {
		return height;
	}
	public int getFrames() {
		return Frames;
	}
	public boolean isVsync() {
		return vsync;
	}
	public GameState getGameState() {
		return gameState;
	}
	public MenuState getMenuState() {
		return menuState;
	}
	public CreditState getCreditState() {
		return creditState;
	}
	public GameCamera getCamera() {
		return camera;
	}
	public SoundEffects getSoundEffects() {
		return soundEffects;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public MouseManager getMouseManager() {
		return mouseManager;
	}
	public GameClient getSocketClient() {
		return client;
	}
	public GameServer getSocketServer() {
		return server;
	}

}
