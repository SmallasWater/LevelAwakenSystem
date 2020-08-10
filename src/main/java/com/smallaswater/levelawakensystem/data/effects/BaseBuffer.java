package com.smallaswater.levelawakensystem.data.effects;


import cn.nukkit.potion.Effect;
import com.smallaswater.levelawakensystem.data.effects.defaults.MinecraftBuffer;
import com.smallaswater.levelawakensystem.data.effects.defaults.PlayerBuffer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SmallasWater
 */
public abstract class BaseBuffer {

    private String bufferName;

    private int loadTime;

    private int coldTime;





    public BaseBuffer(String bufferName,int loadTime,int coldTime){
        this.bufferName = bufferName;
        this.loadTime = loadTime;
        this.coldTime = coldTime;
    }

    public int getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(int loadTime) {
        this.loadTime = loadTime;
    }

    public int getColdTime() {
        return coldTime;
    }

    public void setColdTime(int coldTime) {
        this.coldTime = coldTime;
    }

    public String getBufferName() {
        return bufferName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseBuffer){
            return ((BaseBuffer) obj).bufferName.equals(getBufferName());
        }
        return false;
    }

    public LinkedHashMap<String, Object> toSave() {
        LinkedHashMap<String,Object> saves = new LinkedHashMap<>();
        saves.put("bufferName",getBufferName());
        saves.put("loadTime",getLoadTime());
        saves.put("coldTime",getColdTime());
        return saves;
    }
    public static BaseBuffer getBufferByMap(Map map){
        if(map.containsKey("effect")){
            String effect = map.get("effect").toString();
            Effect effect1 = Effect.getEffect(Integer.parseInt(effect.split(":")[0])).setAmplifier(effect.split(":").length > 1?Integer.parseInt(effect.split(":")[1]):1);
            return new MinecraftBuffer(effect1,map.get("bufferName").toString(),Integer.parseInt(map.get("loadTime").toString()),Integer.parseInt(map.get("coldTime").toString()));
        }else{
            return new PlayerBuffer(map.get("bufferName").toString(),Integer.parseInt(map.get("loadTime").toString()),Integer.parseInt(map.get("coldTime").toString()));
        }
    }


}
