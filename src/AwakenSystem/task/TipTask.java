package AwakenSystem.task;
/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */

import AwakenSystem.AwakenSystem;
import AwakenSystem.data.baseAPI;
import AwakenSystem.data.defaultAPI;
import AwakenSystem.data.getFiles;
import AwakenSystem.utils.BossBarAPI;
import AwakenSystem.utils.nbtItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPaper;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;

import javax.tools.Tool;
import java.io.File;
import java.util.LinkedHashMap;

public class TipTask extends Task{
    @Override
    public void onRun(int i) {
        for (Player player: Server.getInstance().getOnlinePlayers().values()){
            if(new File(AwakenSystem.getMain().getPlayerFileName(player)).exists()){
                if(AwakenSystem.getMain().getConfig().getBoolean(baseAPI.ConfigType.can_show_Tip.getName())){
                    String type = AwakenSystem.getMain().getConfig().getString(baseAPI.ConfigType.show_Tip_type.getName());
                    String message = defaultAPI.getStr_replace(player,AwakenSystem.getMain().getConfig().getString(baseAPI.ConfigType.TIP.getName()));
                    switch (type){
                        case "tip":
                            player.sendTip(message);
                            break;
                        case "popup":
                            player.sendPopup(message);
                            break;
                        default:
                            player.sendTip(message);
                            break;
                    }
                }
                if(AwakenSystem.getMain().getConfig().getBoolean(baseAPI.ConfigType.can_show_Tag.getName())) {
                    player.setNameTag(defaultAPI.getStr_replace(player,AwakenSystem.getMain().getConfig().getString(baseAPI.ConfigType.TAG_MESSAGE.getName())));
                }


            }else{
                LinkedHashMap<String,Object> conf = getFiles.initPlayer();
                Config config = AwakenSystem.getMain().getPlayerConfig(player);
                config.setAll(conf);
                config.save();
            }
        }
    }
}
