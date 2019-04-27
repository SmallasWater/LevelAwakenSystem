package AwakenSystem.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

/**
 * 玩家重置属性事件
 * */
public class PlayerResetAwakenEvent extends PlayerEvent implements Cancellable{
    private String awaken;

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlers() {
        return handlers;
    }

    public PlayerResetAwakenEvent(Player player, String awaken){
        this.awaken = awaken;
        this.player = player;
    }

    public String getAwaken(){
        return this.awaken;
    }

}
