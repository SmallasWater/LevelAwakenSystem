package com.smallaswater.levelawakensystem.task;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import com.smallaswater.levelawakensystem.LevelAwakenSystem;


/**
 * @author SmallasWater
 */
public abstract class BaseTask extends PluginTask<LevelAwakenSystem> {


    public BaseTask(LevelAwakenSystem owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            Server.getInstance().getScheduler().scheduleAsyncTask(LevelAwakenSystem.INSTANCE, new AsyncTask() {
                @Override
                public void onRun() {
                    runPlayer(player);
                }
            });
        }
    }



    /**
     * 调用玩家
     * @param player 玩家
     * */
    abstract public void runPlayer(Player player);
}
