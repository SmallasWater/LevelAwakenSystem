package AwakenSystem.events;
/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
/**
 * 玩家增加经验事件
 * */
public class PlayerAddExpEvent extends PlayerEvent implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private double exp = 0.0;
    public PlayerAddExpEvent(Player player, double exp){
        this.player = player;
        this.exp = exp;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getExp() {
        return exp;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
