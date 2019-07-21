package dev.prince.rpgGameEngine.features;

import dev.prince.rpgGameEngine.entities.Item;

public class InventoryItem {
	public Item item;
	public int quantity;
	public boolean added=true;
	public InventoryItem(Item item,int quantity) {
		this.item=item;
		this.quantity=quantity;
	}
}
