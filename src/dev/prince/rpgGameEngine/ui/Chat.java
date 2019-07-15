package dev.prince.rpgGameEngine.ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.gfx.Renderer;

public class Chat extends UIObject {

	private ArrayList<String> chats;

	private int focusedHeight, unfocusedHeight;
	private int xPadding, yPadding, ySpacing;
	private int chatHistoryLen;
	private boolean focused = false;

	public Chat(Handler handler, float x, float y, int width, int height,
			int xPadding, int yPadding, int ySpacing, int chatHistoryLen) {
		super(handler, x, y, width, height);

		this.chats = new ArrayList<String>();

		this.focusedHeight = height;
		this.unfocusedHeight = height / 2;

		this.xPadding = xPadding;
		this.yPadding = yPadding;
		this.ySpacing = ySpacing;
		this.chatHistoryLen = chatHistoryLen;

		chats.add("+============================+");
		chats.add("||         WELCOME! to RPGamesEngine         ||");
		chats.add("+============================+");
	}

	@Override
	public void tick() {
		if (chats.size() > chatHistoryLen) {
			chats.remove(0);
		}
	}

	@Override
	public void render() {

		Renderer.setColor(0, 0, 0, 1);
		Renderer.renderOutlineOfQuad(x, (focused) ? y : y + focusedHeight
				- unfocusedHeight, width, (focused) ? focusedHeight
				: unfocusedHeight);
		Renderer.setColor(1, 1, 1, (focused) ? 0.5f : 0.2f);
		Renderer.renderQuad(x, (focused) ? y : y + focusedHeight
				- unfocusedHeight, width, (focused) ? focusedHeight
				: unfocusedHeight);

		for (int i = chats.size() - 1; i >= 0; i--) {
			int x = (int) (this.x + xPadding);
			int y = (int) (this.y + focusedHeight - yPadding - (chats.size() - i)
					* (Fonts.font.getHeight("ABC") + ySpacing));

			if (focused) {
				if (y < this.y)
					continue;

			} else {
				if (y < this.y + focusedHeight - unfocusedHeight)
					continue;
			}
			Renderer.renderString(x, y, chats.get(i), Color.black, false);

		}

	}

	public void addText(String str){
		while(Fonts.font.getWidth(str)>width - 2*xPadding){
			chats.add(getFittableString(str));
			str = str.substring(chats.get(chats.size()-1).length());
		}
		chats.add(str);
	}

	public String getFittableString(String str) {
		int len = str.length();
		for (int j = len; j >= 0; j--) {
			if (Fonts.font.getWidth(str.substring(0, j)) <= width - 2
					* xPadding && str.charAt(j) == ' '){
				len = j;
				break;
			}
		}
		return str.substring(0,len);
	}

	@Override
	public void onClick() {

	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public ArrayList<String> getChats() {
		return chats;
	}

}
