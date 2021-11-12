/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.util.Random;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author jkao1
 */
public class Chunks {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    private Texture texture;
    private int StartX, StartY, StartZ;
    
    private Random  r;
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
    public void rebuildMesh(float startX, float startY, float startZ){
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        
        Random r = new Random();
        SimplexNoise noise = new SimplexNoise(40, .05, r.nextInt());
        
        for(int x = 0; x < CHUNK_SIZE; x += 1){
            for(int  z = 0; z < CHUNK_SIZE; z += 1){
                float height = (22 + (int)(100*noise.getNoise(x, z)) * CUBE_LENGTH);
                System.out.println(height);
                //System.out.println(height);
                for(float y = 0; y < height; y++){
                    VertexPositionData.put(createCube((float)(startX + x * CUBE_LENGTH), (float)(y*CUBE_LENGTH+(int)(CHUNK_SIZE*.8)),(float)(startZ + z * CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int)x][(int)y][(int)z])));
                    if(y == height-1){
                        if(r.nextFloat() > 0.6){
                            VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Grass));
                        }else if(r.nextFloat() > 0.3){
                            VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Water));
                        }else{
                            VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Sand));
                        }
                        
                    }else if(y == 0){
                        VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Bedrock));
                    }else{
                        if(r.nextFloat() > 0.5f){
                            VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Stone));
                        }else{
                            VertexTextureData.put(createTexCube((float)0, (float)0,Block.BlockType.Dirt));
                        }
                    }
                    
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
    public static float[] createTexCube(float x, float y, Block.BlockType type){
        float offset = (1024f/16)/1024f;
        switch(type){
            case Grass: 
                return new float[]{
                    //top quad 
                    x + offset*2, y + offset*9,
                    x + offset*3, y + offset*9, 
                    x + offset*3, y + offset*10,
                    x + offset*2, y + offset*10,
                    //bottom  quad
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0, 
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    //front quad
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1,
                    //back quad
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*0, 
                    x + offset*4, y + offset*0,
                    // LEFT QUAD  
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1,
                };
            case Sand: 
                return new float[]{
                    //bottom quad 
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1, 
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    //top quad
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1, 
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    //front quad
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1, 
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    //back quad
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2, 
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // LEFT QUAD  
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1, 
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // RIGHT QUAD
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1, 
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                };
            case Water: 
                return new float[]{
                    //bottom quad 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11, 
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    //top quad
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11, 
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    //front quad
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11, 
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    //back quad
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12, 
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    // LEFT QUAD  
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11, 
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    // RIGHT QUAD
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11, 
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                };
            case Dirt: 
                return new float[]{
                    //bottom quad 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0, 
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    //top quad
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0, 
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    //front quad
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0, 
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    //back quad
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // LEFT QUAD  
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0, 
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    // RIGHT QUAD
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0, 
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                };
            case Stone:
                return new float[]{
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
            case Bedrock: 
                return new float[]{
                    //bottom quad 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    //top quad
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    //front quad
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    //back quad
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    // LEFT QUAD  
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // RIGHT QUAD
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                };
        }
                        return new float[]{
                    //bottom quad 
                    x + offset*3, y + offset*10,
                    x + offset*2, y + offset*10, 
                    x + offset*2, y + offset*9,
                    x + offset*3, y + offset*9,
                    //top quad
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    //front quad
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1,
                    //back quad
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    // LEFT QUAD  
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1, 
                    x + offset*3, y + offset*1
                };
    }
    private float[] createCubeVertexCol(float[] CubeColorArray){
        float [] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for(int i = 0; i < cubeColors.length; i++){
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
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
    public Chunks(int startX, int startY, int startZ){
        try{
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e){
            System.out.print("error");
        }
        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for(int x = 0; x < CHUNK_SIZE; x++){
            for(int y = 0; y < CHUNK_SIZE; y++){
                for(int z = 0; z < CHUNK_SIZE; z++){
                    if(r.nextFloat() > 0.8f){
                        Blocks[x][y][z] = new Block(Block.BlockType.Grass);
                    }else if(r.nextFloat() > 0.7f){
                        Blocks[x][y][z] = new Block(Block.BlockType.Dirt);
                    }else if(r.nextFloat() > 0.5f){
                        Blocks[x][y][z] = new Block(Block.BlockType.Water);
                    
                    }else if(r.nextFloat() > 0.4f){
                        Blocks[x][y][z] = new Block(Block.BlockType.Sand);
                    }else if(r.nextFloat() > 0.2f){
                        Blocks[x][y][z] = new Block(Block.BlockType.Stone);
                    }else{
                        Blocks[x][y][z] = new Block(Block.BlockType.Bedrock);
                    }
                }
            }
        }
        VBOTextureHandle = glGenBuffers();
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY,startZ);
    }
}
