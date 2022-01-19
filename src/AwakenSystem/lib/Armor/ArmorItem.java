package AwakenSystem.lib.Armor;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
// 终于打算写盔甲了....

import AwakenSystem.lib.baseItem;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

public class ArmorItem extends baseItem {


    private static String Name = "Armor";

    private Item item;

    public ArmorItem(String name) {
        this.name = name;
    }

    @Override
    public Item getItem(int count) {
        return null;
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }

    @Override
    public CompoundTag getCompoundTag(){
        return super.getCompoundTag();

    }







}
