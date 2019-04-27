package AwakenSystem.events;
/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */


import AwakenSystem.data.baseAPI;
import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

import java.util.LinkedHashMap;
import java.util.Set;
/**
 * 玩家增加Buff事件
 * */
public class PlayerAddBufferEvent extends PlayerEvent{

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private LinkedHashMap<baseAPI.ItemADDType,int[]> addBuffer;
    private String bufferName;
    public PlayerAddBufferEvent(Player player,String bufferName, LinkedHashMap<baseAPI.ItemADDType,int[]> addBuffer){
        this.player = player;
        this.addBuffer = addBuffer;
        this.bufferName = bufferName;
    }
    public static HandlerList getHandlers() {
        return handlers;
    }

    public String getBufferName(){
        return this.bufferName;
    }

    public LinkedHashMap<baseAPI.ItemADDType,int[]> getBuffers(){
        return this.addBuffer;
    }

    public Set getBufferNames(){
        return this.addBuffer.keySet();
    }
}
