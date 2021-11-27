/*************************************************************** *
 * file: PlayerModel.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Final Checkpoint  
 * date last modified: 11/26/2021 * 
 * purpose: This file uses chunks code to make a player model
 * ****************************************************************/ 
package finalproject;

import static finalproject.Chunks.CHUNK_SIZE;
import static finalproject.Chunks.CUBE_LENGTH;
import static finalproject.Chunks.createTexCube;
import static finalproject.Lake.CUBE_LENGTH;
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Sayem
 */
public class PlayerModel extends Chunks {
    
    static final int CUBE_LENGTH = 2;
    private int PLAYER_HEIGHT;
    private int PLAYER_AREA; 
    private int PLAYER;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    private Texture texture;
    public Vector3f position;
    
    
    PlayerModel(int startX, int startY, int startZ) {
       super(startX, startY, startZ);
        try{
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e){
            System.out.print("error");
        }
    
        PLAYER_HEIGHT = 1;
        PLAYER_AREA = 1;
        PLAYER = 1;
        VBOTextureHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        position = new Vector3f(startX,startY,startZ);
        rebuildMesh(position);
    }
   
    public void render(){
     glPushMatrix();
         glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
         glVertexPointer(3, GL_FLOAT, 0, 0L);
         glTranslatef(position.x,position.y,position.z);
         glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
         glColorPointer(3, GL_FLOAT, 0, 0L);
         glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
         glBindTexture(GL_TEXTURE_2D, 1);
         glTexCoordPointer(2, GL_FLOAT,0,0L);
         glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
     glPopMatrix();
 }

    private void rebuildMesh(Vector3f position) {
     FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((PLAYER_HEIGHT * PLAYER_AREA * PLAYER) * 6 * 12);
     FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((PLAYER_HEIGHT * PLAYER_AREA * PLAYER) * 6 * 12);
     FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((PLAYER_HEIGHT * PLAYER_AREA * PLAYER) * 6 * 12);

     Block global = new Block(Block.BlockType.Water); // doing this since color requires block. Global since all water blocks are same.

     for(int x = 0; x < PLAYER_HEIGHT; x += 1){
         for(int  z = 0; z < PLAYER_AREA; z += 1){
                 VertexPositionData.put(createCube((float)(position.x + x * CUBE_LENGTH), (position.y - z * CUBE_LENGTH),(float)(position.z + z * CUBE_LENGTH)));
                 VertexColorData.put(createCubeVertexCol(getCubeColor(global)));
                 VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Water));
             }
         }
     

     VertexTextureData.flip();
     VertexColorData.flip();
     VertexPositionData.flip();
     glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
     glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
     glBindBuffer(GL_ARRAY_BUFFER,0);

     glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
     glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
     glBindBuffer(GL_ARRAY_BUFFER, 0);

     glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
     glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
     glBindBuffer(GL_ARRAY_BUFFER, 0);

 }

 private static float[] createCube(float x, float y, float z){
     int offset = CUBE_LENGTH / 2;
     return new float[]{
         //TOP QUAD
         x + offset, y + offset, z,
         x - offset, y + offset, z,
         x - offset, y + offset, z - CUBE_LENGTH,
         x + offset, y + offset, z - CUBE_LENGTH,
         //Bottom Quad
         x + offset, y - offset, z - CUBE_LENGTH,
         x - offset, y - offset, z - CUBE_LENGTH,
         x - offset, y - offset, z, 
         x + offset, y - offset, z,
         //FRONT QUAD
         x + offset, y + offset, z - CUBE_LENGTH,
         x - offset, y + offset, z - CUBE_LENGTH,
         x - offset, y - offset, z - CUBE_LENGTH,
         x + offset, y - offset, z - CUBE_LENGTH,
         //Back Quad
         x + offset, y - offset, z, 
         x - offset, y - offset, z, 
         x - offset, y + offset, z, 
         x + offset, y + offset, z,
         //Left Quad
         x - offset, y + offset, z - CUBE_LENGTH,
         x - offset, y + offset, z, 
         x - offset, y - offset, z, 
         x - offset, y - offset, z - CUBE_LENGTH,
         //Right Quad
         x + offset, y + offset, z, 
         x + offset, y + offset, z - CUBE_LENGTH,
         x + offset, y - offset, z - CUBE_LENGTH,
         x + offset, y - offset, z 

     };
 }

 private float[] getCubeColor(Block block){
     return new float[]{1,1,1};
 }

 private float[] createCubeVertexCol(float[] CubeColorArray){
     float [] cubeColors = new float[CubeColorArray.length * 4 * 6];
     for(int i = 0; i < cubeColors.length; i++){
         cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
     }
     return cubeColors;
 }

 public static float[] createTexCube(float x, float y, Block.BlockType type){
     float offset = (1024f/16)/1024f;
     return new float[]{
                 //bottom quad 
//bottom quad 
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0, 
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    //top quad
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0, 
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    //front quad
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0, 
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    //back quad
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                    // LEFT QUAD  
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0, 
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    // RIGHT QUAD
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0, 
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
     };
 }

}

