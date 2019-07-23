package dev.prince.rpgGameEngine.inputs;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dev.prince.rpgGameEngine.Handler;

public class EventManager {
	private Handler handler;
	public static String letter="";
	public static int value;
	
	public static int mouse_button;
	
	
	
	public EventManager(Handler handler) {
		this.handler=handler;
	}
	
	public static String getInput(boolean letter0){
		if(letter0)
			return EventManager.letter;
		else
			return String.valueOf(EventManager.value);
	}
	
	public static int getMouseInput() {
		return mouse_button;
	}
	
//	public static String getInput0(){
//		if(letter0)
//			return EventManager.letter;
//		else
//			return String.valueOf(EventManager.value);
//	}
//	
	
	private static void setInput() {
		if((Keyboard.getEventCharacter()) != 0) {
			letter=String.valueOf(Keyboard.getEventCharacter());
	        value = Keyboard.getEventKey();
		}
	}
	
	
	private static void setMouseInput() {
		if(Mouse.getEventButtonState()) {
			mouse_button=Mouse.getEventButton();
		}
	}
	
	public static void getEvents(){
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()){
				setInput();////
			}
		}
		while(Mouse.next()) {
			setMouseInput();
		}
	}
	
	public static void checkEvents() {
		
	}

	public static Event[] emitEvents() {
		return null;
	}
	
}

