package dev.prince.rpgGameEngine.inputs;

import org.lwjgl.input.Keyboard;

import dev.prince.rpgGameEngine.Handler;

public class EventManager {
	private Handler handler;
	public static String letter="";
	public static int value;

	
	public EventManager(Handler handler) {
		this.handler=handler;
	}
	
	public static String getInput(boolean letter0){
		if(letter0)
			return EventManager.letter;
		else
			return String.valueOf(EventManager.value);
	}
	
	private static void setInput() {
		if((Keyboard.getEventCharacter()) != 0) {
			letter=String.valueOf(Keyboard.getEventCharacter());
	        value = Keyboard.getEventKey();
		}
	}
	
	
	public static void getEvents(){
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()){
				setInput();////
			}
		}
	}
	
	public static void checkEvents() {
		
	}
	
}

