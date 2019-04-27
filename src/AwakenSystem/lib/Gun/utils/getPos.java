package AwakenSystem.lib.Gun.utils;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class getPos {

    private Player player;
    public getPos(Player player){
        this.player = player;
    }

    public void getPosByLine(double line){
        Vector3 pos1 = player.getDirectionVector();
        player.setButtonText("1");
        double Up = Math.abs(pos1.getUp());//上下
        double forward = Math.abs(pos1.getForward());//左
        double right = Math.abs(pos1.getRight());//右
        player.sendPopup("up "+ Up);

        if(Up >= 0){

        }else{

        }
        //象限判断
        double raw = Math.toDegrees(player.yaw / 180 * Math.PI);
        if(raw > 0 && raw <= 90){
            // 1

        }else if(raw > 90 && raw <= 180){
            // 2

        }else if(raw > 180 && raw <= 270){
            // 3

        }else{
            // 4

        }


    }

}
