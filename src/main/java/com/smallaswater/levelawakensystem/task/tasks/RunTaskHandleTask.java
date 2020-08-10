package com.smallaswater.levelawakensystem.task.tasks;

import cn.nukkit.Player;
import com.smallaswater.levelawakensystem.LevelAwakenSystem;
import com.smallaswater.levelawakensystem.data.BaseHuman;
import com.smallaswater.levelawakensystem.task.BaseTask;

/**
 * @author SmallasWater
 */
public class RunTaskHandleTask extends BaseTask {
    public RunTaskHandleTask(LevelAwakenSystem owner) {
        super(owner);
    }

    @Override
    public void runPlayer(Player player) {
        if(LevelAwakenSystem.INSTANCE.linkedHashMap.containsKey(player.getName())){
            BaseHuman human = LevelAwakenSystem.INSTANCE.linkedHashMap.get(player.getName());
            if(human.getHandles().size() > 0){
                human.getHandles().forEach(handle->{
                    if(!handle.isClose()) {
                        handle.runTime();
                    }else{
                        human.getHandles().remove(handle);
                    }
                });
            }
        }
    }
}
