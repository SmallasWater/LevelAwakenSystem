package AwakenSystem.lib.Gun.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

import java.util.LinkedList;


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


    public Position getPosByLine(double line){
        return player.getTargetBlock((int)line);
    }

    public LinkedList<Position> mathLine(Position blockPos){
        Position playerPos = new Position(player.x,player.y+player.getEyeHeight(),player.z,player.level);
        LinkedList<Position> positions = new LinkedList<>();
        double sd = Math.pow(playerPos.x-blockPos.x,2)+Math.pow(playerPos.y-blockPos.y,2)+Math.pow(playerPos.z-blockPos.z,2);
        int dis=(int)Math.sqrt(sd);

        for(int t = 0;t <= 1; t +=(1/(dis))){
            positions.add(new Position((playerPos.x+(blockPos.x - playerPos.x) * t)
                    ,(playerPos.y + (blockPos.y - playerPos.y) * t),(playerPos.z + (blockPos.z - playerPos.z) * t)));
        }
        return positions;
    }

    //检测碰撞

    public boolean canAttack(Position position,Player player){
        return (position.x > player.x - 0.5 && position.x < player.x + 0.5)
                && (position.y > player.y && position.y < player.y + player.getEyeHeight())
                && (position.z > player.z - 0.2 && position.z < player.z + 0.2);

    }





}
