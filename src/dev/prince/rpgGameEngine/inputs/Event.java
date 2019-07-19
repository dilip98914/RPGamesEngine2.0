package dev.prince.rpgGameEngine.inputs;

public class Event {
	public int keyValue;
	public String keyName;
	
	public void set(String key,int val) {
		this.keyName=key;
		this.keyValue=val;
	}
	
}
