package AwakenSystem.lib.removeItem;

import AwakenSystem.AwakenSystem;
import AwakenSystem.data.baseAPI;
import AwakenSystem.data.getFiles;
import AwakenSystem.lib.baseItem;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class CommandItems extends baseItem {
    private static String Name = "CommandItems";
    private String name = null;
    private static CommandItems api;

    public CommandItems(String name){
        this.name = name;
        api = this;
    }
    public static CommandItems getInstance(String name){
        new CommandItems(name);
        return api;
    }
    @Override
    public CompoundTag getCompoundTag(){
        return new CompoundTag()
                .putString("tag","LevelAwakenSystem"+Name)
                .putString("name",name);
    }

    public static boolean is_NbtItem(Item item){
        if(item == null) return false;
        if(item.hasCompoundTag()){
            CompoundTag tag = item.getNamedTag();
            if(tag.contains("tag") && tag.contains("name")){
                if(tag.getString("tag").equals("LevelAwakenSystem"+Name)){
                    return true;
                }
            }
        }
        return false;
    }
    //获取物品执行指令
    public LinkedHashMap<String,String> getUseCommand(){
        if(is_Exit(name)){
            LinkedHashMap<String,String> commands = new LinkedHashMap<>();
            for(Object s:AwakenSystem.getMain().
                    getCommandItemConfig(name).getList(baseAPI.ItemConfigType.sendCommand.getName())){
                String s1 = String.valueOf(s);
                if(s1.split("&").length == 2){
                    commands.put(s1.split("&")[0],s1.split("&")[1]);
                }
            }
            return commands;
        }
        return null;
    }

    public static boolean is_Exit(String name){
        if(name == null) {
            return false;
        }
        File file = new File(AwakenSystem.getMain().getDataFolder()+"/RPG/Items/"+name+".yml");
        return file.exists();
    }




    public static String getName(Item item){
        if(is_NbtItem(item)){
            return item.getNamedTag().getString("name");
        }
        return null;
    }
    @Override
    public Item getItem(int count){
        if(is_Exit(name)){
            String id = AwakenSystem.getMain().getCommandItemConfig(name).getString(baseAPI.ItemConfigType.ID.getName());
            String str = AwakenSystem.getMain().getCommandItemConfig(name).getString(baseAPI.ItemConfigType.Start.getName());
            if((id.split(":")).length < 2){
                id = id.split(":")[0]+":0";
            }
            Item item = new Item(Integer.parseInt((id.split(":"))[0]),Integer.parseInt((id.split(":")[1])),count);
            String[] lore = getLore();
            item.setNamedTag(getCompoundTag());
            if(lore != null)
                item.setLore(lore);
            else return null;

            item.setCustomName(str+name);
            if(AwakenSystem.getMain().getCommandItemConfig(name).getBoolean(baseAPI.ItemConfigType.Enchant.getName())){
                item.addEnchantment(Enchantment.get(1));
            }
            return item;
        }
        return null;
    }
    public Item getItem(){
        return getItem(1);
    }

    @Override
    public String[] getLore(){
        if(is_Exit(name)){
            ArrayList<String> lore = new ArrayList<>();
            lore.add("§l"+AwakenSystem.getMain().getCommandItemConfig(name).getString(baseAPI.ItemConfigType.Message.getName()).replace("\n","\n"));
            LinkedHashMap<String,String> commands = getUseCommand();
            if(commands != null){
                if(commands.size() > 0){
                    lore.add("§r§l§e>>§b---§c点§6地§e使§a用§b---§e<<");
                    lore.addAll(commands.values());
                }
            }
            return lore.toArray(new String[lore.size()]);
        }
        return null;
    }


    public void createItem(String id){
        Config conf = AwakenSystem.getMain().getCommandItemConfig(name);
        LinkedHashMap<String,Object> map = getFiles.initCommandItem(id);
        conf.setAll(map);
        conf.save();
    }


    public static LinkedList<Item> getItemInInventory(Player player){
        LinkedList<Item> list = new LinkedList<>();
        for(Item item:player.getInventory().getContents().values()){
            if(is_NbtItem(item) && is_Exit(getName(item))){
                list.add(item);
            }
        }
        return list;
    }

}
