/*************************************************************** *
 * file: gameLoops.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Final Checkpoint 
 * date last modified: 11/26/2021 * 
 * purpose: This file contains methods to move player
 * ****************************************************************/ 
package finalproject;

import finalproject.PlayerModel;
import finalproject.Chunks;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import finalproject.FPCameraController;

/**
 *
 * @author Sayem
 */
public class Player extends FPCameraController{

    public PlayerModel modelo;
    float distance = .35f;
    private float yaw;
    private float turn = 100;
    public float rotX, rotY, rotZ;
    
    public Player(PlayerModel model, float yawh, float x, float y, float z) {
        super(x, y, z);
        modelo = model;
        distance = distance;
        yaw = yawh;       
    }
    
    public void move(){
        
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
            float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
            modelo.position.x -= xOffset;
            modelo.position.z += zOffset;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
            float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
            modelo.position.x += xOffset;
            modelo.position.z -= zOffset;        
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
            float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
            modelo.position.x -= xOffset;
            modelo.position.z += zOffset;        
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
            float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
            modelo.position.x -= xOffset;
            modelo.position.z += zOffset;        
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            modelo.position.y += distance;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            modelo.position.y -= distance;
        }

    }
    
    
}
