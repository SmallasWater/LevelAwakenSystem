package AwakenSystem.utils;
/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */

import AwakenSystem.AwakenSystem;
import AwakenSystem.data.baseAPI;
import AwakenSystem.data.defaultAPI;
import AwakenSystem.data.getFiles;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;


public class adminCommand extends Command{


    public adminCommand(String name) {
        super(name,"等级管理员指令","/lac help");
    }

    /**
     * 指令接口 可以实现多种修改方法 (限OP)
     * */
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender.isOp()){
            if(strings.length > 0){
                switch (strings[0]){
                    case "help":
                        commandSender.sendMessage("§b--------------§ka\\//a§r§b--------------");
                        commandSender.sendMessage("§e/lac help 查看帮助");
                        commandSender.sendMessage("§e/lac add <属性> <玩家> <数值> 增加玩家属性");
                        commandSender.sendMessage("§e/lac remove <属性> <玩家> <数量> 减少玩家属性");
                        commandSender.sendMessage("§e/lac set <属性> <玩家> <数量> 设置玩家属性");
                        commandSender.sendMessage("§e/lac setA <元素> <玩家>  设置玩家元素");
                        commandSender.sendMessage("§e/lac cleanAll <玩家> 重置玩家属性");
                        commandSender.sendMessage("§6属性:");
                        commandSender.sendMessage("§f    exp: 经验    health: 血量       dw: 物理攻击");
                        commandSender.sendMessage("§f    df: 法术攻击   dlw: 物理防御   dlf: 法术防御");
                        commandSender.sendMessage("§f    b: 暴击      kb: 抗暴          kx: 抗性");
                        commandSender.sendMessage("§f    c: 穿透      level: 等级       pf: 评级");
                        commandSender.sendMessage("§b--------------§ka\\//a§r§b--------------");
                        break;
                    case "add":
                        if(strings.length > 3){
                            Player player = Server.getInstance().getPlayer(strings[2]);
                            int math = Integer.parseInt(strings[3]);
                            if(player != null){
                                if(CommandType.contains(strings[1])){
                                    CommandType type = CommandType.valueOf(strings[1]);
                                    if(type.getType() instanceof baseAPI.PlayerConfigType){
                                        switch (type.getName()) {
                                            case "exp":
                                                defaultAPI.addExp(player, (float) math);
                                                break;
                                            case "level":
                                                defaultAPI.addLevel(player, math);
                                                break;
                                            case "pf":
                                                defaultAPI.addScore(player, math);
                                                break;
                                        }
                                    }else if(type.getType() instanceof baseAPI.PlayerAttType){
                                        defaultAPI.addPlayerAttributeInt(player.getName(),(baseAPI.PlayerAttType) type.getType(),math);
                                    }
                                }
                            }else{
                                commandSender.sendMessage(TextFormat.RED+"玩家不在线");
                            }
                        }else{
                            commandSender.sendMessage("用法:/lac help");
                        }
                        break;
                    case "remove":
                        if(strings.length > 3){
                            Player player = Server.getInstance().getPlayer(strings[2]);
                            int math = Integer.parseInt(strings[3]);
                            if(player != null){
                                if(CommandType.contains(strings[1])){
                                    CommandType type = CommandType.valueOf(strings[1]);
                                    if(type.getType() instanceof baseAPI.PlayerConfigType){
                                        defaultAPI.removePlayerAttributeInt(player.getName(),(baseAPI.PlayerConfigType)type.getType(),math);
                                    }else if(type.getType() instanceof baseAPI.PlayerAttType){
                                        defaultAPI.removePlayerAttributeInt(player.getName(),(baseAPI.PlayerAttType)type.getType(),math);
                                    }
                                }
                            }else {
                                commandSender.sendMessage(TextFormat.RED+"玩家不在线");
                            }
                        }else{
                            commandSender.sendMessage("用法:/lac help");
                        }
                        break;
                    case "set":
                        if(strings.length > 3){
                            Player player = Server.getInstance().getPlayer(strings[2]);
                            int math = Integer.parseInt(strings[3]);
                            if(player != null){
                                if(CommandType.contains(strings[1])){
                                    CommandType type = CommandType.valueOf(strings[1]);
                                    if(type.getType() instanceof baseAPI.PlayerConfigType){
                                        defaultAPI.setPlayerAttributeInt(player.getName(),(baseAPI.PlayerConfigType)type.getType(),math);
                                    }else if(type.getType() instanceof baseAPI.PlayerAttType){
                                        defaultAPI.setPlayerAttributeInt(player.getName(),(baseAPI.PlayerAttType)type.getType(),math);
                                    }
                                }
                            }else{
                                commandSender.sendMessage(TextFormat.RED+"玩家不在线");
                            }
                        }else{
                            commandSender.sendMessage("用法:/lac help");
                        }
                        break;
                    case "setA":
                        if(strings.length > 1){
                            Player player = Server.getInstance().getPlayer(strings[2]);
                            String att = strings[1];
                            if(player != null){
                                if(!att.equals("~")){
                                    if(defaultAPI.getAttTypeAll().contains(att)){
                                        defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,att);
                                    }else{
                                        commandSender.sendMessage(TextFormat.RED+att+"元素不存在");
                                    }
                                }else{
                                    defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,null);
                                }

                            }else {
                                commandSender.sendMessage(TextFormat.RED+"玩家不在线");
                            }
                        }else{
                            commandSender.sendMessage("用法:/lac help");
                        }

                        break;
                    case "cleanAll":
                        Player player = Server.getInstance().getPlayer(strings[1]);
                        if(player != null){
                            Config config = AwakenSystem.getMain().getPlayerConfig(player);
                            config.setAll(getFiles.initPlayer());
                            config.save();
                            player.attack(0.1F);
                        }else {
                            commandSender.sendMessage(TextFormat.RED+"玩家不在线");
                        }
                        break;
                }
            }else{
                commandSender.sendMessage("用法:/lac help");
            }
        }
        return true;
    }
    private enum CommandType{
        exp("exp", baseAPI.PlayerConfigType.EXP),
        health("health", baseAPI.PlayerAttType.HEALTH),
        dw("dw", baseAPI.PlayerAttType.DAMAGE_W),
        df("df", baseAPI.PlayerAttType.DAMAGE_F),
        dlw("dlw", baseAPI.PlayerAttType.DEFENSE_W),
        dlf("dlf",baseAPI.PlayerAttType.DEFENSE_F),
        b("b", baseAPI.PlayerAttType.CRriT),
        kb("kb", baseAPI.PlayerAttType.ANTI_RIOT),
        kx("kx", baseAPI.PlayerAttType.RESISTANCE),
        c("c", baseAPI.PlayerAttType.PENETRATION),
        level("level", baseAPI.PlayerConfigType.LEVEL),
        pf("pf", baseAPI.PlayerConfigType.TALENT);
        protected String name;
        protected Object type;
        CommandType(String name,Object type){
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Object getType() {
            return type;
        }
        public static boolean contains(String name){
            for (CommandType type:CommandType.values()){
                if(type.getName().equals(name))
                    return true;
            }
            return false;
        }
    }
}
