package AwakenSystem.lib.Gun.utils;

import cn.nukkit.scheduler.Task;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class GunTask extends Task{
    /*
    * public function line($info){
  $pos=array();
  $sd=pow($info['X']-$info['X1'],2)+pow($info['Y']-$info['Y1'],2)+pow($info['Z']-$info['Z1'],2);
  $dis=(int)sqrt($sd);
  for($t=0;$t<=1;$t+=(1/($dis))){
   $pos[]=array($info['X']+($info['X1']-$info['X'])*$t,$info['Y']+($info['Y1']-$info['Y'])*$t,$z=$info['Z']+($info['Z1']-$info['Z'])*$t);
  }
  return $pos;
 }*/

    @Override
    public void onRun(int i) {

    }
}
