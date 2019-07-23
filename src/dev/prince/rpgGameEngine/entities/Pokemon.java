package dev.prince.rpgGameEngine.entities;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.gfx.Assets;
import dev.prince.rpgGameEngine.gfx.Renderer;

public class Pokemon extends Item{
	int SHEET_WIDTH=16;//1024
	int SHEET_HEIGHT=8;//512
	public boolean interacted=false;

	public Pokemon(Handler handler, float x, float y, float width, float height,
			String name,float x0,float y0) {
		super(handler, x, y, width, height, name,null,null);
		this.texture=Assets.pokemons;
		this.bounds = new Rectangle(8,8,(int)width-16,(int)height-16);
		this.imageData=new float[] {
				x0/SHEET_WIDTH,y0/SHEET_HEIGHT,
				(x0+1)/SHEET_WIDTH,(y0+1)/SHEET_HEIGHT,1
		};
	}
	
	
	public Pokemon(Handler handler,String name,float x0,float y0) {
		super(handler, 0, 0, 0, 0, name,null,null);
		this.texture=Assets.pokemons;
		this.imageData=new float[] {
				x0/SHEET_WIDTH,y0/SHEET_HEIGHT,
				(x0+1)/SHEET_WIDTH,(y0+1)/SHEET_HEIGHT,1
		};
	}
	
	public void setCoords(float xt,float yt) {
		this.x=xt;
		this.y=yt;
	}
	
	public boolean checkInteractionWith(Entity e) {
		if(this.getCollisionBounds(0f, 0f).intersects(handler.getPlayer().getCollisionBounds(0f,0f))) {
			return true;
		}else {
			return false;			
		}
	}
	
	public void tick() {
		interacted=checkInteractionWith(handler.getPlayer());
	}
	
}