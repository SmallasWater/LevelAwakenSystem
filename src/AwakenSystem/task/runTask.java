package AwakenSystem.task;


import AwakenSystem.AwakenSystem;
import AwakenSystem.data.baseAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import java.util.LinkedHashMap;


/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class runTask extends Task{



    //   用作小道具计时器
    @Override
    public void onRun(int i) {
        //专门算冷却
        //
        for(Player player: Server.getInstance().getOnlinePlayers().values()){

            if(AwakenSystem.getMain().runAdd.containsKey(player)){
                LinkedHashMap<String,LinkedHashMap<baseAPI.ItemADDType,int[]>> map = AwakenSystem.getMain().runAdd.get(player);
                for (String name : map.keySet()){
                    int c = 0;
                    LinkedHashMap<baseAPI.ItemADDType,int[]> delTime = map.get(name);
                    int count = delTime.size();
                    for (baseAPI.ItemADDType type: baseAPI.ItemADDType.values()){
                        if(delTime.containsKey(type)){
                            int[] time = delTime.get(type);
                            if(time[1] > 0 && time[1] < 999999){
                                time[1] --;
                            }
                            if(time[2] > 0  && time[1] < 999999){
                                time[2] --;
                            }
                            if(time[1] <= 0 && time[2] <= 0){
                                c++;
                                delTime.remove(type);
                            }else{
                                delTime.put(type,time);
                            }
                        }
                    }
                    if(c != count){
                        map.put(name,delTime);
                    }else{
                        player.attack(1);
                        map.remove(name);
                    }
                }
                AwakenSystem.getMain().runAdd.put(player,map);
            }
        }
    }
}
