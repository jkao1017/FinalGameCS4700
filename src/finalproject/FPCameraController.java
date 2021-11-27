/*************************************************************** *
 * file: FPCameraConterller.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Checkpoint 3 
 * date last modified: 11/12/2021 * 
 * purpose: This file defines a first person camera
 * ****************************************************************/ 

package finalproject;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.BufferUtils;

public class FPCameraController{
    
    private final float GRAVITY = .015f;
    private final float TERMINAL_VELOCITY = 3.0f;
    private final float UPSPEED = 5.0f;
    private final int MAX_CHUNKS = 3;
    private ArrayList<Chunks> chunks;
    private Vector3f position;
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
    public void gameLoop(){
        FPCameraController camera = new FPCameraController(0,-100,0);
        boolean isJump = false;
        float fall = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        float previousY;
        float previousX;
        float previousZ;
        boolean isFly = false;
        Mouse.setGrabbed(true);
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            
            
            //used to keep track of the last position of the camera so if there is collision
            //send the camera back to this last position.
            previousY = camera.position.y;
            previousX = camera.position.x;
            previousZ = camera.position.z;
            
            time = Sys.getTime();

            lastTime = time;
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);
            
            if(Keyboard.isKeyDown(Keyboard.KEY_W)){
                camera.walkForward(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)){
                camera.walkBackwards(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)){
                camera.strafeLeft(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)){
                camera.strafeRight(movementSpeed);
            }
            //allows you to fly around the map without gravity/ jumping mechanics
            if(Keyboard.isKeyDown(Keyboard.KEY_Y)){
                isFly = true;
            }   
            //activates gravity and jumping
            if(Keyboard.isKeyDown(Keyboard.KEY_L)){
                isFly = false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                
                if(!isJump && !isFly)
                    camera.moveUp(UPSPEED);
                else
                    camera.moveUp(movementSpeed);
              
                isJump = true;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                camera.moveDown(movementSpeed);
            }
            
            float terrainHeight = chunks.get(0).getHeightOfTerrain(camera.position.x, camera.position.z);
            //applies gravity to the camera until it accelerates to TERMINAL_VELOCITY in which it will fall at a constant speed.
            if(!isFly){
                fall += GRAVITY;
                if (fall > TERMINAL_VELOCITY){
                    fall = TERMINAL_VELOCITY;
                }
                camera.moveDown(fall);
            }
            
            

            //if the camera falls below the terrain, move the camera on top of the terrain
            //(blocks camera from falling through the floor)
            if(camera.position.y > -terrainHeight*2){

                camera.position.y = -terrainHeight*2;
                isJump = false;
                fall = 0;
                //if the camera position goes from a lower position to a higher position
                //(i.e from a block that is shorter to a block that is taller)
                //move the camera to its previous position.
                if(camera.position.y < previousY){
                    camera.position.y = previousY;
                    camera.position.x = previousX;
                    camera.position.z = previousZ;
                }
                
            }
            
            
            glLoadIdentity();
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);

            lightPosition.put(100.0f).put(120.0f).put(100.0f).put(2.0f).flip();

            glLight(GL_LIGHT0, GL_POSITION, lightPosition);
            
            for(Chunks chunk : chunks)
                chunk.render();
            
            Display.update();
            Display.sync(60);
            
        }
        Display.destroy();
    }
}
