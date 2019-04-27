package AwakenSystem.task;

import AwakenSystem.utils.nbtItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class fixToInventoryTask extends Task{
    @Override
    public void onRun(int i) {
        for (Player player: Server.getInstance().getOnlinePlayers().values()){
            for (Item item:player.getInventory().getContents().values()){
                if(nbtItems.is_Item(item))   {
                    if(!nbtItems.is_Exit(nbtItems.getName(item))){
                        player.getInventory().removeItem(item);
                    }
                }
            }
        }
    }
}
