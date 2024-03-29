package AwakenSystem;


import AwakenSystem.data.*;
import AwakenSystem.events.*;
import AwakenSystem.lib.removeItem.CommandItems;
import AwakenSystem.utils.BossBarAPI;
import AwakenSystem.utils.nbtItems;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.*;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import healthapi.PlayerHealth;
import me.onebone.economyapi.EconomyAPI;

import java.io.File;
import java.util.*;
/*
  _                   _    _                _
 | |    _____   _____| |  / \__      ____ _| | _____ _ __
 | |   / _ \ \ / / _ \ | / _ \ \ /\ / / _` | |/ / _ \ '_ \
 | |__|  __/\ V /  __/ |/ ___ \ V  V / (_| |     __/ | | |
 |_____\___| \_/ \___|_/_/   \_\_/\_/ \__,_|_|\_\___|_| |_|
 */


public class PlayerEvents implements Listener {

    @EventHandler
    public void onPvpDamage(PlayerAttackEvent event){
        if(event.isCancelled()) {
            return;
        }
        Entity damager = (event).getDamager();

        Entity entity = event.getEntity();

        DamageMath math = new DamageMath();
        if(damager instanceof Player && entity instanceof Player){
            int[] damage = math.getDamage_byPVP((Player) damager,(Player) entity,
                    new float[]{( event).getDamageW(),(event).getDamageF()});
            HashMap<String,Integer> map = math.getCause();

            if(map != null){
                if(map.containsKey("暴击")){
                    damage[0] += map.get("暴击");


                }
                if(map.containsKey("克制")){
                    damage[1] += map.get("克制");
                    map.remove("克制");
                }
                if(map.containsKey("被克制")){
                    damage[1] -= map.get("被克制");
                    map.remove("被克制");
                }
            }
            int delDamageFinal = math.getDelDamage_byPVP((Player) damager,(Player) entity,damage);
            if(delDamageFinal <= 0){
                delDamageFinal = 1;
            }
            if(map != null){
                if(map.containsKey("暴击")){
                    double deltaY = entity.x - damager.x;
                    double deltaZ = entity.z - damager.z;
                    double yaw = Math.atan2(deltaY, deltaZ);
                    ((Player) entity).knockBack(damager,1,Math.sin(yaw),Math.cos(yaw), 0.5D);
                    entity.getLevel().addSound(entity.getPosition(),Sound.CAULDRON_EXPLODE);
                    entity.level.addParticle(new DestroyBlockParticle(new Vector3(entity.x, entity.y, entity.z),
                            Block.get(152,0)));
                    map.remove("暴击");
                }
            }
            event.setDamage(delDamageFinal);
        }else if(damager instanceof Player){
            event.setDamage(event.getDamageF() + event.getDamageW());
        }
    }
    @EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (event instanceof PlayerAttackEvent) {
            Entity damager = ((PlayerAttackEvent) event).getDamager();
            if (damager instanceof Player && entity instanceof Player) {
                int eLevel = defaultAPI.getPlayerAttributeInt(entity.getName(), baseAPI.PlayerConfigType.LEVEL);
                int dLevel = defaultAPI.getPlayerAttributeInt(damager.getName(), baseAPI.PlayerConfigType.LEVEL);
                if (Math.abs(dLevel - eLevel) > AwakenSystem.getMain().getConfig().getInt(baseAPI.ConfigType.getPVPLevel.getName(), 10)) {
                    event.setCancelled();
                    ((Player) damager).sendMessage("§c目标与你的等级相差过大！");
                    return;
                }
            }
            return;
        }
        if (Server.getInstance().getPluginManager().getPlugin("HealthAPI") == null) {
            if (entity instanceof Player && AwakenSystem.getMain().defaultMaxHealth.containsKey(entity)) {
                int mh = AwakenSystem.getMain().defaultMaxHealth.get(entity);

                if (entity.getMaxHealth() != mh + defaultAPI.getPlayerFinalAttributeInt((Player) entity, baseAPI.ItemADDType.HEALTH)) {
                    entity.setMaxHealth(mh + defaultAPI.getPlayerFinalAttributeInt((Player) entity, baseAPI.ItemADDType.HEALTH));
                }

            }
        }else {
            if (entity instanceof Player) {
                PlayerHealth health = PlayerHealth.getPlayerHealth((Player) entity);
                int add =  defaultAPI.getPlayerFinalAttributeInt((Player) entity, baseAPI.ItemADDType.HEALTH);
                health.setMaxHealth("LevelAwakenSystem",add);
            }
        }
        if(event instanceof EntityDamageByEntityEvent){

            if(event.isCancelled()) {
                return;
            }
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if(damager instanceof Player && entity instanceof Player){
                event.setDamage(defaultAPI.addPlayerAttack((Player)damager,entity,event.getDamage(),0).getDamage());
                return;
            }
            if(entity instanceof Player){
                float damage = event.getDamage();

                if(defaultAPI.getPlayerFinalAttributeInt(
                        (Player) entity, baseAPI.ItemADDType.DEFENSE_W) > 0){
                    damage -= defaultAPI.getPlayerFinalAttributeInt(
                            (Player) entity, baseAPI.ItemADDType.DEFENSE_W);
                }
                if(damage <= 0){

                    damage = 1;
                }
                event.setDamage(damage);
                return;
            }
            if(damager instanceof Player){
                float finalDamage = event.getDamage();
                int damage = defaultAPI.getPlayerFinalAttributeInt((Player) damager, baseAPI.ItemADDType.DAMAGE_W);
                int f = 0;
                if(!defaultAPI.getPlayerAttributeString(damager.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")){
                    f = defaultAPI.getPlayerFinalAttributeInt((Player) damager, baseAPI.ItemADDType.DAMAGE_F);
                }
                float d = damage + f + finalDamage;
                if(d <= 0){
                    d = 1;
                }
                event.setDamage(d);

//                ((Player) damager).sendPopup("§f- §b"+(int)event.getFinalDamage()+"\n\n\n\n\n\n\n\n\n");
            }
        }
    }







    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        File file = new File(AwakenSystem.getMain().getPlayerFileName(player));
        BossBarAPI api = null;
        if(AwakenSystem.getMain().getConfig().getBoolean(baseAPI.ConfigType.can_show_Boss.getName())){
            api = new BossBarAPI(player);
        }

