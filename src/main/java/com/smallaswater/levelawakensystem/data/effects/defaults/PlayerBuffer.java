package com.smallaswater.levelawakensystem.data.effects.defaults;


import com.smallaswater.levelawakensystem.data.BaseHuman;
import com.smallaswater.levelawakensystem.data.effects.BaseBuffer;

import java.util.LinkedHashMap;

/**
 * @author SmallasWater
 */
public class PlayerBuffer extends BaseBuffer {



    private LinkedHashMap<BaseHuman.PlayerAttribute,Integer> hashMap = new LinkedHashMap<>();


    public PlayerBuffer(String bufferName, int loadTime, int coldTime) {
        super(bufferName, loadTime, coldTime);
    }



    public void setHashMap(LinkedHashMap<BaseHuman.PlayerAttribute, Integer> hashMap) {
        this.hashMap = hashMap;
    }

    public LinkedHashMap<BaseHuman.PlayerAttribute, Integer> getHashMap() {
        return hashMap;
    }
}
