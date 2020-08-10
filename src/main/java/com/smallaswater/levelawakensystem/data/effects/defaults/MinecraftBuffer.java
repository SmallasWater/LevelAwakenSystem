package com.smallaswater.levelawakensystem.data.effects.defaults;


import cn.nukkit.potion.Effect;
import com.smallaswater.levelawakensystem.data.effects.BaseBuffer;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class MinecraftBuffer extends BaseBuffer {

    private Effect effect;

    public MinecraftBuffer(Effect effect,String bufferName, int loadTime, int coldTime) {
        super(bufferName, loadTime, coldTime);
        this.effect = effect;
    }

    @Override
    public LinkedHashMap<String, Object> toSave() {
        LinkedHashMap<String,Object> saves = super.toSave();
        saves.put("effect",effect.getId()+":"+effect.getAmplifier());
        return saves;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }
}
