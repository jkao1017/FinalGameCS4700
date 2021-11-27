/*************************************************************** *
 * file: FinalProject.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Checkpoint 3 
 * date last modified: 11/26/2021 * 
 * purpose: This file is responsible for initializing everything and starting the program.
 * ****************************************************************/ 
package finalproject;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static  org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;


public class FinalProject {
    private FPCameraController fp;
    private DisplayMode displayMode;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;   
    
    //method: start
    //purpose: initalizes window and camera 
    public void start(){
        try{
            
            createWindow();
            initGL();
            fp = new FPCameraController(0f,0f,0f);
            fp.startGameLoop();

            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //method: createWindow
    //purpose: creates a window
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for(int i = 0; i< d.length; i++){
            //640 480
            if(d[i].getWidth() == 1920 && d[i].getHeight() == 1080 && d[i].getBitsPerPixel() == 32){
                displayMode = d[i];
                break;
            } 
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Final Project");
        Display.create();
    }
    
    private void initGL(){
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glMatrixMode(GL_PROJECTION);
        glEnable(GL_DEPTH_TEST);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glLoadIdentity();
        GLU.gluPerspective(100.0f,(float)displayMode.getWidth()/(float)displayMode.getHeight(),0.1f,300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT,GL_NICEST);
        
        initLightArrays();
        glShadeModel(GL_SMOOTH);
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);//sets our ambient light
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0
    }
    
    //method: initLightArrays
    //purpose: initalizes light
    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(30.0f).put(100.0f).put(30.0f).put(1.0f).flip();
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
    }
    
    public static void main(String[] args) {
        FinalProject basic = new FinalProject();
        basic.start();
    }
}
