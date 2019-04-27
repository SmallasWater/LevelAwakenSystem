package AwakenSystem.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

/**
 * 玩家进阶属性事件
 * */
public class PlayerUpAwakenEvent extends PlayerEvent implements Cancellable{

    private String awaken,newAwaken;
    private static final HandlerList handlers = new HandlerList();
    public PlayerUpAwakenEvent(Player player, String att,String newAtt){
        this.awaken = att;
        this.player = player;
        this.newAwaken = newAtt;
    }
    public static HandlerList getHandlers() {
        return handlers;
    }

    public String getAwaken(){
        return this.awaken;
    }

    public String getNewAwaken(){
        return this.newAwaken;
    }
}
