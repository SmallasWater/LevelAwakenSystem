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
 * 玩家升级事件
 * */
public class PlayerLevelUpEvent extends PlayerEvent implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private int oldLevel;
    private int addLevel;
    private boolean cancelled = false;
    private boolean showUi = true;

    public PlayerLevelUpEvent(Player player,int oldLevel,int addLevel){
        this.player = player;
        this.oldLevel = oldLevel;
        this.addLevel = addLevel;
    }
    public PlayerLevelUpEvent(Player player,int oldLevel,int addLevel,boolean showUi){
        this.player = player;
        this.oldLevel = oldLevel;
        this.addLevel = addLevel;
        this.showUi = showUi;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getAddLevel() {
        return addLevel;
    }

    public boolean isShowUi() {
        return showUi;
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
