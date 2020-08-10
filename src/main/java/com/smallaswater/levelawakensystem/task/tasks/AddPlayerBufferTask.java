package com.smallaswater.levelawakensystem.task.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import com.smallaswater.levelawakensystem.LevelAwakenSystem;
import com.smallaswater.levelawakensystem.data.BaseHuman;
import com.smallaswater.levelawakensystem.data.effects.BaseBuffer;
import com.smallaswater.levelawakensystem.data.effects.defaults.MinecraftBuffer;
import com.smallaswater.levelawakensystem.data.effects.defaults.PlayerBuffer;
import com.smallaswater.levelawakensystem.data.math.Calculator;
import com.smallaswater.levelawakensystem.data.players.PlayerData;
import com.smallaswater.levelawakensystem.skills.Buffers;
import healthapi.PlayerHealth;

/**
 * @author SmallasWater
 */
public class AddPlayerBufferTask extends PluginTask<LevelAwakenSystem> {


    public AddPlayerBufferTask(LevelAwakenSystem owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
       for(BaseHuman human : LevelAwakenSystem.INSTANCE.linkedHashMap.values()){
           for(BaseBuffer buffer : human.getBuffers()){
               if(buffer instanceof MinecraftBuffer){
                   if(human.getEntity() != null) {
                       if(!human.getEntity().hasEffect(((MinecraftBuffer) buffer).getEffect().getId())) {
                           human.getEntity().addEffect(((MinecraftBuffer) buffer).getEffect());
                       }
                   }
               }
               if(buffer instanceof PlayerBuffer){
                   if(owner.buffers.containsKey(buffer.getBufferName())){
                       Buffers buffers = owner.buffers.get(buffer.getBufferName());
                       if(buffers.getDefaultAttribute().containsKey(PlayerData.PlayerDefaultAttribute.health)){
                           if(human.getEntity() != null){
                               if(Server.getInstance().getPluginManager().getPlugin("HealthAPI") != null){
                                   PlayerHealth health = PlayerHealth.getPlayerHealth((Player) human.getEntity());
                                   health.setHealth((float) Calculator.mathRound(health.getHealth(),buffers
                                           .getDefaultAttribute().get(PlayerData.PlayerDefaultAttribute.health)));
                               }else{
                                   human.getEntity().setHealth((float) Calculator.mathRound(human.getEntity().getHealth(),buffers.getDefaultAttribute()
                                           .get(PlayerData.PlayerDefaultAttribute.health)));
                               }

                           }
                       }
                   }
               }
           }
       }
    }
}
