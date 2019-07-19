package dev.prince.rpgGameEngine.ui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.inputs.EventManager;
import dev.prince.rpgGameEngine.states.State;
import dev.prince.rpgGameEngine.utils.Utils;

public class UIPrompt extends UIObject{
	
	public boolean focused=false;
	public int pointerPos =0;
	
	private String text="_";
	
	
	public UIPrompt(Handler handler, float x, float y) {
		super(handler, x, y,TextArea.getWidth(), TextArea.getHeight());
		this.bounds=new Rectangle((int)x,(int)y,(int)400,(int)height);
	}

	@Override
	public void tick() {
		//BACKSPACE WORKING
		
		if(width < 400)
			width = 400;//2*TextArea.getxOffset() + Fonts.font.getWidth("------------------------");

		if(width <0 || width < 2*TextArea.getxOffset() + Fonts.font.getWidth(text))
			width = 2*TextArea.getxOffset() + Fonts.font.getWidth(text);

		bounds.setWidth(width);
		bounds.setHeight(height);
		onMouseMove();//check hover
		
		onMouseClick();//check click (focus when clicked)
		focusPrompt();//focus when "/" pressed
		
//		handler.getChat().setFocused(focused);
		if(focused){
			
			
			String textPrev = text.substring(0, pointerPos);
			String textAfter ="";
			if(pointerPos < text.length() -1)
				textAfter = text.substring(pointerPos+1,text.length());
			
			if(Utils.parseInt(EventManager.getInput(false)) == Keyboard.KEY_BACK){
				if(textPrev.length()>=1){
					textPrev = textPrev.substring(0, textPrev.length()-1);
					pointerPos--;
				}
			}
			//ENTERING TEXT
			
			int key = Utils.parseInt(EventManager.getInput(false));
			if(key == Keyboard.KEY_RETURN){
				textPrev="";
				textAfter="";
				text="";
				pointerPos =0;
//				handler.getChat().getChats().add("Can't regonize the command! Did you try right clicking?");
			}
			if(key != Keyboard.KEY_BACK && key != Keyboard.KEY_RETURN){
					String a =EventManager.getInput(true); 
					if(a!=""){
						textPrev+=a;
						pointerPos++;
					}
			}
			if(key == Keyboard.KEY_SPACE ){
				textPrev += " ";
				pointerPos++;
			}
			
			if(key == Keyboard.KEY_LEFT){
				pointerPos--;
				if(pointerPos<0)
					pointerPos=0;
			}else if(key == Keyboard.KEY_RIGHT){
				pointerPos++;
				if(pointerPos > textPrev.length() + textAfter.length())
					pointerPos = textPrev.length() + textAfter.length();
			}
			
			text = textPrev +  textAfter;
			textPrev = text.substring(0, pointerPos);
			textAfter ="";
			if(pointerPos < text.length())
				textAfter = text.substring(pointerPos,text.length());
			
			text = textPrev + "_" + textAfter;
			
		}
		unFocusprompt();
	}

	
	@Override
	public void render() {
		if(focused)
			TextArea.renderTextArea(x, y, text, Color.black, new float[]{1f,1f,1f,0.7f}, new float[]{0,0,0,0.8f},false,width,-1);	
		else
			TextArea.renderTextArea(x, y, text, Color.black, new float[]{1f,1f,1f,0.3f}, new float[]{0,0,0,0.4f},false,width,-1);	
	}

	@Override
	public void onClick() {
		focused=true;
	}
	
	public void focusPrompt(){
		if(Utils.parseInt(EventManager.getInput(false)) == Keyboard.KEY_SLASH)
			focused = true;
	}
	
	public void unFocusprompt(){
		if(!hovering &&( Mouse.isButtonDown(0) ||Mouse.isButtonDown(1) ))
			focused = false;
		else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			focused = false;
			State.setState(handler.getGameState());
		}
	}
	
	//i guess api to use outside?
	public String getPromptText(){
		String textPrev = text.substring(0, pointerPos);
		String textAfter ="";
		if(pointerPos < text.length() -1)
			textAfter = text.substring(pointerPos+1,text.length());
		String str  = textPrev.concat(textAfter);
		str = str.trim().replaceAll(" +", " ");
		return (str);
	}
	
	public void setPromptText(String message){
		text="";
		text=message;
	}
	public void appendText(String message){
		String textPrev = text.substring(0, pointerPos) + message;
		String textAfter ="";
		if(pointerPos < text.length() -1)
			textAfter = text.substring(pointerPos+1,text.length());
		text = textPrev+ textAfter;
	}
	
}	
