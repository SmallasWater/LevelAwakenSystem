package AwakenSystem.events;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;

/**
 * @author 若水
 */
public class PlayerGetItemEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private Item item;
    public PlayerGetItemEvent(Player player, Item item){
        this.player = player;
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
