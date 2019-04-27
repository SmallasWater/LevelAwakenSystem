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

/**玩家进阶评级事件*/
public class PlayerUpScoreEvent extends PlayerEvent implements Cancellable{
    private int oldScore,newScore;
    private static final HandlerList handlers = new HandlerList();
    public PlayerUpScoreEvent(Player player,int oldScore,int add){
        this.player = player;
        this.newScore = oldScore + add;
        this.oldScore = oldScore;
    }
    public static HandlerList getHandlers() {
        return handlers;
    }
    public int getOldScore() {
        return oldScore;
    }

    public int getNewScore() {
        return newScore;
    }
}
