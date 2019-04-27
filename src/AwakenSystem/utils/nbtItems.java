package AwakenSystem.utils;

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
import AwakenSystem.data.defaultAPI;
import AwakenSystem.data.getFiles;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class nbtItems implements baseAPI {

    private static nbtItems items;
    private String name;
    nbtItems(String Item){
        items = this;
        name = Item;
    }

    public static nbtItems getItems() {
        return items;
    }

    void createItem(Item ItemName, ITEM_TYPE item_type){
        Config conf = AwakenSystem.getMain().getItemConfig(name);
        LinkedHashMap<String,Object> map = getFiles.initItem(ItemName,item_type);
        conf.setAll(map);
        conf.save();
    }

    public static boolean is_Exit(String name){
        File file = new File(AwakenSystem.getMain().getDataFolder()+"/RPG/"+name+".yml");
        return file.exists();
    }

    public void createItem(Item item,String name){
        this.name = name;
        this.createItem(item,ITEM_TYPE.UPDATA);
    }

    public void createItem(String name){
        this.name = name;
        Item item = new Item(Integer.parseInt(ItemType.ID.getDefaultADD().split(":")[0]),
                Integer.parseInt(ItemType.ID.getDefaultADD().split(":")[1]));
        this.createItem(item,ITEM_TYPE.UPDATA);
    }


    private static String[] getLore(String itemName){
        ArrayList<String> lore = new ArrayList<>();
        lore.add(defaultAPI.getItemConfigString(itemName,ItemType.MESSAGE).replace("\n","\n"));
        String type = defaultAPI.getItemConfigString(itemName,ItemType.TYPE);
        if(type.equals(ITEM_TYPE.ORNAMENTS.getName())){
            lore.add("§e饰品");
        }else{
            lore.add("§d点地使用");
        }
        lore.add("§r§c▂§6▂§e▂§a▂§b▂§9▂§d▂");
        lore.add("§r§e品质:  "+defaultAPI.getItemConfigString(itemName,ItemType.STAR));
        if(defaultAPI.getItemConfigString(itemName,ItemType.ATT).equals("null") && defaultAPI.getItemConfigString(itemName,ItemType.LEVEL).equals("0")){
            lore.add("§r§c限制条件: 无");
        }else if(!defaultAPI.getItemConfigString(itemName,ItemType.ATT).equals("null") && !defaultAPI.getItemConfigString(itemName,ItemType.LEVEL).equals("0")){
            lore.add("§r§c限制条件:  §c"+defaultAPI.getItemConfigString(itemName,ItemType.ATT)+"\n§cLv."+defaultAPI.getItemConfigString(itemName,ItemType.LEVEL));
        }else if(!defaultAPI.getItemConfigString(itemName,ItemType.ATT).equals("null")){
            lore.add("§r§c限制条件:  §c"+defaultAPI.getItemConfigString(itemName,ItemType.ATT));
        }else if(!defaultAPI.getItemConfigString(itemName,ItemType.LEVEL).equals("0")){
            lore.add("§r§c限制条件:  §cLv."+defaultAPI.getItemConfigString(itemName,ItemType.LEVEL));
        }
        lore.add("§r§c▂§6▂§e▂§a▂§b▂§9▂§d▂");
        StringBuilder buffer = new StringBuilder("");
        if(type.equals(ITEM_TYPE.ORNAMENTS.getName())){
            lore.add("§6佩戴后: ");
            for (ItemADDType type1:ItemADDType.values()){
                if(!defaultAPI.getItemConfigString(itemName, type1).equals("0")){
                    if(type1.getName().equals(ItemADDType.EXP.getName())){
                        buffer.append("§r").append(type1.getName()).append(": + ").append(defaultAPI.getItemConfigString(itemName, type1)).append(" ％\n");
                        continue;
                    }
                    buffer.append("§r").append(type1.getName()).append(": + ").append(defaultAPI.getItemConfigString(itemName, type1)).append(" 点\n");

                }

            }
        }else if(type.equals(ITEM_TYPE.UPDATA.getName())){
            lore.add("§6使用后: (永久增加)");
            for (ItemADDType type1:ItemADDType.values()){
                if(!defaultAPI.getItemConfigString(itemName, type1).equals("0")){
                    buffer.append("§r").append(type1.getName()).append(": + ").append(defaultAPI.getItemConfigString(itemName, type1)).append(" \n");
                }
            }
        }else{
            lore.add("§6使用后: ");
            for (ItemADDType type1:ItemADDType.values()){
                String[] array = defaultAPI.getItemConfigString(itemName, type1).split(":");
                if(array.length == 3){
                    if(!array[0].equals("0")){
                        buffer.append("§r").append(type1.getName()).append(" + ").append(array[0]).append("§d持续: ").append(array[1]).append("秒 ").append("§c冷却: ").append(array[2]).append("秒\n");
                    }
                }
            }
        }
        lore.add(buffer.toString());
        return lore.toArray(new String[lore.size()]);
    }

    //仅限于 各种e类型
    public static LinkedHashMap<ItemADDType,Integer> getColde_by_Ring(String itemName){
         LinkedHashMap<ItemADDType,Integer> array = new LinkedHashMap<>();
        for (ItemADDType types:ItemADDType.values()){
            if(!defaultAPI.getItemConfigString(itemName,types).equals("0"))
                array.put(types,Integer.parseInt(defaultAPI.getItemConfigString(itemName,types)));
        }
        return array;
    }
    public static LinkedHashMap<ItemADDType,ArrayList<Integer>> getColde_by_use(String itemName){
        LinkedHashMap<ItemADDType,ArrayList<Integer>> array = new LinkedHashMap<>();
        for (ItemADDType types:ItemADDType.values()){
            String[] list = defaultAPI.getItemConfigString(itemName,types).split(":");
            if(list.length == 3){
                if(!list[0].equals("0")){
                    ArrayList<Integer> l = new ArrayList<>();
                    for(String s:list){
                        l.add(Integer.valueOf(s));
                    }
                    array.put(types,l);
                }
            }
        }
        return array;
    }


    Item toItem(String name){
        String[] itemArray = defaultAPI.getItemConfigString(name,ItemType.ID).split(":");
        Item item = Item.get(Integer.parseInt(itemArray[0]),Integer.parseInt(itemArray[1]),1);
        item.setCustomName(name);
        item.setNamedTag(this.getCompoundTag());
        item.setLore(getLore(name));
        return item;
    }

    private CompoundTag getCompoundTag(){
         return new CompoundTag()
                .putString("tag","LevelAwakenSystem")
                .putString("name",this.name);
    }

    public CompoundTag getCompoundTag(String name){
        this.name = name;
        return this.getCompoundTag();
    }

    public static boolean is_Item(Item item){
        if(item == null) return false;
        if(item.hasCompoundTag()){
            CompoundTag tag = item.getNamedTag();
            if(tag.contains("tag") && tag.contains("name")){
                if(tag.getString("tag").equals("LevelAwakenSystem")){
                    return true;
                }
            }
        }
        return false;
    }

    public static String getName(Item item){
        if(is_Item(item)){
            return item.getNamedTag().getString("name");
        }
        return null;
    }

    public static boolean is_use(Item item){
        if(is_Item(item) && is_Exit(getName(item))){
            String name = getName(item);
            return AwakenSystem.getMain().getItemConfig(name).getString(ItemType.TYPE.getName()).equals(ITEM_TYPE.USE.getName());
        }
        return false;
    }
    public static boolean is_add(Item item){
        if(is_Item(item) && is_Exit(getName(item))){
            String name = getName(item);
            return AwakenSystem.getMain().getItemConfig(name).getString(ItemType.TYPE.getName()).equals(ITEM_TYPE.UPDATA.getName());
        }
        return false;
    }
    public static boolean is_ring(Item item){
        if(is_Item(item) && is_Exit(getName(item))){
            String name = getName(item);
            return AwakenSystem.getMain().getItemConfig(name).getString(ItemType.TYPE.getName()).equals(ITEM_TYPE.ORNAMENTS.getName());
        }
        return false;
    }


    public static boolean can_use(Player player,Item item){
        if(is_Item(item) && is_Exit(getName(item))){
            if(is_Exit(getName(item))){
                int level = defaultAPI.getPlayerAttributeInt(player.getName(),PlayerConfigType.LEVEL);
                String att= defaultAPI.getPlayerAttributeString(player.getName(),PlayerConfigType.ATTRIBUTE);
                if(!defaultAPI.getItemConfigString(getName(item),ItemType.ATT).equals("null")
                        && !defaultAPI.getItemConfigString(getName(item),ItemType.LEVEL).equals("0")){
                    if(att.equals(defaultAPI.getItemConfigString(getName(item),ItemType.ATT)) &&
                            level >= Integer.parseInt(defaultAPI.getItemConfigString(getName(item),ItemType.LEVEL))){
                        return true;
                    }
                }else if (!defaultAPI.getItemConfigString(getName(item), ItemType.ATT).equals("null")) {
                    return att.equals(defaultAPI.getItemConfigString(getName(item), ItemType.ATT));
                } else
                    return defaultAPI.getItemConfigString(getName(item), ItemType.LEVEL).equals("0")
                            || level >= Integer.parseInt(defaultAPI.getItemConfigString(getName(item), ItemType.LEVEL));
            }
        }
        return false;
    }

}
