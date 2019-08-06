package AwakenSystem.lib;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;


public abstract class baseItem {

    private String Name = null;

    protected String name = null;


    public CompoundTag getCompoundTag(){
        return new CompoundTag()
                .putString("tag","LevelAwakenSystem"+Name)
                .putString("name",name);
    }

    public static boolean is_NbtItem(Item item){
        return false;
    }

    public static boolean is_Exit(String name){
        return false;
    }

    abstract public Item getItem(int count);


    abstract public String[] getLore();


}
