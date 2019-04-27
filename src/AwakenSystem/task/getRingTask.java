package AwakenSystem.task;

import AwakenSystem.events.PlayerOrnamentsEvent;
import AwakenSystem.utils.nbtItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;

import java.util.HashMap;
import java.util.Objects;

public class getRingTask extends Task{
    private HashMap<Player,Item> first = new HashMap<>();
    @Override
    public void onRun(int i) {
        for (Player player:Server.getInstance().getOnlinePlayers().values()){
            Item item = player.getInventory().getItem(35);
            if(nbtItems.is_Item(item)){
                String names = nbtItems.getName(item);
                if(nbtItems.is_ring(item)){
                    if(!first.containsKey(player)){
                        PlayerOrnamentsEvent events = new PlayerOrnamentsEvent(player,item,PlayerOrnamentsEvent.wear);
                        Server.getInstance().getPluginManager().callEvent(events);
                        first.put(player,item);
                    }else if (!(Objects.equals(nbtItems.getName(first.get(player)), names))){
                        PlayerOrnamentsEvent event1 = new PlayerOrnamentsEvent(player,item,PlayerOrnamentsEvent.wear);
                        Server.getInstance().getPluginManager().callEvent(event1);
                        event1 = new PlayerOrnamentsEvent(player,first.get(player),PlayerOrnamentsEvent.remove);
                        Server.getInstance().getPluginManager().callEvent(event1);
                        first.put(player,item);
                    }
                }
            }else if(first.containsKey(player)){
                PlayerOrnamentsEvent event1 = new PlayerOrnamentsEvent(player,first.get(player),PlayerOrnamentsEvent.remove);
                Server.getInstance().getPluginManager().callEvent(event1);
                first.remove(player);
            }
        }
    }
}
