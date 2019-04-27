package AwakenSystem.utils;


import AwakenSystem.data.defaultAPI;
import AwakenSystem.data.uiAPI;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */
public class Commands extends Command{
    public Commands(String name) {
        super(name,"等级系统","level");
    }
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        uiAPI api = new uiAPI();
        if(strings.length == 3){
            if(strings[0].equals("exp")){
                if(commandSender.isOp()){
                    Player player = Server.getInstance().getPlayer(strings[1]);
                    if(player != null){
                        float exp = Float.parseFloat(strings[2]);
                        defaultAPI.addExp(player,exp);
                    }
                }
            }
            return true;
        }
        if(commandSender instanceof Player)
            api.sendFrom((Player) commandSender);
        return true;
    }
}
