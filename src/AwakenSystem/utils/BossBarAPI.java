package AwakenSystem.utils;

import AwakenSystem.AwakenSystem;
import AwakenSystem.data.baseAPI;
import AwakenSystem.data.defaultAPI;
import cn.nukkit.Player;
import cn.nukkit.utils.DummyBossBar;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class BossBarAPI {
    protected Player player;
    public BossBarAPI(Player player){
        this.player = player;
    }
    public void createBossBar(){
        long eid = 0xbb071;
        DummyBossBar bossBar = (new BossBar(this.player, eid)).build();
        bossBar.setLength(100.0F);
        bossBar.setText("加载中");
        AwakenSystem.getMain().bar.put(this.player, bossBar);
        this.player.createBossBar(AwakenSystem.getMain().bar.get(this.player));
    }


    public void showBoss(){
        if(AwakenSystem.getMain().bar.get(player) != null){
            float percentage =(player.getHealth() / player.getMaxHealth()) * 100;
            String text = AwakenSystem.getMain().getConfig().getString(baseAPI.ConfigType.showBoss.getName());
            DummyBossBar bossBar = AwakenSystem.getMain().bar.get(player);
            bossBar.setColor(0,255,0);
            bossBar.setText(defaultAPI.getStr_replace(player, text));
            bossBar.setLength(percentage);
            player.createBossBar(bossBar);
        }
    }
}
class BossBar extends DummyBossBar.Builder{

    private final long bossBarId;
    BossBar(Player player,long bossBarId) {
        super(player);
        this.bossBarId = bossBarId;
    }

    public long getBossBarId() {
        return bossBarId;
    }
}
