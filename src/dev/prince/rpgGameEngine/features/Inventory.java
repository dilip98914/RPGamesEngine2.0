package dev.prince.rpgGameEngine.features;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.entities.EntityManager;
import dev.prince.rpgGameEngine.entities.Pokemon;
import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.gfx.Renderer;
import dev.prince.rpgGameEngine.inputs.Event;
import dev.prince.rpgGameEngine.inputs.EventManager;
import dev.prince.rpgGameEngine.ui.TextArea;

public class Inventory {

	private Handler handler;
	private String title = "Inventory";
	private int selected = 0, displayQuantity = 12, moveMagnitude = 0, visibleLength = 0;
	private int x, y, width, height;
	public static final int X_OFFSET = 15, Y_OFFSET = 10, MAX_QUANTITY = 25;

	private float[] selectedColor;

	public ArrayList<InventoryItem> items;

	public InventoryItem currentItem;
	public int index = 0;
	public boolean throwEvent=false;
//	public boolean throwCurretItem=false;

	public Inventory(Handler handler) {
		this.handler = handler;
		// DEFINE WIDTH AND HEIGHT
		width = handler.getWidth() - 2 * (10 * X_OFFSET);
		height = Y_OFFSET + Fonts.font.getHeight("ANYTHING") * displayQuantity + displayQuantity * Y_OFFSET + Y_OFFSET;
		x = handler.getWidth() / 2 - width / 2;
		y = handler.getHeight() / 2 - height / 2;

		index = 0;
		
		// DEFINE COLOR FLOAT VALUES
		selectedColor = new float[] { 1, 0, 0, 0.8f };
		items=new ArrayList<InventoryItem>();
	}

	/* still neater way to handle events in each file */
	public void getEvents() {
//			System.out.println(EventManager.getInput(true));
		String key = EventManager.getInput(true);
		if (key.contains("s")) {
			index++;
//			index=(index>=items.size())?0:1;
			if (index >= items.size()) {
				index = 0;
			}
		} else if (key.contains("w")) {
			index--;
			if (index < 0) {
				index = items.size() - 1;
			}
		}else if (EventManager.value == Keyboard.KEY_Q) {
//			index--;
//			if (index < 0) {
//				index = items.size() - 1;
//			}
			System.out.println("caleedcsc");
			throwEvent=true;
			EventManager.value=0;
		}  
	}

	
	public void tick(Event[] events) {
		getEvents();
		if(items.size()>0 && index>=0) {
//			System.out.println(index+" index");
		}
		currentItem = items.get(0);
		System.out.println(currentItem.item.name+"curert ietm");

//		this.inventoryIterator=handler.ge;

		// Set Values
//		visibleLength=0;
//		
//		for(int i=0;i<MAX_QUANTITY;i++){
//			if(items[i] == null)
//				break;
//			visibleLength++;
//		}

//		if(selected>displayQuantity/2){
//			moveMagnitude=selected-displayQuantity/2-1;
//		}

		// System.out.println("Visible length is: "+visibleLength);
		// NAVIGATION
//		if(EventManager.value == Keyboard.KEY_DOWN){
//			if(selected<visibleLength-1)
//				selected++;
//			EventManager.value=0;
//		}else if(EventManager.value == Keyboard.KEY_UP){
//			if(selected >= 1)
//				selected--;
//			EventManager.value=0;
//		}

		// System.out.println("SELECETED VALUE IS: "+selected);
//		while (inventoryIterator.hasNext()) {
//			InventoryItem e = inventoryIterator.next();
//		}
//		currentItem = items.get(index);
		handler.getWorld().getEntityManager().getPlayer().zIndex = 1;

	}

	public void render() {
		TextArea.renderTextArea(handler.getWidth() / 2 - Fonts.fontBig.getWidth(title) / 2,
				y - TextArea.getyOffset() * 2 - Fonts.fontBig.getHeight("I"), title, Color.white,
				new float[] { 0, 0, 0, 0 }, new float[] { 0, 0, 0, 0 }, true, -1, -1);

		Renderer.setColor(0.6f, 0.7f, 0.8f, 0.7f);
		Renderer.renderQuad(x, y, width, height);
		Renderer.setColor(0.6f, 0.7f, 0.8f, 1f);
		Renderer.renderOutlineOfQuad(x, y, width, height);
//		
//		//NORMAL RENDER
//		for(int i=
//				((moveMagnitude<=selected)?moveMagnitude:selected)
//				;i<
//				((displayQuantity+moveMagnitude<=maxQuantity)?displayQuantity+moveMagnitude:maxQuantity)
//				;i++){
//			if(items[i]!=null){					
//				if(i==selected){
//					TextArea.renderTextArea(x+xOffset, y+yOffset+(i-moveMagnitude)*Fonts.font.getHeight("A")+(i-moveMagnitude)*yOffset, items[i] + " x " + quantities[i], Color.black,selectedColor,new float[]{0,0,0,1},false,-1,-1);
//					continue;
//				}
//				TextArea.renderTextArea(x + xOffset, y + yOffset + (i-moveMagnitude)*Fonts.font.getHeight("A") + (i-moveMagnitude)*yOffset, items[i] + " x " + quantities[i], Color.black,-1,-1);
//			}
//		}
		int constX1 = 10;
		int constX2 = width / 4 + 10;
		int constY = 10;
		int yOff = 0;
		float alpha = 0.6f;
		
		for(int i=0;i<items.size();i++) {
			InventoryItem item=items.get(i);
			
			if (item == currentItem) {
				Renderer.setColor(5.5f, 0.5f, 0.7f, 1f);
			} else {
				Renderer.setColor(0.5f, 0.5f, 0.7f, 0.6f);
			}

			Renderer.renderQuad(x + constX1, (y + yOff), 200, 39);

			item.item.render(x + constX1, (y + yOff) + constY, 40, 40);
			Renderer.renderString(x + constX2 / 2, (y + yOff) + constY, "-", Color.white, true);
//			Renderer.renderString(x + constX2, (y + yOff) + constY, String.valueOf(item.quantity), Color.white, true);
			Renderer.renderString(x + constX2, (y + yOff) + constY, String.valueOf(i), Color.white, true);
			
			yOff += 40;
		}

	}

//	public  void addItem(String item , String quantity){
//		for(int i=0;i<items.length;i++){
//			if(items[i]==null){
//				items[i] = item;
//				quantities[i]=quantity;
//				return;
//			}
//		}
//	}

}
