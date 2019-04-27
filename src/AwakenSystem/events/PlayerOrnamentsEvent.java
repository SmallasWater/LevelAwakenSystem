package AwakenSystem.events;


import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
/**
 * 玩家装备饰品事件
 * */
public class PlayerOrnamentsEvent extends PlayerEvent{

    public static final int wear = 0;
    public static final int remove = 1;
    private static final HandlerList handlers = new HandlerList();
    private Item item;
    private int type;
    public PlayerOrnamentsEvent(Player player, Item item,int type){
        this.player = player;
        this.item = item;
        this.type = type;
    }
    public static HandlerList getHandlers() {
        return handlers;
    }

    public Item getItem(){
        return this.item;
    }

    public int getType() {
        return type;
    }
}
