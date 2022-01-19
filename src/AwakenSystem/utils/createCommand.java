package AwakenSystem.utils;
/*
    _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |   <  __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|

 @author 若水

 */

import AwakenSystem.data.baseAPI;
import AwakenSystem.events.PlayerGetItemEvent;
import AwakenSystem.lib.removeItem.CommandItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;
import java.util.List;

public class createCommand extends Command{
    public createCommand(String name) {
        super(name,"小道具","/items help");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(commandSender.isOp()) {
            if (strings.length < 1 || strings.length > 4) {
                return false;
            }
            List<String> args = Arrays.asList(strings);
            switch (strings[0]) {
                case "help":
                    commandSender.sendMessage("§b--------§ka\\//a§r§b--------");
                    commandSender.sendMessage("§d/items add <道具名称> <ID:Damage> <类型(强化/使用/饰品/消耗)> 添加小道具");
                    commandSender.sendMessage("§d/items addhand <道具名称> <类型(强化/使用/饰品/消耗)> 把手上的物品添加为小道具");
                    commandSender.sendMessage("§d/items give <道具名称> <Player> 给玩家小道具");
                    commandSender.sendMessage("§d/items giveItem <名称> <Player> <count> 给玩家消耗品");
                    commandSender.sendMessage("§b--------§ka\\//a§r§b--------");
                    break;
                case "add":
                    if(args.get(3).equals("消耗")){
                        String itemName = args.get(1);
                        String id = args.get(2);
                        if(CommandItems.is_Exit(itemName)){
                            commandSender.sendMessage(TextFormat.RED + "这个小道具已存在");
                            return false;
                        }
                        CommandItems.getInstance(itemName).createItem(id);
                        commandSender.sendMessage(TextFormat.AQUA + "消耗品添加成功");
                        return true;
                    }
                    String itemName = args.get(1);
                    String id = args.get(2);
                    String type = args.get(3);
                    baseAPI.ITEM_TYPE item_type = baseAPI.ITEM_TYPE.UPDATA;
                    for (baseAPI.ITEM_TYPE t: baseAPI.ITEM_TYPE.values()){
                        if(t.getName().equals(type))
                            item_type = t;
                    }
                    // File f = new File(AwakenSystem.getMain().getDataFolder() + "/RPG/" + itemName + ".yml");
                    if (nbtItems.is_Exit(itemName)) {
                        commandSender.sendMessage(TextFormat.RED + "这个小道具已存在");
                        return false;
                    }
                    String[] del = id.split(":");
                    if (del.length != 2) {
                        del[1] = "0";
                    }
                    Item item = new Item(Integer.parseInt(del[0]), Integer.parseInt(del[1]));
                    nbtItems create = new nbtItems(itemName);
                    create.createItem(item, item_type);
                    commandSender.sendMessage(TextFormat.AQUA + "小道具添加成功");
                    break;
                case "addhand":
                    if(commandSender instanceof Player){
                        if(args.get(2).equals("消耗")){
                            itemName = args.get(1);
                            if(CommandItems.is_Exit(itemName)){
                                commandSender.sendMessage(TextFormat.RED + "这个小道具已存在");
                                return false;
                            }
                            Item item1 = ((Player) commandSender).getInventory().getItemInHand();
                            CommandItems.getInstance(itemName).createItem(item1.getId()+":"+item1.getDamage());
                            commandSender.sendMessage(TextFormat.AQUA + "消耗品添加成功");
                            return true;
                        }
                        String itemNames = args.get(1);
                        String types = args.get(2);
                        baseAPI.ITEM_TYPE item_type1 = baseAPI.ITEM_TYPE.UPDATA;
                        for (baseAPI.ITEM_TYPE t: baseAPI.ITEM_TYPE.values()){
                            if(t.getName().equals(types))
                                item_type1 = t;
                        }
                        Item item1 = ((Player) commandSender).getInventory().getItemInHand();
                        nbtItems creates = new nbtItems(itemNames);
                        creates.createItem(item1, item_type1);
                        commandSender.sendMessage(TextFormat.AQUA + "小道具添加成功");
                    }else{
                        commandSender.sendMessage(TextFormat.RED + "控制台无法添加手持物品");
                    }

                    break;
                case "give":
                    Player player1 = Server.getInstance().getPlayer(args.get(2));
                    String Name1 = args.get(1);
                    if (player1 == null) {
                        commandSender.sendMessage(TextFormat.RED + "该玩家不在线");
                        return false;
                    }
//                        File file = new File(AwakenSystem.getMain().getDataFolder()+"/RPG/"+Name1+".yml");
                    if(!nbtItems.is_Exit(Name1)){
                        commandSender.sendMessage(TextFormat.RED + "这个小道具不存在");
                        return false;
                    }
                    nbtItems items = new nbtItems(Name1);
                    Item itemS = items.toItem(Name1);
                    itemS.setCustomName(Name1);
                    player1.getInventory().addItem(itemS);
                    PlayerGetItemEvent event = new PlayerGetItemEvent(player1,itemS);
                    Server.getInstance().getPluginManager().callEvent(event);
                    commandSender.sendMessage(TextFormat.AQUA + "小道具给予成功");
                    break;
                case "giveItem":
                    Player player = Server.getInstance().getPlayer(args.get(2));
                    if(player !=null){
                        String name = args.get(1);
                        int count = 1;
                        if(args.size() == 4){
                            count = Integer.parseInt(args.get(3));
                        }
                        if(CommandItems.is_Exit(name)){
                            item = CommandItems.getInstance(name).getItem(count);
                            if(item != null){
                                PlayerGetItemEvent event1 = new PlayerGetItemEvent(player,item);
                                Server.getInstance().getPluginManager().callEvent(event1);
                                player.getInventory().addItem(item);
                            }else {
                                commandSender.sendMessage(TextFormat.RED + "给予失败");
                            }
                        }else{
                            commandSender.sendMessage(TextFormat.RED + "该消耗品不存在");
                            return false;
                        }
                    }else{
                        commandSender.sendMessage(TextFormat.RED + "该玩家不在线");
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
}
