package com.smallaswater.levelawakensystem.skills;

import cn.nukkit.potion.Effect;
import com.smallaswater.levelawakensystem.data.BaseHuman;
import com.smallaswater.levelawakensystem.data.players.PlayerData;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class Buffers {

    private String bufferName;

    private LinkedHashMap<BaseHuman.PlayerAttribute,String> playerAttributes = new LinkedHashMap<>();

    private LinkedList<Effect> effects = new LinkedList<>();

    private LinkedHashMap<PlayerData.PlayerDefaultAttribute,String> defaultAttribute = new LinkedHashMap<>();

    public Buffers(String bufferName){
        this.bufferName = bufferName;
    }

    public void setPlayerAttributes(LinkedHashMap<BaseHuman.PlayerAttribute, String> playerAttributes) {
        this.playerAttributes = playerAttributes;
    }

    public void setEffects(LinkedList<Effect> effects) {
        this.effects = effects;
    }

    public void setDefaultAttribute(LinkedHashMap<PlayerData.PlayerDefaultAttribute, String> defaultAttribute) {
        this.defaultAttribute = defaultAttribute;
    }

    public LinkedList<Effect> getEffects() {
        return effects;
    }

    public String getBufferName() {
        return bufferName;
    }

    public LinkedHashMap<BaseHuman.PlayerAttribute, String> getPlayerAttributes() {
        return playerAttributes;
    }

    public LinkedHashMap<PlayerData.PlayerDefaultAttribute, String> getDefaultAttribute() {
        return defaultAttribute;
    }
}
