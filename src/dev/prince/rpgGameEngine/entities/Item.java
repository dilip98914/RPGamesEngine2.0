package dev.prince.rpgGameEngine.entities;


import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.gfx.Renderer;

public class Item extends Entity {
	
	protected float[] imageData;
	protected Texture texture;
	
	public Item(Handler handler, float x, float y, float width, float height,String name,
			float[] imageData0,Texture texture0) {
		super(handler, x, y, width, height,name);
		this.imageData = imageData0;
		this.texture=texture0;
		this.bounds = new Rectangle(0,0,(int)width,(int)height);
		isItem = true;
	}
	@Override
	public void tick() {
	}
	@Override
	public void render() {
		float xx= /*this.isItem?x:*/x-handler.getGameCamera().getxOffset();
		float yy=y-handler.getGameCamera().getyOffset();
		Renderer.renderSubImage(this.texture,xx,yy,width,height, imageData,(byte)1f);
	}
	public void render(float xx,float yy,float widtht,float heightt) {
		Renderer.renderSubImage(this.texture,xx,yy,widtht,heightt, imageData,(byte)1f);
	}
	
}
