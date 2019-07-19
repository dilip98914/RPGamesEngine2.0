package dev.prince.rpgGameEngine.entities;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.gfx.Assets;
import dev.prince.rpgGameEngine.gfx.Renderer;

public class Pokemon extends Item{

	public Pokemon(Handler handler, float x, float y, float width, float height, String name) {
		super(handler, x, y, width, height, name);
		System.out.println("created");
//		isItem = true;

		
	}
	public void render() {
		System.out.println("logginf rendeirng");
		Renderer.renderSubImage(Assets.pokemonTexture, x-handler.getGameCamera().getxOffset(), y-handler.getGameCamera().getyOffset(), width, height, new float[] {
				0,0,1,1,1
		}, (byte) 1f);
	}
}