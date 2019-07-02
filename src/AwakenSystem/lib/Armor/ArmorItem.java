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

public class ArmorItem extends baseItem {


    private static String Name = "Armor";

    public ArmorItem(String name) {
        super(name);
    }

    @Override
    public Item getItem(int count) {
        return null;
    }

    @Override
    public String[] getLore() {
        return new String[0];
    }






}
