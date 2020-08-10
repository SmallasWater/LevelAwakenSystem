package com.smallaswater.levelawakensystem.events.defaults;

import cn.nukkit.Player;
import com.smallaswater.levelawakensystem.events.BaseDefaultPlayerEvent;

public class PlayerAddLevelEvent extends BaseDefaultPlayerEvent{
    private int level;
    public PlayerAddLevelEvent(Player player, int level){
        this.player = player;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
