package com.smallaswater.levelawakensystem;

import cn.nukkit.plugin.PluginBase;
import com.smallaswater.levelawakensystem.data.BaseHuman;
import com.smallaswater.levelawakensystem.listeners.PluginListener;
import com.smallaswater.levelawakensystem.listeners.WindowsListener;
import com.smallaswater.levelawakensystem.skills.Buffers;
import com.smallaswater.levelawakensystem.task.tasks.FixInventoryTask;
import com.smallaswater.levelawakensystem.task.tasks.RunTaskHandleTask;


import java.util.LinkedHashMap;


/**
 * @author SmallasWater
 */
public class LevelAwakenSystem extends PluginBase {


    public LinkedHashMap<String, BaseHuman> linkedHashMap = new LinkedHashMap<>();

    public LinkedHashMap<String, Buffers> buffers = new LinkedHashMap<>();

    public static LevelAwakenSystem INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE = this;
        this.getLogger().info("正在启动 Rpg插件..");
        this.getServer().getScheduler().scheduleRepeatingTask(new FixInventoryTask(this),20);
        this.getServer().getScheduler().scheduleRepeatingTask(new RunTaskHandleTask(this),20);
        //初始化
        this.getServer().getPluginManager().registerEvents(new WindowsListener(),this);
        this.getServer().getPluginManager().registerEvents(new PluginListener(),this);
        this.getLogger().info("正在加载语言文件");
    }






}
