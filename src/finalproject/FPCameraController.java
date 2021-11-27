/*************************************************************** *
 * file: FPCameraConterller.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Final Checkpoint  
 * date last modified: 11/26/2021 * 
 * purpose: This file defines a first person camera
 * ****************************************************************/ 

package finalproject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import finalproject.gameLoops;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import static finalproject.gameLoops.displayUpdate;
import org.lwjgl.Sys;



public class FPCameraController{
    
    private final float GRAVITY = .015f;
    private final float TERMINAL_VELOCITY = 3.0f;
    private final float UPSPEED = 5.0f;
    
    private final int MAX_CHUNKS = 3;
    public ArrayList<Chunks> chunks;
    public Vector3f position;
    private Vector3f lPosition;
           
    //rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
   
    
    public FPCameraController(float x, float y, float z){
        position = new Vector3f(x,y,z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
        
        chunks = new ArrayList<>();
        int offset = Chunks.CHUNK_SIZE * Chunks.CUBE_LENGTH;
        for(int i = 0, xCurr = 0, zCurr = 0; i < MAX_CHUNKS; i++) {
            chunks.add(new Chunks(xCurr, 0, zCurr));
            boolean incr = false;
            if(Math.random() <= .5) {
                if(Math.random() <= .5)
                    xCurr += offset;
                else
                    xCurr -= offset;
                incr = true;
            }
            if(Math.random() > .5) {
                if(Math.random() <= .5)
                    zCurr += offset;
                else
                    zCurr -= offset;
                incr = true;
            }
            if(!incr) {
                double r = Math.random();
                xCurr += r <= .5 ? (Math.random() >= .5 ? offset : -offset) : 0;
                zCurr += r > .5 ? (Math.random() >= .5 ? offset : -offset) : 0;
            }
        }
    }
    
    //method: yaw
    //purpose: modify yaw by set amount
    public void yaw(float amount){
        yaw += amount;
    }
    
    //method: pitch
    //purpose: modify pitch by set amount
    public void pitch(float amount){
        pitch -= amount;
    }
    
    //method: walkForward
    //purpose: modify position based on distance moved forward
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x-=xOffset).put(lPosition.y).put(lPosition.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        
    }
    
    //method: walkBackwards
    //purpose: modify position based on distance moved backward
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x+=xOffset).put(lPosition.y).put(lPosition.z-=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        
    }
    
    //method: strafeLeft
    //purpose: modify position based on distance moved to the left
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x-=xOffset).put(lPosition.y).put(lPosition.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); 
        
        
    }
    
    //method: strafeRight
    //purpose: modify position based on distance moved to the right
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x-=xOffset).put(lPosition.y).put(lPosition.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
 
    }
    
    //method: moveUp
    //purpose: modify position based on distance moved up
    public void moveUp(float distance){
        position.y -= distance;
    }
    
    //method: moveDown
    //purpose: modify position based on distance moved down
    public void moveDown(float distance){
        position.y += distance;
    }
    
    //method: lookThrough
    //purpose: transformation matrices for distance moved, and view change
    public void lookThrough(){
        glRotatef(pitch,1.0f,0.0f,0.0f);
        glRotatef(yaw,0.0f,1.0f,0.0f);
        glTranslatef(position.x,position.y,position.z);
    }

    //method: gameLoop
    //purpose: responsible for keeping track of inputs, camera 
    //view and pos every frame, as well as lighting.
 
    public void startGameLoop(){
        FPCameraController camera = new FPCameraController(-54,-85,-60);
        boolean isJump = false;
        float fall = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        float previousY;
        float previousX;
        float previousZ;
        boolean isFly = false; 
        
        Player playa = new Player(new PlayerModel(20,45,20), yaw ,0,0,0);
        gameLoops game = new gameLoops(0,0,0);
        Mouse.setGrabbed(true);        
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);                   
            glLoadIdentity();
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(90).put(150).put(90).put(1.5f).flip();
            glLight(GL_LIGHT0, GL_POSITION, lightPosition);
            
            for(Chunks chunk : chunks)
                chunk.render();       
                        
            playa.modelo.render();            
            game.gameLoopCamera(camera, playa); 
            //displayUpdate(); 
            Display.destroy();      

        }

    }
    
}
