package com.smallaswater.levelawakensystem.events.defaults;

import cn.nukkit.Player;
import com.smallaswater.levelawakensystem.events.BaseDefaultPlayerEvent;

public class PlayerAddExpEvent extends BaseDefaultPlayerEvent {

    private float exp;
    public PlayerAddExpEvent(Player player,float exp){
        this.player = player;
        this.exp = exp;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }
}
