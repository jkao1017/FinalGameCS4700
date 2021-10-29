/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author jkao1
 */
public class FPCameraController {
    
    private Chunks chunk = new Chunks(0,0,0);
    
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
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
    }
    
    public void yaw(float amount){
        yaw += amount;
    }
    public void pitch(float amount){
        pitch -= amount;
    }
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
    }
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    public void moveUp(float distance){
        position.y -= distance;
    }
    public void moveDown(float distance){
        position.y += distance;
    }
    public void lookThrough(){
        glRotatef(pitch,1.0f,0.0f,0.0f);
        glRotatef(yaw,0.0f,1.0f,0.0f);
        glTranslatef(position.x,position.y,position.z);
    }
    public void gameLoop(){
        FPCameraController camera = new FPCameraController(0,0,0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        Mouse.setGrabbed(true);
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
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
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                camera.moveUp(movementSpeed);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                camera.moveDown(movementSpeed);
            }
            glLoadIdentity();
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            chunk.render();
            Display.update();
            Display.sync(60);
            
        }
        Display.destroy();
        
    }
    public void render(){
        try{
            //pyramid
            /*glColor3f(0.0f,1.0f,0.0f);
            glBegin(GL_TRIANGLES);
            glVertex3f(100,100,100);
            glVertex3f(000,000,000);
            glVertex3f(200,000,000);
            
            glColor3f(0.0f,0.0f,1.0f);
            glVertex3f(100,100,100);
            glVertex3f(200,000,000);
            glVertex3f(200,000,200);
            
            glColor3f(0.0f,0.0f,1.0f);
            glVertex3f(100,100,100);
            glVertex3f(000,000,000);
            glVertex3f(000,000,200);
            
            glColor3f(1.0f,1.0f,1.0f);
            glVertex3f(100,100,100);
            glVertex3f(200,000,200);
            glVertex3f(000,000,200);
            glEnd();*/
            glBegin(GL_QUADS);
            //top
                glColor3f(0.0f,0.0f,1.0f);
                glVertex3f(1.0f,1.0f,-1.0f);
                glVertex3f(-1.0f,1.0f,-1.0f);
                glVertex3f(-1.0f,1.0f,1.0f);
                glVertex3f(1.0f,1.0f,1.0f);
                //bottom
                glColor3f(1.0f,0.0f,0.0f);
                glVertex3f(1.0f,-1.0f,1.0f);
                glVertex3f(-1.0f,-1.0f,1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(1.0f,-1.0f,-1.0f);
                //front
                glColor3f(0.0f,1.0f,0.0f);
                glVertex3f(1.0f,1.0f,1.0f);
                glVertex3f(-1.0f,1.0f,1.0f);
                glVertex3f(-1.0f,-1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,1.0f);
                //back
                glColor3f(1.0f,1.0f,0.0f);
                glVertex3f(1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,1.0f,-1.0f);
                glVertex3f(1.0f,1.0f,-1.0f);
                //left
                glColor3f(0.0f,1.0f,1.0f);
                glVertex3f(-1.0f,1.0f,1.0f);
                glVertex3f(-1.0f,1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,1.0f);
                //right
                glColor3f(1.0f,0.0f,1.0f);
                glVertex3f(1.0f,1.0f,-1.0f);
                glVertex3f(1.0f,1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,-1.0f);
            glEnd();
           
            //right
           /* glBegin(GL_LINE_LOOP);
                glColor3f(0.0f,0.0f,0.0f);
                glVertex3f(1.0f,1.0f,-1.0f);
                glVertex3f(1.0f,1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,-1.0f);
            glEnd();
            //top
            glBegin(GL_LINE_LOOP);
                glColor3f(0.0f,0.0f,0.0f);
                glVertex3f(1.0f,1.0f,-1.0f);
                glVertex3f(-1.0f,1.0f,-1.0f);
                glVertex3f(-1.0f,1.0f,1.0f);
                glVertex3f(1.0f,1.0f,1.0f);
            glEnd();
            //bottom
                glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f,-1.0f,1.0f);
                glVertex3f(-1.0f,-1.0f,1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(1.0f,-1.0f,-1.0f);
            glEnd();
            //front
                glVertex3f(1.0f,1.0f,1.0f);
                glVertex3f(-1.0f,1.0f,1.0f);
                glVertex3f(-1.0f,-1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,1.0f);
            glEnd();
            //back
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,1.0f,-1.0f);
                glVertex3f(1.0f,1.0f,-1.0f);
            glEnd();
            //left
            glBegin(GL_LINE_LOOP);
                glVertex3f(-1.0f,1.0f,1.0f);
                glVertex3f(-1.0f,1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,1.0f);
            glEnd();
            //right
            glBegin(GL_LINE_LOOP);
                glVertex3f(1.0f,1.0f,-1.0f);
                glVertex3f(1.0f,1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,1.0f);
                glVertex3f(1.0f,-1.0f,-1.0f);
            glEnd();
            */       
            
        }catch(Exception e){
            
        }
    }
}
