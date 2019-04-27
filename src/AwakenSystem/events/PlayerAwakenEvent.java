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
//玩家觉醒事件
/**
 * 玩家觉醒属性事件
 *
 * */
public class PlayerAwakenEvent extends PlayerEvent implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private String element;
    public PlayerAwakenEvent(Player player, String element){
        this.player = player;
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
