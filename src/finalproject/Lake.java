/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import static finalproject.Chunks.CHUNK_SIZE;
import static finalproject.Chunks.CUBE_LENGTH;
import static finalproject.Chunks.createTexCube;
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
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author manam
 */
public class Lake
{
    static final int CUBE_LENGTH = 2;
    private int LAKE_DEPTH;
    private int LAKE_AREA; 
    private int LAKE_SPREAD;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    private Texture texture;
    private int startX, startY, startZ;
    
    Lake(int startX, int startY, int startZ) {
        try{
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e){
            System.out.print("error");
        }
    
        Random r = new Random();
        LAKE_DEPTH = r.nextInt(10) + 1;
        LAKE_AREA = r.nextInt(30) + 1;
        LAKE_SPREAD = r.nextInt(30) + 1;
        VBOTextureHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        startX = startX;
        startY = startY;
        startZ = startZ;
        rebuildMesh(startX, startY,startZ);
    }
    
    public void render(){
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2, GL_FLOAT,0,0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    private void rebuildMesh(int startX, int startY, int startZ) {
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((LAKE_AREA * LAKE_DEPTH * LAKE_SPREAD) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((LAKE_AREA * LAKE_DEPTH * LAKE_SPREAD) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((LAKE_AREA * LAKE_DEPTH * LAKE_SPREAD) * 6 * 12);
        
        Block global = new Block(Block.BlockType.Water); // doing this since color requires block. Global since all water blocks are same.
        
        for(int x = 0; x < LAKE_AREA; x += 1){
            for(int  z = 0; z < LAKE_SPREAD; z += 1){
                for(float y = 0; y < LAKE_DEPTH; y += 1){
                    VertexPositionData.put(createCube((float)(startX + x * CUBE_LENGTH), (startY - y * CUBE_LENGTH),(float)(startZ + z * CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(global)));
                    VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Water));
                }
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
            x + offset * 1, y + offset * 11,
            x + offset * 2, y + offset * 11,
            x + offset * 2, y + offset * 12,
            x + offset * 1, y + offset * 12,
            //top quad
            x + offset * 1, y + offset * 11,
            x + offset * 2, y + offset * 11,
            x + offset * 2, y + offset * 12,
            x + offset * 1, y + offset * 12,
            //front quad
            x + offset * 1, y + offset * 11,
            x + offset * 2, y + offset * 11,
            x + offset * 2, y + offset * 12,
            x + offset * 1, y + offset * 12,
            //back quad
            x + offset * 2, y + offset * 12,
            x + offset * 1, y + offset * 12,
            x + offset * 1, y + offset * 11,
            x + offset * 2, y + offset * 11,
            // LEFT QUAD  
            x + offset * 1, y + offset * 11,
            x + offset * 2, y + offset * 11,
            x + offset * 2, y + offset * 12,
            x + offset * 1, y + offset * 12,
            // RIGHT QUAD
            x + offset * 1, y + offset * 11,
            x + offset * 2, y + offset * 11,
            x + offset * 2, y + offset * 12,
            x + offset * 1, y + offset * 12,
        };
    }
}
