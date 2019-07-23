package dev.prince.rpgGameEngine.states;

import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.ui.ClickListener;
import dev.prince.rpgGameEngine.ui.UIButton;
import dev.prince.rpgGameEngine.ui.UIManager;

public class CombatState extends State {
	private UIManager uiManager;
	private UIButton fight, bag, pokemon, run;
	int WIDTH = 90, HEIGHT = 50;
	int X1 = 10, X2 = 10 + WIDTH + 10;
	int Y1 = handler.getHeight() - 50 * 2 - 10 * 2, Y2 = Y1 + 50 + 10;

	public CombatState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		initButtons();
		uiManager.addUIObject(fight);
		uiManager.addUIObject(pokemon);
		uiManager.addUIObject(bag);
		uiManager.addUIObject(run);


	}

	private void initButtons() {
		fight = new UIButton(handler, X1, Y1, WIDTH, HEIGHT, "FIGHT", Color.black, 1f, 1f, 1f, true, 5,
				new ClickListener() {
					public void onClick() {
						System.out.println("FIGHT");
					}
				});
		bag = new UIButton(handler, X2, Y1, WIDTH, HEIGHT, "BAG", Color.black, 1f, 1f, 1f, true, 5,
				new ClickListener() {
					public void onClick() {
						System.out.println("BAG");
					}
				});
		pokemon = new UIButton(handler, X1, Y2, WIDTH, HEIGHT, "POKE", Color.black, 1f, 1f, 1f, true, 5,
				new ClickListener() {
					public void onClick() {
						System.out.println("POKEMON");
					}
				});
		run = new UIButton(handler, X2, Y2, WIDTH, HEIGHT, "RUN", Color.black, 1f, 1f, 1f, true, 5,
				new ClickListener() {
					public void onClick() {
						System.out.println("RUN");
					}
				});

	}

	public void tick() {
		uiManager.tick();

	}

	public void render() {
		Renderer.setColor(0.3f, 0.3f, 0.3f, 1);
		Renderer.renderQuad(0, 0, handler.getWidth(), handler.getHeight());
		uiManager.render();

	}
}