        if(!file.exists()){
            Server.getInstance().getLogger().info("正在初始化"+player.getName());
            LinkedHashMap<String,Object> conf = getFiles.initPlayer();
            Config config = AwakenSystem.getMain().getPlayerConfig(player);
            config.setAll(conf);
            config.save();
            if(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT) > 3){
                List<String> list = AwakenSystem.getMain().getConfig().getStringList(baseAPI.ConfigType.SETTING_CHAT.getName());
                String ta = list.get(defaultAPI.getPlayerAttributeInt(player.getName(),baseAPI.PlayerConfigType.TALENT));
                Server.getInstance().broadcastMessage("§d 恭喜玩家 "+player.getName()+"获得了 "+ta+"§c 的天赋 §a未来前途不可限量");
            }
            defaultHealth(player, api);
        }else{
            //获取基础生命
            defaultHealth(player, api);

        }
    }

    private void defaultHealth(Player player, BossBarAPI api) {
        if(Server.getInstance().getPluginManager().getPlugin("HealthAPI") != null){
            PlayerHealth health = PlayerHealth.getPlayerHealth(player);
            AwakenSystem.getMain().defaultMaxHealth.put(player,player.getMaxHealth());
            int add = defaultAPI.getPlayerFinalAttributeInt(player, baseAPI.ItemADDType.HEALTH);
            health.setMaxHealth("LevelAwakenSystem",add);
        }else{
            if(!AwakenSystem.getMain().defaultMaxHealth.containsKey(player)){
                AwakenSystem.getMain().defaultMaxHealth.put(player,player.getMaxHealth());
                player.setMaxHealth(player.getMaxHealth() + defaultAPI.getPlayerFinalAttributeInt(player, baseAPI.ItemADDType.HEALTH));

            }
        }

        if(AwakenSystem.getMain().getConfig().getBoolean(baseAPI.ConfigType.can_show_Boss.getName())){
            if(api != null){
                api.createBossBar();
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onAddExp(PlayerAddExpEvent event){
        Player player = event.getPlayer();
        if(player.getGamemode() == 1){
            event.setCancelled(true);
            player.sendMessage("§c创造模式不增加经验");
            return;
        }
        int exp = (int) event.getExp();
        if(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL) >=
                AwakenSystem.getMain().getConfig().getInt(baseAPI.ConfigType.LEVEL_MAX.getName())){
            event.setCancelled(true);
            player.sendTip("§c你满级啦 不再获取经验");
            return;
        }

        player.sendTip("§e 恭喜你获得 "+event.getExp()+"点经验");
        player.level.addSound(player.getPosition(), Sound.RANDOM_LEVELUP);
        int maxExp = DamageMath.getUpDataEXP(player);
        int playerExp =  defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.EXP);
        if((exp + playerExp) >= maxExp){
            PlayerLevelUpEvent event1 = new PlayerLevelUpEvent(player,
                    defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL),1);
            Server.getInstance().getPluginManager().callEvent(event1);
            int delexp = (exp + playerExp) - maxExp;
            if(delexp > 0) {
                PlayerAddExpEvent expEvent = new PlayerAddExpEvent(event.getPlayer(), delexp);
                Server.getInstance().getPluginManager().callEvent(expEvent);
            }
        }else{
            exp += defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.EXP);
            defaultAPI.setPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.EXP,exp);
        }
    }
    @EventHandler
    public void SuccessUpData(PlayerLevelUpEvent event){
        Player player = event.getPlayer();
        int level = defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL);
        HashMap map = (HashMap) AwakenSystem.getMain().getConfig().get(baseAPI.ConfigType.UPDATA.getName());
        if(map.containsKey(level+"")){
            player.sendMessage("§l§b[系统]§d恭喜你升到 "+level+"级");
            ArrayList list = (ArrayList) map.get(level+"");
            for (Object it:list){
                String[] su = String.valueOf(it).split("&");
                String name = null;
                if(su.length >= 2){
                    if(su.length == 3){
                        name = su[2];
                    }
                    switch (su[1]){
                        case "item":
                            String[] items = su[0].split(":");
                            Item item = Item.get(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]));
                            player.getInventory().addItem(item);
                            if(name != null){
                                player.sendMessage("§l§e◎ - 你获得了 "+name+" * "+items[2]);
                            }else{
                                player.sendMessage("§l§e◎ - 你获得了 "+items[0]+":"+items[1]+" * "+items[2]);
                            }

                            break;
                        case "money":
                            int money = Integer.parseInt(su[0]);
                            EconomyAPI.getInstance().addMoney(player,money);
                            if(name == null){
                                name = "金币";
                            }
                            player.sendMessage("§l§e◎ - 你获得了 "+money+name);
                            break;
                        case "cmd":
                            String cmd = su[0].replace("@p",player.getName());

                            Server.getInstance().dispatchCommand(new ConsoleCommandSender(),cmd);
                            if(name != null){
                                player.sendMessage("§l§e◎ - 你获得了 "+name);
                            }
                            break;
                            default:break;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void levelUp(PlayerLevelUpEvent event){
        Player player = event.getPlayer();
        defaultAPI.setPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.EXP,0);
        StringBuilder text = new StringBuilder("");

        HashMap<baseAPI.PlayerAttType,Integer> upData = defaultAPI.upDataAddAttribute(player.getName());
        for (baseAPI.PlayerAttType type: baseAPI.PlayerAttType.values()){
            text.append("§d").append(type.getName()).append(" ").append(defaultAPI.getPlayerAttributeInt(player.getName(), type)).
                    append("§6 -> §a").append(upData.get(type) + defaultAPI.getPlayerAttributeInt(player.getName(),type)).append("\n§r");
            defaultAPI.setPlayerAttributeInt(player.getName(),type,
                    (upData.get(type) + defaultAPI.getPlayerAttributeInt(player.getName(),type)));
        }
        defaultAPI.setPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL,event.getAddLevel() + event.getOldLevel());
        player.attack(1);
        if(event.isShowUi()){
            player.sendMessage(text.toString());
//            player.level.addSound(player.getPosition(), Sound.CAULDRON_EXPLODE);
//            uiAPI uiAPI = new uiAPI();
//            uiAPI.sendMenuMessage(player,text.toString());
        }
    }
    private boolean canRemove(Player player,LinkedList<Item> upItem){
        if(upItem.size() > 0){
            int size = 0;
            for(Item item:upItem){
                Item i = in_Inventory(player,item);
                if(i != null && i.getCount() >= item.getCount()){
                    size++;
                }
            }
            if(size != upItem.size()){
                player.sendMessage(TextFormat.RED+"物品不足 需要");
                for(Item item:upItem){
                    player.sendMessage(TextFormat.RED+item.getCustomName()+" * "+item.getCount());
                }
                return false;
            }else{
                player.sendMessage(TextFormat.YELLOW+"扣除》》");

                for(Item item:upItem){
                    player.getInventory().removeItem(item);
                    player.sendMessage(TextFormat.YELLOW+"§e◎- "+item.getCustomName()+" * "+item.getCount());
                }
            }
        }
        return true;
    }

    private boolean isAwaken(Player player, String att) {
        LinkedList<Item> upItem = defaultAPI.getUseItem_Awaken(att);
        return !(upItem != null && upItem.size() > 0) || canRemove(player, upItem);

    }
    //判断物品是否在背包 有则返回物品 没有返回null
    private Item in_Inventory(Player player,Item item){
        for (Item item1:player.getInventory().getContents().values()){
            if(item1.equals(item,true,true)) {
                return item1;
            }
        }
        return null;
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event){
        for(Config config:AwakenSystem.getMain().playerConfig.values()){
            config.save();
        }
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void playerAwaken(PlayerAwakenEvent event){
        Player player = event.getPlayer();
        if(event.isCancelled()){
            player.sendMessage(TextFormat.RED+"抱歉，，觉醒失败");
            return;
        }
        String att = event.getElement();
        if(isAwaken(player,att)){
            EconomyAPI.getInstance().reduceMoney(player,DamageMath.getReduceMoney(player));
            int random = new Random().nextInt(100) + 1;
            int r = DamageMath.getAwaken(player);
            if(defaultAPI.getAwakenConfig(att).containsKey("成功率")){
                r = (int) defaultAPI.getAwakenConfig(att).get("成功率");
            }
            if(random <= r){
                Server.getInstance().broadcastMessage("§d恭喜玩家 "+player.getName()+"觉醒成功 "+"§6获得了§e"+att+"§6的魔法属性");
                defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,att);
                defaultAPI.addPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.COUNT,1);
                player.sendMessage(att+"元素介绍: §b"+defaultAPI.getMessages(att));
            }else{
                player.sendMessage("§c抱歉 ~~ 觉醒失败啦 再试一次");
                defaultAPI.addPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.COUNT,1);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        HashMap<String,Object> arrays = (HashMap<String, Object>) AwakenSystem.getMain().getConfig().getAll();
        Map map = (Map) arrays.get(baseAPI.ConfigType.EXP_ORE.getName());
        Block block = event.getBlock();
        if(event.isCancelled()) {
            return;
        }

        if(map.containsKey(block.getId()+"")){
            float exp = Float.parseFloat(String.valueOf(map.get(block.getId()+"")));
            if(exp > 0) {
                defaultAPI.addExp(player, exp);
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent event){
        EntityDamageEvent damage = event.getEntity().getLastDamageCause();
        if(damage instanceof EntityDamageByEntityEvent){
            Entity killer = ((EntityDamageByEntityEvent) damage).getDamager();
            if(killer instanceof Player){
                int exp = AwakenSystem.getMain().getConfig().getInt(baseAPI.ConfigType.EXP_PVP.getName());
                if(exp > 0) {
                    defaultAPI.addExp((Player) killer, (float) exp);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void playerUpAwaken(PlayerUpAwakenEvent event){
        String att = event.getAwaken();
        Player player = event.getPlayer();
        if(event.isCancelled()){
            player.sendMessage("§c 出现未知阻碍 进阶失败");
            return;
        }
        LinkedList<Item> list = defaultAPI.getUseItem_Awaken(defaultAPI.getUpDataAwaken(att));
        String string = event.getNewAwaken();
        if(list != null && list.size() > 0){
            if(canRemove(player,list)){
                player.sendMessage("§e>> 恭喜 属性进阶成功");
                defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,string);
                Server.getInstance().broadcastMessage("§d恭喜玩家 "+player.getName()+"进阶属性成功 "+"§6获得了§e"+string+"§6的魔法属性");
            }else{
                player.sendMessage("§c属性进阶失败");
            }
        }else{
            player.sendMessage("§e>> 恭喜 属性进阶成功");
            defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,string);
            Server.getInstance().broadcastMessage("§d恭喜玩家 "+player.getName()+"进阶属性成功 "+"§6获得了§e"+string+"§6的魔法属性");
        }
    }

    @EventHandler
    public void playerResetAwaken(PlayerResetAwakenEvent event){
        Player player  = event.getPlayer();
        LinkedList<Item> list = defaultAPI.getRemoveItem_Awaken(event.getAwaken());
        if(event.isCancelled()){
            player.sendMessage("§c 出现未知阻碍 重置失败");
            return;
        }
        if(list != null && list.size() > 0){
            if(canRemove(player,list)){
                player.sendMessage("§c重置成功!!");
                defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,null);
            }else {
                player.sendMessage("§c重置失败");
            }
        }else{
            player.sendMessage("§c重置成功!!");
            defaultAPI.setPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE,null);
        }
    }


    @EventHandler
    public void getUI(DataPacketReceiveEvent event){
        String data;
        ModalFormResponsePacket ui;
        Player player = event.getPlayer();
        if((event.getPacket() instanceof ModalFormResponsePacket)){
        ui = (ModalFormResponsePacket)event.getPacket();
        data = ui.data.trim();
        int fromId = ui.formId;
        switch (fromId) {
            case baseAPI.modal:
                if (AwakenSystem.getMain().modaltype.containsKey(player)) {
                    switch (AwakenSystem.getMain().modaltype.get(player)) {
                        case AWAKEN:
                            if (!data.equals("null")) {
                                if (data.equals("true")) {
                                    //同意觉醒 然后判断
                                    if (EconomyAPI.getInstance().myMoney(player) < DamageMath.getReduceMoney(player)) {
                                        player.sendMessage("§c抱歉~~您的金钱不足");
                                        return;
                                    }
                                    if (!DamageMath.can_Awaken(player, AwakenSystem.getMain().Awaken.get(player))) {
                                        player.sendMessage("§c抱歉~~您的等级不足 无法觉醒");
                                        return;
                                    }
                                    if (AwakenSystem.getMain().Awaken.containsKey(player)) {
                                        defaultAPI.startNewAwaken(player, AwakenSystem.getMain().Awaken.get(player));
                                    } else {
                                        player.sendMessage("§c抱歉出现未知原因 ~~ 觉醒失败啦");
                                        defaultAPI.addPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.COUNT, 1);
                                    }
//
                                }
                                AwakenSystem.getMain().Awaken.remove(player);
                            }

                            break;
                        case RESET:
                            if (!data.equals("null")) {
                                if (data.equals("true")) {
                                    if (defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")) {
                                        player.sendMessage("§c抱歉，你没有属性 无法重置");
                                        return;
                                    }
                                    defaultAPI.startResetAwaken(player);
                                }
                            }
                            break;
                        case UpData:
                            if (!data.equals("null")) {
                                String att = defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE);
                                if (data.equals("true")) {
                                    if (att.equals("null")) {
                                        player.sendMessage("§c抱歉，你没有属性 无法进阶");
                                        return;
                                    }
                                    String string = defaultAPI.getUpDataAwaken(att);
                                    if (string != null && !string.equals("null")) {
                                        defaultAPI.startUpAwaken(player, att, string);
                                    } else {
                                        player.sendMessage("§d当前属性无法进阶");
                                    }
                                }
                            }
                            break;
                    }
                    AwakenSystem.getMain().modaltype.remove(player);
                }
                break;
            case baseAPI.chose:
                if (!data.equals("null")) {
                    String att = defaultAPI.getFinalAwaken(player).get(Integer.parseInt(data));
                    if (AwakenSystem.getMain().modaltype.containsKey(player)) {
                        AwakenSystem.getMain().modaltype.remove(player);
                    }
                    if (!defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")) {
                        player.sendMessage("§c你已经拥有属性了!!!");
                        return;
                    }
                    if (!DamageMath.can_Awaken(player, att)) {
                        player.sendMessage("§c你的等级不足 无法觉醒!!!");
                        return;
                    }
                    //判断是否为进阶属性
                    //
                    AwakenSystem.getMain().Awaken.put(player, att);

                    AwakenSystem.getMain().modaltype.put(player, baseAPI.ModalType.AWAKEN);
                    StringBuilder arr = new StringBuilder("");
                    LinkedList<Item> remove = defaultAPI.getUseItem_Awaken(att);
                    if (remove != null) {
                        arr.append("需要物品>>\n");
                        for (Item item : remove) {
                            arr.append(item.getCustomName()).append(" * ").append(item.getCount()).append("\n");
                        }
                    }
                    String texts = "§c你确定要选择觉醒" + att + "属性吗? \n§b当前成功率: §a" + DamageMath.getAwaken(player) + " %" +
                            "§d需要花费: " + DamageMath.getReduceMoney(player) + "\n" + arr.toString();
                    uiAPI.getApi().sendModal(player, texts, "当然", "我再想想");
                }
                break;
            case baseAPI.chose_Att:
                if (!data.equals("null")) {
                    switch (Integer.parseInt(data)) {
                        case 0:
                            String att = defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE);
                            if (att.equals("null")) {
                                player.sendMessage("§c你没有属性 无法重置");
                                return;
                            }
                            AwakenSystem.getMain().modaltype.put(player, baseAPI.ModalType.RESET);
                            StringBuilder arr = new StringBuilder("");
                            LinkedList<Item> remove = defaultAPI.getRemoveItem_Awaken(att);
                            if (remove != null) {
                                arr.append("需要物品>>\n");
                                for (Item item : remove) {
                                    arr.append(item.getCustomName()).append(" * ").append(item.getCount()).append("\n");
                                }
                            }
                            String texts = "§c你确定要选择重置" + att + "属性吗\n当前属性: " + att + "\n" + arr.toString();
                            uiAPI.getApi().sendModal(player, texts, "重置", "取消");
                            break;
                        case 1:
                            att = defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE);
                            if (att.equals("null")) {
                                player.sendMessage("§c你没有属性进阶失败");
                                return;
                            }
                            AwakenSystem.getMain().modaltype.put(player, baseAPI.ModalType.UpData);
                            String up = defaultAPI.getUpDataAwaken(att);
                            if (up == null) {
                                player.sendMessage("§d当前属性无法进阶");
                                return;
                            }
                            if (up.equals("null")) {
                                player.sendMessage("§d当前属性无法进阶");
                                return;
                            }

                            if (!DamageMath.studyAwaken(player)) {
                                player.sendMessage("§c抱歉 你等级不足" + defaultAPI.getStudyLevel(up) + "进阶失败");
                                return;
                            }
                            arr = new StringBuilder("");
                            remove = defaultAPI.getUseItem_Awaken(up);
                            if (remove != null) {
                                arr.append("需要物品>>\n");
                                for (Item item : remove) {
                                    arr.append(item.getCustomName()).append(" * ").append(item.getCount()).append("\n");
                                }
                            }
                            texts = "§c你确定要选择进阶" + att + "属性吗\n可进阶: " + up + "\n" + arr.toString();
                            uiAPI.getApi().sendModal(player, texts, "进阶", "取消");
                            break;
                        case 2:
                            uiAPI.getApi().sendChose(player);
                            break;

                    }
                }
                break;
            case baseAPI.from_setting:
                if (!data.equals("null")) {
                    switch (Integer.parseInt(data)) {
                        case 0:
                            StringBuilder text = new StringBuilder("");
                            text.append("§6等级 : ").append(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.LEVEL)).append("\n");
                            text.append("§6评级 : ").append(defaultAPI.getChatBySetting(player.getName())).append("\n");
                            text.append("§6属性 : ").append(defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE).equals("null")
                                    ? "无" : defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE)).append("\n");
                            text.append("§6觉醒次数 : ").append(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.COUNT)).append("\n");
                            Item item = player.getInventory().getItem(35);
                            String add = "无";
                            if (nbtItems.can_use(player, item)) {
                                add = nbtItems.getName(item);
                            }
                            text.append("§d饰品 : ").append(add).append("\n");
                            text.append("§6经验值 : ").append(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.EXP)).append(" / ").append(DamageMath.getUpDataEXP(player)).append("\n");
                            for (baseAPI.ItemADDType type : baseAPI.ItemADDType.values()) {
                                if (type.getName().equals(baseAPI.ItemADDType.EXP.getName())) {
                                    continue;
                                }
                                text.append(type.getName()).append(" : §e").append(defaultAPI.getPlayerFinalAttributeInt(player, type)).append("\n");
                            }
                            uiAPI.getApi().sendMenuMessage(player, text.toString());
                            break;
                        case 1:
                            uiAPI.getApi().sendChoseAtt(player);
                            break;
                        case 2:
                            PlayerUpScoreEvent event1 = new PlayerUpScoreEvent(player,
                                    defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT), 1);
                            Server.getInstance().getPluginManager().callEvent(event1);
                            break;
                        case 3:
                            boolean pvp;
                            if (!AwakenSystem.getMain().canPVP.containsKey(player)) {
                                AwakenSystem.getMain().canPVP.put(player, true);
                            } else {
                                AwakenSystem.getMain().canPVP.put(player, !AwakenSystem.getMain().canPVP.get(player));
                            }
                            pvp = AwakenSystem.getMain().canPVP.get(player);

                            player.sendMessage("§d[系统]§b§l您的PVP状态更改为 ---- " + (pvp ? "§c敌对" : "§a和平"));
                            break;
                    }
                }
                break;
            }
        }
    }
    private boolean is_UpScore(Player player){
        if(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT) >= DamageMath.getMaxAwaken()){
            player.sendMessage("§c抱歉~~您的评分已经到顶级啦");
            return false;
        }
        if(!DamageMath.can_UpDataAwaken(player)){
            player.sendMessage("§c抱歉~~您的等级不足，无法进阶");
            player.sendMessage("§c需要达到: "+defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT)*10);
            return false;
        }
        if(EconomyAPI.getInstance().myMoney(player) < DamageMath.getUpDataAwakenMoney(player)){
            player.sendMessage("§c抱歉~~您的金钱不足 ~~ 需要"+DamageMath.getUpDataAwakenMoney(player));
            return false;
        }
        LinkedList<Item> upItem = defaultAPI.getUpScore(defaultAPI.getPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT) + 1);
        if(upItem != null && upItem.size() > 0){
            if(canRemove(player,upItem)){
                EconomyAPI.getInstance().reduceMoney(player,DamageMath.getUpDataAwakenMoney(player));
                return true;
            }
        }else{
            EconomyAPI.getInstance().reduceMoney(player,DamageMath.getUpDataAwakenMoney(player));
            return true;
        }

        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpScore(PlayerUpScoreEvent event){
        Player player = event.getPlayer();
        if(event.isCancelled()){
            player.sendMessage("§c抱歉~~您的评分进阶失败");
            return;
        }
        if(is_UpScore(player)){
            defaultAPI.addPlayerAttributeInt(player.getName(), baseAPI.PlayerConfigType.TALENT,1);
            player.sendMessage("§b恭喜!!! 进阶成功 您的当前评级为"+defaultAPI.getChatBySetting(player.getName()));
            Server.getInstance().broadcastMessage("§d[系统]§e恭喜玩家"+player.getName()+"获得了"+defaultAPI.getChatBySetting(player.getName())+"的评级 ");
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        String message = event.getMessage();
        if(event.isCancelled()) {
            return;
        }
        String string = AwakenSystem.getMain().getConfig().getString(baseAPI.ConfigType.CHAT_MESSAGE.getName());
        if(AwakenSystem.getMain().getConfig().getBoolean(baseAPI.ConfigType.can_show_message.getName())){
            string = defaultAPI.getStr_replace(player,string);
            event.setCancelled();
            Server.getInstance().broadcastMessage(string+message);
        }




    }

//    @EventHandler
//    public void onItemInHand(PlayerItemHeldEvent e){
//        Player player = e.getPlayer();
//        if(AwakenSystem.getMain().getConfig().getBoolean("是否显示个人信息",true)) {
//            String s = AwakenSystem.getMain().getConfig().getString("个人信息内容", "");
//            if (e.getItem().getId() == 339) {
//                player.setScale(-1);
//            }
////                if (!"".equals(s)) {
////                    float yaw = (float) ((player.yaw + 180) * Math.PI / 180);
////                    AddEntityPacket pk = new AddEntityPacket();
////                    pk.type = 37;
////                    pk.entityUniqueId = 0x55ac1c;
////                    pk.speedX = 0;
////                    pk.speedY = 0;
////                    pk.speedZ = 0;
////                    pk.x = (float) (player.x + 3.2 * Math.cos(yaw));
////                    pk.y = (float) player.y;
////                    pk.z = (float) (player.z + 3.2 * Math.sin(yaw));
////                    pk.metadata = defaultAPI.getMeta(player, s);
////                    player.dataPacket(pk);
////                    System.out.println("生成");
//////                    defaultAPI.onUpdateText(player,115, s);
////                }
////            } else {
////                if (!"".equals(s)) {
//////                    defaultAPI.onUpdateText(player, 180, s);
////                    RemoveEntityPacket pk = new RemoveEntityPacket();
////                    pk.eid = 0x55ac1c;
////                    player.dataPacket(pk);
////                }
////            }
//        }
//    }

    @EventHandler
    public void onInt(PlayerInteractEvent event){
        if(event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Item item = event.getItem();
        if(item == null) {
            return;
        }
        if(nbtItems.is_Item(item)){
            if(nbtItems.can_use(player,item)){
                if(!defaultAPI.getItemConfigString(nbtItems.getName(item), baseAPI.ItemType.TYPE).equals(baseAPI.ITEM_TYPE.ORNAMENTS.getName())){
                    if(defaultAPI.getItemConfigString(nbtItems.getName(item), baseAPI.ItemType.TYPE).equals(baseAPI.ITEM_TYPE.USE.getName())){
                        LinkedHashMap<baseAPI.ItemADDType, ArrayList<Integer>> adds = nbtItems.getColde_by_use(nbtItems.getName(item));
                        if(!AwakenSystem.getMain().runAdd.containsKey(player)){
                            LinkedHashMap<baseAPI.ItemADDType,int[]> addTypeLinkedHashMap = new LinkedHashMap<>();
                            for (baseAPI.ItemADDType itemADDType:adds.keySet()){
                                addTypeLinkedHashMap.put(itemADDType,new int[]{adds.get(itemADDType).get(0),adds.get(itemADDType).get(1),adds.get(itemADDType).get(2)});
                            }
                            defaultAPI.addPlayerBuffer(player,nbtItems.getName(item),addTypeLinkedHashMap);
                            player.sendMessage(TextFormat.GREEN+"使用成功!! ");
                            player.attack(1);
                            player.level.addSound(player.getPosition(),Sound.FALL_WOOD);
                            item.setCount(1);
                            player.getInventory().removeItem(item);

                        }else{
                            LinkedHashMap<String,LinkedHashMap<baseAPI.ItemADDType,int[]>> addTime = AwakenSystem.getMain().runAdd.get(player);
                            LinkedHashMap<baseAPI.ItemADDType,int[]> addTypeLinkedHashMap = new LinkedHashMap<>();
                            if(addTime.containsKey(nbtItems.getName(item))){
                                player.sendMessage(TextFormat.GREEN+"抱歉，当前物品正在使用中");
                                return;
                            }
                            for (baseAPI.ItemADDType itemADDType:adds.keySet()){
                                addTypeLinkedHashMap.put(itemADDType,new int[]{adds.get(itemADDType).get(0),adds.get(itemADDType).get(1),adds.get(itemADDType).get(2)});
                            }
                            defaultAPI.addPlayerBuffer(player,nbtItems.getName(item),addTypeLinkedHashMap);
                            player.attack(1);
                            player.level.addSound(player.getPosition(),Sound.FALL_WOOD);
                            player.sendMessage(TextFormat.GREEN+"使用成功!! ");
                            item.setCount(1);
                            player.getInventory().removeItem(item);
                        }
                    }else {
                        LinkedHashMap<baseAPI.ItemADDType, Integer> adds = nbtItems.getColde_by_Ring(nbtItems.getName(item));
                        for (baseAPI.ItemADDType itemADDType:adds.keySet()){
                            if(itemADDType.getName().equals(baseAPI.ItemADDType.EXP.getName())){
                                defaultAPI.addExp(player,adds.get(itemADDType));
                                continue;
                            }
                            defaultAPI.addPlayerAttributeInt(player.getName(),itemADDType,adds.get(itemADDType));
                        }
                        item.setCount(1);
                        player.getInventory().removeItem(item);
                        player.attack(1);
                        player.sendMessage(TextFormat.GREEN+"使用成功!! ");
                        player.level.addSound(player.getPosition(),Sound.FALL_WOOD);
                    }
                }else {
                    player.sendMessage(TextFormat.YELLOW+"请将饰品放在背包的最后一格");
                }
            }else {
                player.sendMessage(TextFormat.RED+"抱歉,,你不能使用此物品");
            }
        }
        item = player.getInventory().getItemInHand();
        if(item == null) {
            return;
        }
        if(CommandItems.is_NbtItem(item)){
            String itemName = CommandItems.getName(item);
            if(CommandItems.is_Exit(itemName)){
                LinkedHashMap<String,String> commands = CommandItems.getInstance(itemName).getUseCommand();
                if(commands != null && commands.size() > 0){
                    for (String name:commands.keySet()){
                        String command = name.replace("@p",player.getName());
                        Server.getInstance().dispatchCommand(new ConsoleCommandSender(),command);
                        item.setCount(1);
                        player.getInventory().removeItem(item);
                        player.sendMessage(commands.get(name));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void addBufferEvent(PlayerAddBufferEvent event){
        Player player = event.getPlayer();

        PlayerBuffer buffer = new PlayerBuffer(player,event.getBufferName());
        AwakenSystem.getMain().runAdd.put(player,buffer.addBuffer(event.getBuffers()));
        player.sendPopup("你获得了 "+event.getBufferName()+"Buff");
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onOrnamentsEvent(PlayerOrnamentsEvent event){
        Player player = event.getPlayer();
        if(event.getType() == PlayerOrnamentsEvent.wear){
            Item item = event.getItem();
            if(nbtItems.can_use(player,item)){
                player.sendMessage("§d[系统]§b饰品 "+nbtItems.getName(item)+"装备成功");
                player.attack(1);
                LinkedHashMap<baseAPI.ItemADDType, Integer> add = nbtItems.getColde_by_Ring(nbtItems.getName(item));
                for (baseAPI.ItemADDType type:add.keySet()){
                    if(type.getName().equals(baseAPI.ItemADDType.EXP.getName())){
                        player.sendMessage("§e◎- "+type.getName()+" + "+add.get(type)+"％");
                        continue;
                    }
                    player.sendMessage("§e◎- "+type.getName()+" + "+add.get(type));
                }

            }
        }else if(event.getType() == PlayerOrnamentsEvent.remove){
            Item item = event.getItem();
            player.sendMessage("§d[系统]§b饰品 "+nbtItems.getName(item)+"装备移除");
            player.attack(1);
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void moveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(!"null".equals(defaultAPI.getPlayerAttributeString(player.getName(), baseAPI.PlayerConfigType.ATTRIBUTE))){

            defaultAPI.showParticle(player);
        }
        if(AwakenSystem.getMain().getConfig().getBoolean("是否显示个人信息",true)) {
//            defaultAPI.onUpdateText(player);
        }
    }



}
