/*************************************************************** *
 * file: gameLoops.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Final Checkpoint 
 * date last modified: 11/26/2021 * 
 * purpose: This file contains the various gameloops for free camera, gravity, and object 
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
import org.lwjgl.util.glu.GLU;


public class gameLoops extends FPCameraController{
    
    public gameLoops(float x, float y, float z) {
        super(x, y, z);
    }
    
    private final float GRAVITY = .015f;
    private final float TERMINAL_VELOCITY = 3.0f;
    private final float UPSPEED = 5.0f;
    
    public void gameLoopCamera(FPCameraController cam, Player pl){
        Display.setTitle("Free Camera");
        boolean isJump = false;
        float fall = 0;
        float dx = 0.0f;
        float dy = 0.0f;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        float previousY;
        float previousX;
        float previousZ;
        boolean isFly = true; 
        Mouse.setGrabbed(true);
        
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            
            //used to keep track of the last position of the camera so if there is collision
            //send the camera back to this last position.
            previousY = cam.position.y;
            previousX = cam.position.x;
            previousZ = cam.position.z;
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            cam.yaw(dx * mouseSensitivity);
            cam.pitch(dy * mouseSensitivity);          
            
            if(Keyboard.isKeyDown(Keyboard.KEY_W)){
                cam.walkForward(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)){
                cam.walkBackwards(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)){
                cam.strafeLeft(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)){
                cam.strafeRight(movementSpeed);
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
                    cam.moveUp(UPSPEED);
                else
                    cam.moveUp(movementSpeed);
              
                isJump = true;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                cam.moveDown(movementSpeed);
            }            
            float terrainHeight = chunks.get(0).getHeightOfTerrain(cam.position.x, cam.position.z);
            //applies gravity to the camera until it accelerates to TERMINAL_VELOCITY in which it will fall at a constant speed.
            if(!isFly){
                fall += GRAVITY;
                if (fall > TERMINAL_VELOCITY){
                    fall = TERMINAL_VELOCITY;
                }
                cam.moveDown(fall);
            }
                      
            //if the camera falls below the terrain, move the camera on top of the terrain
            //(blocks camera from falling through the floor)
            if(cam.position.y > -terrainHeight*2){

                cam.position.y = -terrainHeight*2;
                isJump = false;
                fall = 0;
                //if the camera position goes from a lower position to a higher position
                //(i.e from a block that is shorter to a block that is taller)
                //move the camera to its previous position.
                if(cam.position.y < previousY){
                    cam.position.y = previousY;
                    cam.position.x = previousX;
                    cam.position.z = previousZ;
                }               
            }
            
            glLoadIdentity();
            cam.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(100).put(200).put(100).put(2.0f).flip();
            glLight(GL_LIGHT0, GL_POSITION, lightPosition);
            
            for(Chunks chunk : chunks)
                chunk.render();       
            
            pl.modelo.render();            
            displayUpdate();
            if(Keyboard.isKeyDown(Keyboard.KEY_P)){
                gameLoopPlayer(cam, pl);
            }
        }           
        Display.destroy();      
    }
        
    public void gameLoopPlayer(FPCameraController cam, Player pl){
        Display.setTitle("Object Camera");       
        float dx = 0.0f;
        float dy = 0.0f;
        float mouseSensitivity = 0.09f;
        
        Mouse.setGrabbed(true);
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            cam.yaw(dx * mouseSensitivity);
            cam.pitch(dy * mouseSensitivity);                   
            glLoadIdentity();
            cam.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
            lightPosition.put(100).put(200).put(100).put(2.0f).flip();
            glLight(GL_LIGHT0, GL_POSITION, lightPosition);
            
            for(Chunks chunk : chunks)
                chunk.render();
                        
            pl.move();           
            pl.modelo.render();
            
            if(Keyboard.isKeyDown(Keyboard.KEY_TAB)){
                gameLoopCamera(cam, pl);
            }            
            displayUpdate();                                 
        }           
        Display.destroy();      
    }
    
    
    public static void displayUpdate(){
        Display.update();
        Display.sync(60);
    }
        
    
}
