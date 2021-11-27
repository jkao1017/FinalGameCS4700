/*************************************************************** *
 * file: Block.java 
 * author: Jonathan Kao, Mohammed Bari, Viswadeep Manam
 * class: CS 4450- Computer Graphics * 
 * assignment: Final Checkpoint  
 * date last modified: 11/12/2021 * 
 * purpose: This file defines a block and its different types
 * ****************************************************************/ 

package finalproject;

public class Block {
    private boolean isActive;
    private BlockType Type;
    private float x,y,z;
    
    public enum BlockType{
        Grass(0),
        Sand(1),
        Water(2),
        Dirt(3),
        Stone(4),
        Bedrock(5);
        
        private int BlockID;
        
        BlockType(int i){
            BlockID = i;
        }
        public int GetID(){
            return BlockID;
        }
        public void SetID(int i){
            BlockID = i;
        }
    }
    
   public Block() {
       isActive = true;
   }
    
    public Block(BlockType type){
        Type = type;
    }
    
    // method: setCoords
    // purpose: this method sets the coordinates of the block
    public void setCoords(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
    }
    
    // method: isActive
    // purpose: this method returns the active status of a block
    public boolean isActive(){
        return isActive;
    }
    
    // method: setActive
    // purpose: this method sets the active status of a block
    public void setActive(boolean active){
        isActive = active;
    }
    
    // method: getId
    // purpose: this method returns the id of a block
    public int getID(){
        return Type.GetID();
    }
}
