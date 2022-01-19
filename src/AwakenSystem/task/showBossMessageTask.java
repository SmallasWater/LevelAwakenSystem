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
import AwakenSystem.utils.BossBarAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;


public class showBossMessageTask extends Task{
    @Override
    public void onRun(int i) {
        for (Player player: Server.getInstance().getOnlinePlayers().values()){
            if(AwakenSystem.getMain().getConfig().getBoolean(baseAPI.ConfigType.can_show_Boss.getName())){
                BossBarAPI bossBarAPI = new BossBarAPI(player);
                bossBarAPI.showBoss();
            }
        }
    }
}
