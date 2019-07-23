package dev.prince.rpgGameEngine.features;

import java.util.ArrayList;

public class Command {
	public String command;
	public boolean value;
	
	public static ArrayList<Command> commands=new ArrayList<Command>();
	
	public Command(String command) {
		this.command=command;
		this.value=false;
	}
	
	public static String getCommandName(int index){
		Command cc=commands.get(index);
		return cc.command;
	}
	
	public static boolean getCommandValue(int index){
		Command cc=commands.get(index);
		return cc.value;
	}
	
	public static Command searchCommand(String name){
		for(int i=0;i<commands.size();i++) {
			Command c0=commands.get(i);
			if(c0.command.contains(name)) {
				return c0;
			}
		}
		return null;
	}
	
	public static void addCommand(Command c) {
		commands.add(c);
	}
}
