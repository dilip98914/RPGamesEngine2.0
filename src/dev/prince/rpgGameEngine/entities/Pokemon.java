package dev.prince.rpgGameEngine.entities;

import org.newdawn.slick.opengl.Texture;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.gfx.Assets;
import dev.prince.rpgGameEngine.gfx.Renderer;

public class Pokemon extends Item{

	public Pokemon(Handler handler, float x, float y, float width, float height,
			String name,float[] imageData0,Texture texture0) {
		super(handler, x, y, width, height, name,imageData0,texture0);
	}
	public Pokemon(Handler handler,String name,float[] imageData0,Texture texture0) {
		super(handler, 0, 0, 0, 0, name,imageData0,texture0);
	}
}