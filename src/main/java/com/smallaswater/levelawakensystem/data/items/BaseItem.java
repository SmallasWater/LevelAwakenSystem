package com.smallaswater.levelawakensystem.data.items;

import cn.nukkit.item.Item;

/**
 * @author SmallasWater
 */
public abstract class BaseItem {

    private Item item;

    private String name;

    public BaseItem(String name,Item item){
        this.item = item;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
