package com.smallaswater.levelawakensystem.data.players;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.smallaswater.levelawakensystem.LevelAwakenSystem;
import com.smallaswater.levelawakensystem.data.BaseHuman;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author SmallasWater
 */
public class PlayerData extends BaseHuman {


    private String name;

    public static PlayerData getPlayerData(String playerName){
        if(!LevelAwakenSystem.INSTANCE.linkedHashMap.containsKey(playerName)){
            PlayerData data = new PlayerData(playerName,1,0.0F,301F,0,null,new LinkedHashMap<>());
            LevelAwakenSystem.INSTANCE.linkedHashMap.put(playerName,data);
        }
        return (PlayerData) LevelAwakenSystem.INSTANCE.linkedHashMap.get(playerName);
    }

    public enum PlayerDefaultAttribute{
        /**速度,血量,伤害*/
        speed,health,damage;
    }

    private void setName(String name) {
        this.name = name;
    }

    public static PlayerData getPlayerDataByMap(String playerName, Map map){
        PlayerData human = (PlayerData) BaseHuman.getBaseHumanByMap(map);
        human.setName(playerName);
        return human;
    }

    public PlayerData(Player player, int level, float exp,float maxExp,int levelClass, String occupation, LinkedHashMap<PlayerAttribute, Number> playerAttributes) {
        super(player,level,exp,maxExp, levelClass, occupation, playerAttributes);
        this.name = player.getName();
    }



    public PlayerData(String playerName, int level, float exp,float maxExp,int levelClass, String occupation, LinkedHashMap<PlayerAttribute, Number> playerAttributes) {
        super(null,level,exp,maxExp, levelClass, occupation, playerAttributes);
        this.name = playerName;
    }

    public String getName() {
        return name;
    }




    public void save(){
        Config config = new Config(LevelAwakenSystem.INSTANCE.getDataFolder()+"/players/"+name+".yml");
        config.set("level",getLevel());
        config.set("levelClass",getLevelClass());
        config.set("occupation",getOccupation());
        config.set("exp",getExp());
        config.set("maxExp",getMaxExp());
        config.set("attribute",getAllAttributeToSave());
        config.set("buffer",getAllBufferToSave());
        config.save();
    }
}
