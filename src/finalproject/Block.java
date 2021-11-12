/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

/**
 *
 * @author jkao1
 */
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
    
    public Block(BlockType type){
        Type = type;
    }
    public void setCoords(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
    }
    public boolean isActive(){
        return isActive;
    }
    public void setActive(boolean active){
        isActive = active;
    }
    public int getID(){
        return Type.GetID();
    }
}
