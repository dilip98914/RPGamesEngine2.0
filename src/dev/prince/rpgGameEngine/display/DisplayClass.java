package dev.prince.rpgGameEngine.display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class DisplayClass {
	public static void initGL(String title,int width,int height){
		
		//CREATE DISPLAY
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.setTitle(title);
			Display.setLocation(0, 0);
			//ResolutionManager.changeResolution(width, height, !Display.isFullscreen());
			Display.create();
			//DIsplay.c
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
			
		}
		
		//INIT OPENGL
		
		glEnable(GL_TEXTURE_2D);//ENABLE 2D TEXTURE
	    glShadeModel(GL_SMOOTH);        
	    glDisable(GL_DEPTH_TEST);
	    glDisable(GL_LIGHTING);   
	    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	    glClearColor(0f,0f,0f,0f);
	    glClearDepth(1);
//	    glEnable(GL13.GL_MULTISAMPLE);
		//ENABLE ALPHA BENDING MEANING ENABLETRANSPARENCY
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
		
        glViewport(0,0,width,height);
		
		glMatrixMode(GL_PROJECTION);
		
		glLoadIdentity();
		glOrtho(0,width,height,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
		
		
	}
}	
