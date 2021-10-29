/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static  org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author jkao1
 */
public class FinalProject {

    private FPCameraController fp;
    private DisplayMode displayMode;
    
    public void start(){
        try{
            
            createWindow();
            initGL();
            fp = new FPCameraController(0f,0f,0f);
            fp.gameLoop();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for(int i = 0; i< d.length; i++){
            if(d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32){
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
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FinalProject basic = new FinalProject();
        basic.start();
    }
    
}