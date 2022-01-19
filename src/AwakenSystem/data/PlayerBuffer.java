package AwakenSystem.data;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */

import AwakenSystem.AwakenSystem;
import cn.nukkit.Player;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class PlayerBuffer implements baseAPI{
    private Player player;
    private String bufferName;
    private LinkedHashMap<String,LinkedHashMap<ItemADDType,int[]>> buffer = new LinkedHashMap<>();

    public PlayerBuffer(Player player, String bufferName){
        this.player = player;
        this.bufferName = bufferName;
        if(AwakenSystem.getMain().runAdd.containsKey(player)){
            this.buffer = AwakenSystem.getMain().runAdd.get(player);
        }
    }
    public LinkedHashMap<String,LinkedHashMap<ItemADDType,int[]>> getBuffer(){
        return this.buffer;
    }


    public boolean canAddBuffer(String bufferName){
        return this.buffer.containsKey(bufferName);
    }

    public LinkedHashMap<String,LinkedHashMap<ItemADDType,int[]>> addBuffer(LinkedHashMap<ItemADDType,int[]> addTypeLinkedHashMap){
        for(ItemADDType type:addTypeLinkedHashMap.keySet()){
            if(!this.buffer.containsKey(this.bufferName)){
                this.buffer.put(this.bufferName,new LinkedHashMap<>());
            }
            if(addTypeLinkedHashMap.get(type) == null){
                int[] ints = new int[]{3,999999,999999};
                addTypeLinkedHashMap.put(type,ints);
            }
            this.buffer.put(this.bufferName,addTypeLinkedHashMap);
        }
        return this.buffer;
    }


    public LinkedHashMap<String,LinkedHashMap<ItemADDType,int[]>> removeBuffer_Type(ItemADDType type){
        if(this.buffer.containsKey(this.bufferName)){
            if(this.buffer.get(this.bufferName).containsKey(type)){
                this.buffer.get(this.bufferName).remove(type);
            }
            if(this.buffer.get(this.bufferName).size() == 0){
                return this.removeBuffer();
            }
        }
        return this.buffer;
    }
    public LinkedHashMap<String,LinkedHashMap<ItemADDType,int[]>> removeBuffer(){
        if(this.buffer.containsKey(this.bufferName)){
            this.buffer.remove(this.bufferName);
        }
        return this.buffer;
    }

    public Player getPlayer() {
        return player;
    }

    public String getBufferName() {
        return bufferName;
    }


    public LinkedHashMap<ItemADDType,int[]> getBuffers() {
        return this.buffer.get(this.bufferName);
    }

}
