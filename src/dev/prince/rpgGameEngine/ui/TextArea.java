package dev.prince.rpgGameEngine.ui;

import org.newdawn.slick.Color;

import dev.prince.rpgGameEngine.fonts.Fonts;
import dev.prince.rpgGameEngine.gfx.Renderer;

public class TextArea {
	
	private static int lineNo=1,length=0,xOffset = 10 ,yOffset = 2,lineHeight=4,fontHeight=10,totalSize=0,increaseConst=35;
	private static int width=35,height=30;
//	private static boolean hovering=false;
	
	public static void renderTextArea(float x , float y , String message ,Color textColor,int w,int h){
		width = w;
		height = h;
		//DETERMINE WIDTH AND HEIGHT OF QUAD
		if(w <0 || w <xOffset + Fonts.font.getWidth(message) + xOffset)
			width = xOffset + Fonts.font.getWidth(message) + xOffset;
		if(h <0 || h <yOffset + Fonts.font.getHeight(message) + yOffset)	
			height = yOffset + Fonts.font.getHeight(message) + yOffset;
		
		//RENDER QUAD
		Renderer.setColor(1, 1, 1, 1);
		Renderer.renderQuad(x, y, width,(int) height);
		//RENDER OULINE
		Renderer.setColor(0, 0, 0, 1);
		Renderer.renderOutlineOfQuad(x, y, width,height);
		
		//RENDER FONT
		Renderer.renderString(x+xOffset, y+yOffset, message, textColor, false);
		
	}
	
	public static void renderTextArea(float x , float y , String message ,Color textColor,float[] quadRGBA , float[] rectRGBA,boolean big,int w,int h){
		width = w;
		height = h;	
		//DETERMINE WIDTH AND HEIGHT OF QUAD
		if(w <0 || w <xOffset + Fonts.font.getWidth(message) + xOffset)
			width = xOffset + Fonts.font.getWidth(message) + xOffset;
		if(h <0 || h <yOffset + Fonts.font.getHeight(message) + yOffset)	
			height = yOffset + Fonts.font.getHeight(message) + yOffset;
		
		//RENDER QUAD
		Renderer.setColor(quadRGBA[0],quadRGBA[1],quadRGBA[2],quadRGBA[3]);
		Renderer.renderQuad(x, y,width,height);
		//RENDER OULINE
		Renderer.setColor(rectRGBA[0],rectRGBA[1],rectRGBA[2],rectRGBA[3]);
		Renderer.renderOutlineOfQuad(x, y, width,height);
		
		//RENDER FONT
		Renderer.renderString(x+xOffset, y+yOffset, message, textColor, big);

	}
	
	public static int getxOffset() {
		return xOffset;
	}

	public static int getyOffset() {
		return yOffset;
	}

	public static int getWidth(){
		return width;
	}
	
	public static int getHeight(){
		return height;
	}
	
}
