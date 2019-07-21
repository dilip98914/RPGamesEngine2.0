package dev.prince.rpgGameEngine.features;

import dev.prince.rpgGameEngine.entities.Item;

public class InventoryItem {
	public Item item;
	public int quantity;
//	public boolean removedFromInventory=false;
	public InventoryItem(Item item,int quantity) {
		this.item=item;
		this.quantity=quantity;
	}
}
