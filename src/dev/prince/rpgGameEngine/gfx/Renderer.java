package dev.prince.rpgGameEngine.gfx;


import static org.lwjgl.opengl.ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import dev.prince.rpgGameEngine.Handler;
import dev.prince.rpgGameEngine.fonts.Fonts;

public class Renderer {
	
	public static int displayListHandler,vertexCount,vertexSize,colorSize;
	
	public static int vboID;
	
	public static FloatBuffer totalData,tData;
	public static float r=0f,g=0f,b=0f,a=0f;
	
	public static int texture ;
	
	public static Handler handler;
	
	public Renderer(){
	}
	
	public static void init(Handler handler1){
		handler=handler1;
		displayListHandler = glGenLists(1);
		vertexCount = 4;
		vertexSize=2;
		colorSize=4;
		totalData = BufferUtils.createFloatBuffer(vertexCount*vertexSize+vertexCount*colorSize+vertexCount*vertexSize);
		tData = BufferUtils.createFloatBuffer(vertexCount*vertexSize+vertexCount*colorSize);
		
		vboID = generateVBOID();
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER,vboID);

		
	}
	
	public static int generateVBOID(){
		return glGenBuffers();
	}
	
	public static void bufferData(int id,FloatBuffer buffer){//INITIALIZATION OF
		glBufferData(GL_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
	}
	
	public static void setColor(float R,float G , float B,float A){
		glColor4f(R,G,B,A);
		r=R;
		g=G;
		b=B;
		a=A;
	}
	
	public static void renderString(float x , float y ,String str,Color color,boolean big){
		if(!GL11.glIsEnabled(GL_TEXTURE_2D))
		glEnable(GL_TEXTURE_2D);
		Fonts.drawString(x, y, str, color,big);
	}
	
	public static void renderLine(float x1 , float y1,float x2,float y2){
		glNewList(displayListHandler,GL_COMPILE);
		glBegin(GL_LINES);
			glVertex2f(x1,y1);
			glVertex2f(x2,y2);
		glEnd();
		glEndList();
		glCallList(displayListHandler);
		
	}
	
	public static void renderQuad(float x , float y , int width,int height){
		if(GL11.glIsEnabled(GL_TEXTURE_2D))

			glDisable(GL_TEXTURE_2D);
		//glEnable(GL_BLEND);
	
		tData.put(new float[]{
				x,y,				r,g,b,a,
				x+width,y,			r,g,b,a,
				x+width,y+height,	r,g,b,a,
				x,y+height,			r,g,b,a
		});
		tData.flip();
		//MAKE VBOs		
		bufferData(vboID,tData);//vbo binded and data buffered
		//COLOR
		glVertexPointer(vertexSize,GL_FLOAT,6*4,0L);
		glColorPointer(colorSize,GL_FLOAT,6*4,2*4);		
		//ENABLEs
		
		glDrawArrays(GL_QUADS,0,vertexCount);
	}
	
	public static void renderOutlineOfQuad(float x , float y , int width,int height){
		if(GL11.glIsEnabled(GL_TEXTURE_2D))

			glDisable(GL_TEXTURE_2D);
		//glEnable(GL_BLEND);
		
		glNewList(displayListHandler,GL_COMPILE);
		glBegin(GL_LINE_STRIP);
			glVertex2f(x,y);
			glVertex2f(x+width,y);
			glVertex2f(x+width,y+height);
			glVertex2f(x,y+height);
		glEnd();
		glBegin(GL_LINE_STRIP);
			glVertex2f(x,y);
			glVertex2f(x,y+height);
		glEnd();
		glEndList();

		glCallList(displayListHandler);
		

	}
	
	public static void renderImage(Texture texture,float x , float y , float width,float height,float alpha){
	
		texture.bind();
		
		totalData.put(new float[]{
				x,y,				1,1,1,alpha,	0,0,//delta1
				x+width,y,			1,1,1,alpha,	1,0,//delta2
				x+width,y+height,	1,1,1,alpha,	1,//delta3
													1,
				x,y+height,			1,1,1,alpha,	0,1
		});
		
		totalData.flip();
		
		bufferData(vboID,totalData);
		
		//BIND VBOs
		//VERTEX
		glVertexPointer(vertexSize,GL_FLOAT,8*4,0L);
				
		//COLOR
		glColorPointer(colorSize,GL_FLOAT,8*4,2*4);
				
		//TEXTURE
		GL11.glTexCoordPointer(vertexSize,GL_FLOAT,8*4,6*4);		
		
		glDrawArrays(GL_QUADS,0,vertexCount);

		
	}
	
	
	
	public static void renderSubImage(Texture texture,float x , float y,float width,float height,float[] data,float alpha ){//,float x1,float y1, int width,int height,float alpha){
	
		texture.bind();
		totalData.put(new float[]{
		x,y,				1,1,1,alpha,	data[0],data[1],//delta1
		x+width,y,			1,1,1,alpha,	data[2],data[1],//delta2
		x+width,y+height,	1,1,1,alpha,	data[2],data[3],
											
		x,y+height,			1,1,1,alpha,	data[0],data[3]
		});
		
		totalData.flip();
		bufferData(vboID,totalData);
		//BIND VBOs
		//VERTEX
		glVertexPointer(vertexSize,GL_FLOAT,8*4,0L);
		
		//COLOR
		glColorPointer(colorSize,GL_FLOAT,8*4,2*4);
		
		//TEXTURE
		GL11.glTexCoordPointer(vertexSize,GL_FLOAT,8*4,6*4);
		
		glDrawArrays(GL_QUADS,0,vertexCount);	
			
	}
	
	public static void destroy(){
		glDeleteLists(displayListHandler,1);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER_ARB,0);
		glDeleteBuffers(vboID);
	}
	
}
