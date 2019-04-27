package AwakenSystem.data;

import cn.nukkit.Player;
import cn.nukkit.item.Item;


import java.util.*;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class getFiles implements baseAPI{


    private static getFiles file;
    public getFiles(){
        file = this;
    }
    public static getFiles getFile() {
        return file;
    }



    public static LinkedHashMap<String,Object> initPlayer(){   /*   首次进服    */
        LinkedHashMap<String,Object> playerConfig = new LinkedHashMap<>();
        playerConfig.put(PlayerConfigType.LEVEL.getName(),1);
        playerConfig.put(PlayerConfigType.TALENT.getName(),1);
        playerConfig.put(PlayerConfigType.ATTRIBUTE.getName(),null);
        playerConfig.put(PlayerConfigType.EXP.getName(),0);
        playerConfig.put(PlayerConfigType.COUNT.getName(),0);
        for (PlayerAttType type:PlayerAttType.values()){
            playerConfig.put(type.getName(),0);
        }
        return playerConfig;
    }

    public static LinkedHashMap<String,Object> initItem(Item item, ITEM_TYPE itemType){
        LinkedHashMap<String,Object> ItemConfig = new LinkedHashMap<>();
        for (ItemType type:ItemType.values()){
            if(type.getName().equals(ItemType.ID.getName())){
                ItemConfig.put(type.getName(),item.getId()+":"+item.getDamage());
                continue;
            }
            if(type.getName().equals(ItemType.TYPE.getName())){
                ItemConfig.put(type.getName(),itemType.getName());
                continue;
            }
            ItemConfig.put(type.getName(),type.getDefaultADD());
            for (ItemADDType type1:ItemADDType.values()){
                if(itemType.getName().equals(ITEM_TYPE.USE.getName())){
                    ItemConfig.put(type1.getName(),type1.getDefaultADD()+":5:5");
                }else {
                    ItemConfig.put(type1.getName(),type1.getDefaultADD());
                }

            }
        }
        return ItemConfig;
    }

    public static LinkedHashMap<String,Object> initCommandItem(String id){
        LinkedHashMap<String,Object> ItemConfig = new LinkedHashMap<>();
        for (ItemConfigType type:ItemConfigType.values()){
            if(type == ItemConfigType.ID){
                ItemConfig.put(type.getName(),id);
                continue;
            }
            ItemConfig.put(type.getName(),type.getType());
        }
        return ItemConfig;
    }
}
